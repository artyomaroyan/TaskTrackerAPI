package org.tasktracker.demo.user.application.ports.out;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Author: Artyom Aroyan
 * Date: 24.11.25
 * Time: 20:37:00
 */
public interface KeyProvider {
    PrivateKey getPrivateKey();
    PublicKey getPublicKey();
}