package org.tasktracker.demo.userservice.security;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.*;
import java.util.Base64;

/**
 * Author: Artyom Aroyan
 * Date: 07.11.25
 * Time: 00:33:00
 */
@Component
public class JwtKeyGenerator {
    @Value("${spring.security.jwt.key-siz}")
    private int keySize;
    @Value("${spring.security.jwt.algorithm}")
    private String algorithm;

    private KeyPair keyPair;

    @PostConstruct
    void init() {
        this.keyPair = generateKeyPair();
    }

    public String getPublicKeyBase64() {
        return Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
    }

    public String getPrivateKeyBase64() {
        return Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
    }

    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return keyPair.getPrivate();
    }

    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(algorithm);
            generator.initialize(keySize);
            return generator.generateKeyPair();
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Error generating key pair", ex);
        }
    }
}