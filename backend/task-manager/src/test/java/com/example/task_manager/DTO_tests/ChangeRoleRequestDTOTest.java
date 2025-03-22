package com.example.task_manager.DTO_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.example.task_manager.DTO.ChangeRoleRequestDTO;
import com.example.task_manager.enums.RoleType;

public class ChangeRoleRequestDTOTest {
    @Test
    void testNoArgsConstructorAndSetters() {
        ChangeRoleRequestDTO dto = new ChangeRoleRequestDTO();
        dto.setRole(RoleType.ADMIN);

        assertEquals(RoleType.ADMIN, dto.getRole());
    }
    
    @Test
    void testSetAndGetRole() {
        ChangeRoleRequestDTO dto = new ChangeRoleRequestDTO();
        dto.setRole(RoleType.TEAM_MEMBER);

        RoleType role = dto.getRole();
        assertNotNull(role);
        assertEquals(RoleType.TEAM_MEMBER, role);
    }
}
