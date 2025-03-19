package com.example.task_manager.DTO_tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.task_manager.DTO.TeamMemberDTO;
import com.example.task_manager.enums.RoleType;

public class TeamMemberDTOTest {

    @Test
    void testDTOConstructorAndGetters() {
        TeamMemberDTO dto = new TeamMemberDTO(1, "Alice Johnson", "alice@example.com", RoleType.TEAM_MEMBER);

        assertEquals(1, dto.getAccountId());
        assertEquals("Alice Johnson", dto.getUserName());
        assertEquals("alice@example.com", dto.getUserEmail());
    }

    @Test
    void testDTOSetters() {
        TeamMemberDTO dto = new TeamMemberDTO(1, "Alice Johnson", "alice@example.com", RoleType.TEAM_MEMBER);

        dto.setAccountId(2);
        dto.setUserName("Updated Name");
        dto.setUserEmail("updated@example.com");

        assertEquals(2, dto.getAccountId());
        assertEquals("Updated Name", dto.getUserName());
        assertEquals("updated@example.com", dto.getUserEmail());
    }
}
