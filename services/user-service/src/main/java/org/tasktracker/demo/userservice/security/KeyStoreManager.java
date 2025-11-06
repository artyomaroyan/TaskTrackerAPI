package org.tasktracker.demo.userservice.security;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Author: Artyom Aroyan
 * Date: 07.11.25
 * Time: 00:40:54
 */
@Component
public class KeyStoreManager {
    private final JwtKeyGenerator jwtKeyGenerator;

    public KeyStoreManager(JwtKeyGenerator jwtKeyGenerator) {
        this.jwtKeyGenerator = jwtKeyGenerator;
    }

    @PostConstruct
    void storeKeysInEnvironment() {
        System.setProperty("JWT_PUBLIC_KEY", jwtKeyGenerator.getPublicKeyBase64());
        System.setProperty("JWT_PRIVATE_KEY", jwtKeyGenerator.getPrivateKeyBase64());
        // In production, these would be set via:
        // - Kubernetes Secrets
        // - AWS Parameter Store
        // - Spring Cloud Config
        // - Environment variables from deployment
    }

    public PublicKey loadPublicKey() {
        try {
            String publicKeyBase64 = System.getenv("JWT_PUBLIC_KEY");
            if (publicKeyBase64 == null) {
                publicKeyBase64 = System.getProperty("JWT_PUBLIC_KEY");
            }

            byte[] keyBytes = Base64.getDecoder().decode(publicKeyBase64);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
        } catch (Exception ex) {
            throw new RuntimeException("Error loading public key", ex);
        }
    }

    public PrivateKey loadPrivateKey() {
        try {
            String privateKeyBase64 = System.getenv("JWT_PRIVATE_KEY");
            if (privateKeyBase64 == null) {
                privateKeyBase64 = System.getProperty("JWT_PRIVATE_KEY");
            }

            byte[] keyBytes = Base64.getDecoder().decode(privateKeyBase64);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
        } catch (Exception ex) {
            throw new RuntimeException("Error loading private key", ex);
        }
    }
}