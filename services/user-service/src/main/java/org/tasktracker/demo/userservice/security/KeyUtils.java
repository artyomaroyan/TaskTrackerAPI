package org.tasktracker.demo.userservice.security;

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
 * Date: 13.11.25
 * Time: 19:38:48
 */
final class KeyUtils {
    private static final String JWT_ALGORITHM = "RSA";

    private KeyUtils() {
    }

    static PrivateKey loadPrivateKey() throws Exception {
        final String key = readKeyFromResource("/Users/artyom_aroyan/Software/Java/.keys/private_key.pem")
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        final byte[] bytes = Base64.getDecoder().decode(key);
        final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        return KeyFactory.getInstance(JWT_ALGORITHM).generatePrivate(keySpec);
    }

    static PublicKey loadPublicKey() throws Exception {
        final String key = readKeyFromResource("/Users/artyom_aroyan/Software/Java/.keys/public_key.pem")
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        final byte[] bytes = Base64.getDecoder().decode(key);
        final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
        return KeyFactory.getInstance(JWT_ALGORITHM).generatePublic(keySpec);
    }

    private static String readKeyFromResource(final String path) throws Exception {
        return new String(Files.readAllBytes(Paths.get(path)));
    }
}