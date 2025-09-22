package org.cryptotrader.api.library.services.rsa

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*

/**
 * Provides an RSA key pair for signing and verifying access tokens and exposes a stable kid.
 * If no keys are configured, an ephemeral dev key is generated at startup.
 *
 * Properties (optional):
 * - security.jwt.rsa.public-key-pem: Base64 (PEM without headers) public key (X.509 SubjectPublicKeyInfo)
 * - security.jwt.rsa.private-key-pem: Base64 (PEM without headers) private key (PKCS#8)
 * - security.jwt.kid: Optional static key id; otherwise computed from public key SHA-256.
 */
@Service
class RsaKeyService(
    @Value("\${security.jwt.rsa.public-key-pem:}") private val publicPem: String?,
    @Value("\${security.jwt.rsa.private-key-pem:}") private val privatePem: String?,
    @Value("\${security.jwt.kid:}") private val configuredKid: String?
) {
    val publicKey: RSAPublicKey
    val privateKey: RSAPrivateKey
    val kid: String

    init {
        if (!this.publicPem.isNullOrBlank() && !this.privatePem.isNullOrBlank()) {
            val publicKeyBytes = Base64.getDecoder().decode(this.stripPemHeaders(this.publicPem))
            val privateKeyBytes = Base64.getDecoder().decode(this.stripPemHeaders(this.privatePem))
            val keyFactory = this.getRsaKeyFactory()
            this.publicKey = keyFactory.generatePublic(X509EncodedKeySpec(publicKeyBytes)) as RSAPublicKey
            this.privateKey = keyFactory.generatePrivate(PKCS8EncodedKeySpec(privateKeyBytes)) as RSAPrivateKey
            this.kid = this.configuredKid ?: this.computeKid(this.publicKey)
            log.info("Loaded RSA keys from configuration; kid={}", this.kid)
        } else {
            val keyPair = this.generateEphemeralKeyPair()
            this.publicKey = keyPair.public as RSAPublicKey
            this.privateKey = keyPair.private as RSAPrivateKey
            this.kid = this.configuredKid ?: this.computeKid(this.publicKey)
            log.warn("Generated ephemeral RSA key pair for JWT signing (dev only); kid={}", this.kid)
        }
    }

    private fun stripPemHeaders(pem: String): String = pem
        .replace("-----BEGIN PUBLIC KEY-----", "")
        .replace("-----END PUBLIC KEY-----", "")
        .replace("-----BEGIN PRIVATE KEY-----", "")
        .replace("-----END PRIVATE KEY-----", "")
        .replace("\n", "")
        .replace("\r", "")
        .trim()

    private fun getRsaKeyFactory(): KeyFactory = KeyFactory.getInstance("RSA")

    private fun generateEphemeralKeyPair(): KeyPair {
        val generator = KeyPairGenerator.getInstance("RSA")
        generator.initialize(2048)
        return generator.genKeyPair()
    }

    private fun computeKid(pub: RSAPublicKey): String {
        // kid = base64url(SHA-256(n || e)) minimal and deterministic for our purposes
        val modulusBytes = pub.modulus.toByteArray()
        val exponentBytes = pub.publicExponent.toByteArray()
        val data = ByteArray(modulusBytes.size + exponentBytes.size)
        System.arraycopy(modulusBytes, 0, data, 0, modulusBytes.size)
        System.arraycopy(exponentBytes, 0, data, modulusBytes.size, exponentBytes.size)
        val sha = java.security.MessageDigest.getInstance("SHA-256").digest(data)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(sha)
    }

    companion object {
        private val log = LoggerFactory.getLogger(RsaKeyService::class.java)
    }
}
