package com.alex.example.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Created by alex.
 */
@Component
public class JWTParser {

    private final KeyProvider keyProvider;
    private final Algorithm algorithm;

    @Inject
    public JWTParser(KeyProvider keyProvider) {
        this.keyProvider = keyProvider;
        algorithm = Algorithm.RSA256(
            (RSAPublicKey) keyProvider.getPublicKey(),
            (RSAPrivateKey) keyProvider.getPrivateKey()
        );
    }

    /**
     * Parse a token and extract the subject id.
     *
     * @param token JWT token to get the subject from
     */
    public void validate(String token) {
        JWTVerifier verifier = JWT.require(algorithm)
            .withIssuer("Leonardo")
            .acceptExpiresAt(60)
            .acceptIssuedAt(60)
            .build();

        DecodedJWT jwt = verifier.verify(token);
    }
}
