package org.tasktracker.demo.userservice.infrastructure.configuration;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tasktracker.demo.userservice.application.ports.out.KeyProvider;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 17.01.26
 * Time: 22:09:12
 */
@Configuration
public class RsaKeyConfiguration {
    private final KeyProvider keyProvider;

    private RsaKeyConfiguration(KeyProvider keyProvider) {
        this.keyProvider = keyProvider;
    }

    @Bean
    public RSAKey rsaKey() {
        KeyPair keyPair = loadKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .algorithm(JWSAlgorithm.RS256)
                .build();
    }

    private KeyPair loadKeyPair() throws UnsupportedOperationException {
        PublicKey publicKey = keyProvider.getPublicKey();
        PrivateKey privateKey = keyProvider.getPrivateKey();
        return new KeyPair(publicKey, privateKey);
    }
}