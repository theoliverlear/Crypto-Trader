import {Injectable} from "@angular/core";
import {environment} from "../../../environments/environment";
import {DpopKeyStoreService} from "./dpop-key-store.service";
import {PossibleCryptoKeyPair, PossibleJsonWebKey} from "./types";

/**
 * Manages the client's DPoP key pair using WebCrypto.
 * - Generates a non-extractable private key (ECDSA P-256)
 * - Exports the public key as JWK
 * - Computes RFC7638 JWK thumbprint (jkt)
 * - Signs JWS inputs for DPoP proofs (ES256)
 *
 * Note: Uses ECDSA P-256 and converts DER WebCrypto signatures to JOSE (r|s) format.
 */
@Injectable({
    providedIn: 'root'
})
export class DpopKeyService {
    private keyPair: PossibleCryptoKeyPair = null;
    private cachedJwk: PossibleJsonWebKey = null;
    private cachedJkt: string | null = null;

    // TODO: Clean up code.
    constructor(private store: DpopKeyStoreService) {}

    /** Ensure a keypair exists. */
    async ensureKeys(): Promise<void> {
        if (this.keyPair) return;

        // Try to load from IndexedDB if enabled
        if (environment.persistDpopKey) {
            try {
                const loaded = await this.store.load();
                if (loaded) {
                    this.keyPair = loaded;
                    this.cachedJwk = null;
                    this.cachedJkt = null;
                    return;
                }
            } catch { /* ignore */ }
        }

        // Generate a new EC P-256 key pair for ES256
        this.keyPair = await crypto.subtle.generateKey(
            {
                name: 'ECDSA',
                namedCurve: 'P-256'
            },
            false, // private key non-extractable
            ['sign', 'verify']
        ) as CryptoKeyPair;

        // Persist if enabled
        if (environment.persistDpopKey) {
            try { await this.store.save(this.keyPair); } catch { /* ignore */ }
        }

        this.cachedJwk = null;
        this.cachedJkt = null;
    }

    /** Export the public key as a JWK (cached). */
    async getPublicJwk(): Promise<JsonWebKey> {
        await this.ensureKeys();
        if (!this.cachedJwk) {
            this.cachedJwk = await crypto.subtle.exportKey('jwk', this.keyPair!.publicKey);
            // normalize fields for EC
            this.cachedJwk.kty = 'EC';
            (this.cachedJwk as any).alg = 'ES256';
            (this.cachedJwk as any).key_ops = ['verify'];
            (this.cachedJwk as any).ext = true;
        }
        return this.cachedJwk!;
    }

    /** Compute and cache the RFC7638 JWK thumbprint (jkt). */
    async getJkt(): Promise<string> {
        if (this.cachedJkt) return this.cachedJkt;
        const jwk: any = await this.getPublicJwk();
        // Canonical JSON with required members for EC in lexicographic order: {"crv":"P-256","kty":"EC","x":"...","y":"..."}
        const canonical = `{"crv":"${jwk.crv}","kty":"EC","x":"${jwk.x}","y":"${jwk.y}"}`;
        const digest = await crypto.subtle.digest('SHA-256', new TextEncoder().encode(canonical));
        this.cachedJkt = base64url(new Uint8Array(digest));
        return this.cachedJkt;
    }

    /** Sign an input (string) using the private key (ECDSA P-256), returning base64url(JOSE r|s) signature */
    async signInput(input: string): Promise<string> {
        await this.ensureKeys();
        const data = new TextEncoder().encode(input);
        const derSig = await crypto.subtle.sign(
            { name: 'ECDSA', hash: 'SHA-256' },
            this.keyPair!.privateKey,
            data
        );
        const jose = derToJose(new Uint8Array(derSig), 32);
        return base64url(jose);
    }

    /** Reset and forget keys (e.g., on logout). */
    clear(): void {
        this.keyPair = null;
        this.cachedJwk = null;
        this.cachedJkt = null;
        if (environment.persistDpopKey) {
            // Fire-and-forget
            this.store.clear();
        }
    }
}

export function base64url(bytesOrString: Uint8Array | string): string {
    let bytes: Uint8Array;
    if (typeof bytesOrString === 'string') {
        bytes = new TextEncoder().encode(bytesOrString);
    } else {
        bytes = bytesOrString;
    }
    let str = '';
    const chunk = 0x8000;
    for (let i = 0; i < bytes.length; i += chunk) {
        str += String.fromCharCode.apply(null, Array.from(bytes.subarray(i, i + chunk)) as unknown as number[]);
    }
    const b64 = btoa(str).replace(/\+/g, '-').replace(/\//g, '_').replace(/=+$/g, '');
    return b64;
}

/** Convert ASN.1/DER ECDSA signature to JOSE r|s raw format of fixed size. Accepts raw r||s too. */
function derToJose(derSignature: Uint8Array, coordinateSize: number = 32): Uint8Array {
    // Accept already-raw signatures (r||s) of expected length
    if (derSignature.length === coordinateSize * 2) {
        return derSignature;
    }

    // Minimal ASN.1 DER parser for ECDSA signatures: SEQUENCE( r INTEGER, s INTEGER )
    let offset = 0;
    const buf = derSignature;

    // Expect DER SEQUENCE (0x30)
    if (buf[offset++] !== 0x30) {
        throw new Error('Invalid ECDSA signature format');
    }

    // Read DER length (short or long form)
    let lengthByte = buf[offset++];
    let sequenceLength = 0;
    if (lengthByte < 0x80) {
        sequenceLength = lengthByte;
    } else {
        const numLenBytes = lengthByte & 0x7f;
        sequenceLength = 0;
        for (let i = 0; i < numLenBytes; i++) {
            sequenceLength = (sequenceLength << 8) | buf[offset++];
        }
    }

    if (buf[offset++] !== 0x02) throw new Error('Invalid DER r INTEGER');
    let rLength = buf[offset++];
    let rInteger = buf.slice(offset, offset + rLength);
    offset += rLength;

    if (buf[offset++] !== 0x02) throw new Error('Invalid DER s INTEGER');
    let sLength = buf[offset++];
    let sInteger = buf.slice(offset, offset + sLength);

    // Strip leading zeros if present (ensures positive INTEGER)
    if (rInteger.length > 0 && rInteger[0] === 0x00) {
        rInteger = rInteger.slice(1);
    }
    if (sInteger.length > 0 && sInteger[0] === 0x00) {
        sInteger = sInteger.slice(1);
    }

    // Left pad to fixed coordinate size
    const rPadded = new Uint8Array(coordinateSize);
    if (rInteger.length > coordinateSize) {
        rInteger = rInteger.slice(rInteger.length - coordinateSize);
    }
    rPadded.set(rInteger, coordinateSize - rInteger.length);

    const sPadded = new Uint8Array(coordinateSize);
    if (sInteger.length > coordinateSize) {
        sInteger = sInteger.slice(sInteger.length - coordinateSize);
    }
    sPadded.set(sInteger, coordinateSize - sInteger.length);

    const joseSignature = new Uint8Array(coordinateSize * 2);
    joseSignature.set(rPadded, 0);
    joseSignature.set(sPadded, coordinateSize);
    return joseSignature;
}
