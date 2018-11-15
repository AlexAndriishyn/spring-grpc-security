package com.alex.example.security

import org.apache.commons.codec.binary.Base64

import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec

class ResourceKeyReader {

    static readPrivateKey() {
        def key = getClass().getResource('/private_key.pkcs8.pem').getText()
        def stripped = key.replace('-----BEGIN PRIVATE KEY-----\n', '')
            .replace('-----END PRIVATE KEY-----', '')
        def der = Base64.decodeBase64(stripped)
        def spec = new PKCS8EncodedKeySpec(der)
        def kf = KeyFactory.getInstance('RSA')

        return (RSAPrivateKey) kf.generatePrivate(spec)
    }

    static readPublicKey() {
        def key = getClass().getResource('/public_key.pkcs8.pem').getText()
        def stripped = key.replace('-----BEGIN PUBLIC KEY-----\n', '')
            .replace('-----END PUBLIC KEY-----', '')
        def der = Base64.decodeBase64(stripped)
        def spec = new X509EncodedKeySpec(der)
        def kf = KeyFactory.getInstance('RSA')

        return (RSAPublicKey) kf.generatePublic(spec)
    }
}
