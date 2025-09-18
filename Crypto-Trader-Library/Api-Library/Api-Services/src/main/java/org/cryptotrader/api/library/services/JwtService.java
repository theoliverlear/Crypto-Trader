package org.cryptotrader.api.library.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {
    private final Algorithm algorithm;
    private final String issuer;
    private final long ttlSeconds;

    public JwtService(
            @Value("${security.jwt.secret:changeme-please-set-SECURITY_JWT_SECRET}") String secret,
            @Value("${security.jwt.issuer:crypto-trader-api}") String issuer,
            @Value("${security.jwt.ttl-seconds:3600}") long ttlSeconds
    ) {
        // Auth0 library will handle HMAC256 key size appropriately
        this.algorithm = Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));
        this.issuer = issuer;
        this.ttlSeconds = ttlSeconds;
    }

    public String generateToken(String subject, String email) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(ttlSeconds);
        return JWT.create()
                .withIssuer(issuer)
                .withSubject(subject)
                .withClaim("email", email)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(exp))
                .sign(algorithm);
    }

    public JwtClaims validateAndParse(String token) {
        DecodedJWT jwt = JWT.require(algorithm)
                .withIssuer(issuer)
                .build()
                .verify(token);
        String subject = jwt.getSubject();
        String email = jwt.getClaim("email").asString();
        Date iat = jwt.getIssuedAt();
        Date exp = jwt.getExpiresAt();
        return new JwtClaims(subject, email, iat, exp);
    }
}
