package com.alex.example.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm

import java.security.interfaces.RSAPrivateKey

class JWTStore {

    RSAPrivateKey privateKey

    JWTStore() {
        this.privateKey = ResourceKeyReader.readPrivateKey()
    }

    def createJWT() {
        return Jwts.builder()
            .setIssuer('leonardo')
            .setSubject('alex')
            .setAudience('service/greeter')
            .signWith(SignatureAlgorithm.RS256, privateKey)
            .compact()
    }
}
