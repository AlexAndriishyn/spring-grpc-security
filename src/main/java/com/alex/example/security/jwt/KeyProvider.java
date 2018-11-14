package com.alex.example.security.jwt;

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
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by alex.
 */
@Component
public class KeyProvider {

    private final PublicKey publicKey;
    private final PrivateKey privateKey;

    @Inject
    public KeyProvider(
        @Value("${publicKeyFile:classpath:private.pem}") final Resource privateKeyResource,
        @Value("${publicKeyFile:classpath:public.pem}") final Resource publicKeyResource
    ) throws Exception {
        final String publicKey = readKey(publicKeyResource);
        final String privateKey = readKey(privateKeyResource);
        this.publicKey = getPublicKey(publicKey);
        this.privateKey = getPrivateKey(privateKey);
    }

    public KeyProvider(final PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    private PublicKey getPublicKey(final String publicKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
        final byte[] publicKeyDER = Base64.decodeBase64(publicKey);
        final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyDER);
        final KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePublic(keySpec);
    }

    private PrivateKey getPrivateKey(final String privateKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
        final byte[] privateKeyDer = Base64.decodeBase64(privateKey);
        final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyDer);
        final KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePrivate(keySpec);
    }

    private String readKey(final Resource keyResource) throws IOException, URISyntaxException {
        final byte[] data = FileCopyUtils.copyToByteArray(keyResource.getInputStream());
        String key = new String(data, StandardCharsets.UTF_8);

        // strip of header, footer, newlines, whitespaces
        key = key
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replace("-----BEGIN RSA PRIVATE KEY-----", "")
            .replace("-----END RSA PRIVATE KEY----", "")
            .replaceAll("\\s", "");

        return key;
    }
}
