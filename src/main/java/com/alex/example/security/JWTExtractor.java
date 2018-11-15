package com.alex.example.security;

import io.grpc.Metadata;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;

@Component
public class JWTExtractor {

    private final Metadata.Key<String> AUTHORIZATION_METADATA_KEY = Metadata.Key.of("Authorization", ASCII_STRING_MARSHALLER);

    // Always expect a signature since we create only signed JWTs.
    private final Pattern pattern = Pattern.compile("^Bearer (?<JWT>[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_.+/=]+)$");

    public String extract(Metadata headers) throws JWTExtractorException {
        String authorizationHeaderValue = readAuthorizationHeader(headers);

        return extractJWTFromAuthorizationValue(authorizationHeaderValue);
    }

    private String readAuthorizationHeader(Metadata headers) throws JWTExtractorException {
        String authorization = headers.get(AUTHORIZATION_METADATA_KEY);

        if (StringUtils.isEmpty(authorization)) {
            throw new JWTExtractorException("authorization header is empty, expected JWT");
        }

        return authorization;
    }

    private String extractJWTFromAuthorizationValue(String value) throws JWTExtractorException {
        Matcher matcher = pattern.matcher(value);

        if (matcher.groupCount() != 1) {
            throw new JWTExtractorException("invalid authorization header value, expected \"Bearer ${JWT}\"");
        }

        return matcher.group("JWT");
    }
}
