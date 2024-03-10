package com.example.sklep.model;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import java.security.SecureRandom;
import java.util.Base64;

public class PasswordHasher {
    private static final Argon2 argon2 = Argon2Factory.create();


    public String hash(String password) {
        String hash = argon2.hash(2, 19456, 1, password );
        System.out.println(hash);

        return hash;
    }

    public boolean matches(String rawPassword, String hashedPassword) {
        return argon2.verify(hashedPassword, rawPassword);
    }


}
