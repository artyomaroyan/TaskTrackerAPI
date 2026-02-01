package org.tasktracker.demo.userservice.presentation.web;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tasktracker.demo.userservice.application.ports.out.KeyProvider;

import java.security.interfaces.RSAPublicKey;
import java.util.Map;

/**
 * Author: Artyom Aroyan
 * Date: 17.01.26
 * Time: 22:19:19
 */
@RestController
@RequiredArgsConstructor
public class JwksController {
    private final KeyProvider keyProvider;

    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> jwks() {
        RSAPublicKey publicKey = (RSAPublicKey) keyProvider.getPublicKey();
        RSAKey jwks = new RSAKey.Builder(publicKey)
                .keyID("user-service-key")
                .build();
        return new JWKSet(jwks).toJSONObject();
    }
}