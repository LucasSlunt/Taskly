package com.example.task_manager.DTO_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import com.example.task_manager.DTO.AuthInfoDTO;
import com.example.task_manager.enums.RoleType;

public class AuthInfoDTOTest {
    @Test
    void testDTOConstructorAndGetters() {
        AuthInfoDTO authInfoDTO = new AuthInfoDTO(1, "User Name", RoleType.ADMIN);

        assertEquals(1, authInfoDTO.getAccountId());
        assertEquals("User Name", authInfoDTO.getUserName());
        assertNotEquals(authInfoDTO.getRole(), RoleType.TEAM_MEMBER);
    }

    @Test
    void testDTOSetters() {
        AuthInfoDTO authInfoDTO = new AuthInfoDTO(1, "User Name", RoleType.ADMIN);

        authInfoDTO.setAccountId(2);
        authInfoDTO.setUserName("Updated Name");
        authInfoDTO.setRole(RoleType.ADMIN);

        assertEquals("Updated Name", authInfoDTO.getUserName());
        assertEquals(authInfoDTO.getRole(), RoleType.ADMIN);
    }
}
