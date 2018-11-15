package com.alex.example.security;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by alex.
 */
@Component
final class KeyProvider {

    private final PublicKey publicKey;

    @Inject
    KeyProvider(
        @Value("${publicKeyFile:classpath:public_key.pkcs8.pem}") final Resource publicKeyResource
    ) throws Exception {
        final String publicKey = readKey(publicKeyResource);
        this.publicKey = getPublicKey(publicKey);
    }

    KeyProvider(final PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    PublicKey getPublicKey() {
        return this.publicKey;
    }

    private PublicKey getPublicKey(final String publicKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
        final byte[] publicKeyDER = Base64.decodeBase64(publicKey);
        final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyDER);
        final KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePublic(keySpec);
    }

    private String readKey(final Resource keyResource) throws IOException, URISyntaxException {
        final byte[] data = FileCopyUtils.copyToByteArray(keyResource.getInputStream());
        String key = new String(data, StandardCharsets.UTF_8);

        // strip of header, footer, newlines, whitespaces
        key = key.replace("-----BEGIN PUBLIC KEY-----\n", "")
            .replace("-----END PUBLIC KEY-----", "");

        return key;
    }
}
