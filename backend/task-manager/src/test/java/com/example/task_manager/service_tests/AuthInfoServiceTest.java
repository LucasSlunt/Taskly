package com.example.task_manager.service_tests;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthInfoServiceTest {

    @Test
    void testHashPassword() {
        //to be implemented with hashPassword method
    }

    @Test
    void testApproveLogin() {
        //to be implemented with approveLogin method
    }
}
