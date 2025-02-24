package com.example.task_manager.service_tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.task_manager.service.AuthInfoService;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthInfoServiceTest {

    @Test
    void testHashPassword() {
        //check to see if the password is hashed correctly, without using a salt
        String expectedHash = "c805f21c578f8d4a5109881058761e3cb2b52ad1d3c1d2a4aa9afda4d4706eca";
        String generatedHash = AuthInfoService.hashPassword("testPassword","testSalt");
        assertEquals(generatedHash,expectedHash);
    }

    @Test
    void testGenerateSalt() {
        String salt = AuthInfoService.generateSalt();
        assertTrue(salt.length() == 16);
    }

    @Test 
    void testSaltsAreUnique(){
        String salt1 = AuthInfoService.generateSalt();
        String salt2 = AuthInfoService.generateSalt();
        assertFalse(salt1.equals(salt2));
    }

    @Test
    void testApproveLogin() {
        //to be implemented with approveLogin method
    }
}
