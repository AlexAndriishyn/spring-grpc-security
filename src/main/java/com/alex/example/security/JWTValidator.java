package com.alex.example.security;

import io.grpc.Metadata;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Map;

@Component
public class JWTValidator {

    private final KeyProvider keyProvider;
    private final JWTExtractor jwtExtractor;

    @Inject
    public JWTValidator(KeyProvider keyProvider, JWTExtractor jwtExtractor) {
        this.keyProvider = keyProvider;
        this.jwtExtractor = jwtExtractor;
    }

    public void validate(Metadata headers, Map<String, String> claims) throws JWTValidationException {
        String jwt = extractJwt(headers);
        validateClaims(jwt, claims);
    }

    private String extractJwt(Metadata headers) throws JWTValidationException {
        try {
            return jwtExtractor.extract(headers);
        } catch (JWTExtractorException ex) {
            throw new JWTValidationException(ex);
        }
    }

    private void validateClaims(String jwt, Map<String, String> claims) throws JWTValidationException {
        try {
            Jwts.parser()
                .setSigningKey(keyProvider.getPublicKey())
                .requireIssuer(claims.get("iss"))
                .requireSubject(claims.get("sub"))
                .requireAudience(claims.get("aud"))
                .parseClaimsJws(jwt);
        } catch (JwtException ex) {
            throw new JWTValidationException(ex);
        }
    }
}
