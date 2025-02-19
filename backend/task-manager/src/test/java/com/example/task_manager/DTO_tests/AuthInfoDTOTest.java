package com.example.task_manager.DTO_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.example.task_manager.DTO.AuthInfoDTO;

public class AuthInfoDTOTest {
    @Test
    void testDTOConstructorAndGetters() {
        AuthInfoDTO authInfoDTO = new AuthInfoDTO(1, 1001);

        assertEquals(1, authInfoDTO.getAccountId());
        assertEquals(1001, authInfoDTO.getTeamMemberId());
    }

    @Test
    void testDTOSetters() {
        AuthInfoDTO authInfoDTO = new AuthInfoDTO(1, 1001);

        authInfoDTO.setAccountId(2);
        authInfoDTO.setTeamMemberId(2002);

        assertEquals(2, authInfoDTO.getAccountId());
        assertEquals(2002, authInfoDTO.getTeamMemberId());
    }
}
