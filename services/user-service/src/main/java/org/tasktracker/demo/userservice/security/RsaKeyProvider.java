package org.tasktracker.demo.userservice.security;

import org.springframework.stereotype.Component;
import org.tasktracker.demo.userservice.application.ports.out.KeyProvider;
import org.tasktracker.demo.userservice.infrastructure.configuration.JwtProperties;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Author: Artyom Aroyan
 * Date: 24.11.25
 * Time: 20:37:36
 */
@Component
public class RsaKeyProvider implements KeyProvider {
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    RsaKeyProvider(JwtProperties jwtProperties) {
        try {
            this.privateKey = loadPrivateKey(jwtProperties);
            this.publicKey = loadPublicKey(jwtProperties);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to load keys! " + ex.getMessage());
        }
    }

    @Override
    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    @Override
    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    private PrivateKey loadPrivateKey(JwtProperties jwtProperties) throws Exception {
        String keyContent = readKeyContent(jwtProperties.rsaPrivateKeyPath());
        String privateKeyPem = keyContent
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] encoded = Base64.getDecoder().decode(privateKeyPem);
        KeyFactory keyFactory = KeyFactory.getInstance(jwtProperties.algorithm());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        return keyFactory.generatePrivate(keySpec);
    }

    private PublicKey loadPublicKey(JwtProperties jwtProperties) throws Exception {
        String keyContent = readKeyContent(jwtProperties.rsaPublicKeyPath());
        String publicKeyPem = keyContent
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] encoded = Base64.getDecoder().decode(publicKeyPem);
        KeyFactory keyFactory = KeyFactory.getInstance(jwtProperties.algorithm());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        return keyFactory.generatePublic(keySpec);
    }

    private static String readKeyContent(final String path) throws Exception {
        return new String(Files.readAllBytes(Paths.get(path)));
    }
}