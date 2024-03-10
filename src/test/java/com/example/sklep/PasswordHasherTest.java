package com.example.sklep;

import static org.junit.jupiter.api.Assertions.*;

import com.example.sklep.model.PasswordHasher;
import org.junit.jupiter.api.Test;

class PasswordHasherTest {

    @Test
    void testHashAndMatch() {
        PasswordHasher passwordHasher = new PasswordHasher();


        String rawPassword = "SecurePassword123";
        String hashedPassword = passwordHasher.hash(rawPassword);
        assertTrue(passwordHasher.matches(rawPassword, hashedPassword));
    }

    @Test
    void testMismatchedPasswords() {
        PasswordHasher passwordHasher = new PasswordHasher();


        String rawPassword = "Password123";
        String incorrectPassword = "WrongPassword456";
        String hashedPassword = passwordHasher.hash(rawPassword);
        assertFalse(passwordHasher.matches(incorrectPassword, hashedPassword));
    }

    @Test
    void testEmptyPassword() {
        PasswordHasher passwordHasher = new PasswordHasher();


        String rawPassword = "";
        String hashedPassword = passwordHasher.hash(rawPassword);
        assertTrue(passwordHasher.matches(rawPassword, hashedPassword));
    }

    @Test
    void testNullPassword() {
        PasswordHasher passwordHasher = new PasswordHasher();
        assertThrows(NullPointerException.class, () -> {
            passwordHasher.hash(null);
        });
    }
}
