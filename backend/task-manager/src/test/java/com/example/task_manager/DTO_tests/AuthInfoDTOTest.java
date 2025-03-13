package com.example.task_manager.DTO_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.example.task_manager.DTO.AuthInfoDTO;

public class AuthInfoDTOTest {
    @Test
    void testDTOConstructorAndGetters() {
        AuthInfoDTO authInfoDTO = new AuthInfoDTO(1, "User Name", false);

        assertEquals(1, authInfoDTO.getAccountId());
        assertEquals("User Name", authInfoDTO.getUserName());
        assertFalse(authInfoDTO.getIsAdmin());
    }

    @Test
    void testDTOSetters() {
        AuthInfoDTO authInfoDTO = new AuthInfoDTO(1, "User Name", false);

        authInfoDTO.setAccountId(2);
        authInfoDTO.setUserName("Updated Name");
        authInfoDTO.setIsAdmin(true);

        assertEquals("Updated Name", authInfoDTO.getUserName());
        assertTrue(authInfoDTO.getIsAdmin());
    }
}
