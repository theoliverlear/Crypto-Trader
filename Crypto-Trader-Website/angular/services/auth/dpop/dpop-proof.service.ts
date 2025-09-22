import {Injectable} from "@angular/core";
import {DpopKeyService, base64url} from "./dpop-key.service";

@Injectable({ 
    providedIn: 'root' 
})
export class DpopProofService {
    // TODO: Clean up code.
    constructor(private keys: DpopKeyService) {}

    /**
     * Build a DPoP proof JWT (compact JWS) for the given request
     * method and absolute URL. Optionally include 'ath' for the
     * presented access token.
     */
    async buildProof(method: string, absoluteUrl: string, accessToken?: string): Promise<string> {
        await this.keys.ensureKeys();
        const jwk = await this.keys.getPublicJwk();

        const header = {
            alg: 'ES256',
            typ: 'dpop+jwt',
            jwk
        } as const;

        const now = Math.floor(Date.now() / 1000);
        const payload: any = {
            htm: method,
            htu: absoluteUrl,
            iat: now,
            jti: this.generateNonce()
        };
        if (accessToken) {
            payload.ath = await this.sha256Base64url(accessToken);
        }

        const encodedHeader = base64url(JSON.stringify(header));
        const encodedPayload = base64url(JSON.stringify(payload));
        const signingInput = `${encodedHeader}.${encodedPayload}`;
        const signature = await this.keys.signInput(signingInput);
        return `${encodedHeader}.${encodedPayload}.${signature}`;
    }

    /** Generate a UUID v4 nonce for DPoP jti. */
    private generateNonce(): string {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, (patternChar) => {
            const randomNibble = crypto.getRandomValues(new Uint8Array(1))[0] & 0x0f;
            const selectedNibble = patternChar === 'x' ? randomNibble : ((randomNibble & 0x3) | 0x8);
            return selectedNibble.toString(16);
        });
    }

    /** Compute base64url(SHA-256(input)) used for the optional 'ath' claim. */
    private async sha256Base64url(input: string): Promise<string> {
        const bytes = new TextEncoder().encode(input);
        const digest = await crypto.subtle.digest('SHA-256', bytes);
        return base64url(new Uint8Array(digest));
    }
}
