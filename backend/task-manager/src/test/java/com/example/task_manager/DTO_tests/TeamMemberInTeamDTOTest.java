package com.example.task_manager.DTO_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.example.task_manager.DTO.TeamMemberInTeamDTO;
import com.example.task_manager.enums.RoleType;

public class TeamMemberInTeamDTOTest {
    @Test
    void testDTOConstructorAndGetters() {
        TeamMemberInTeamDTO dto = new TeamMemberInTeamDTO(1, "Alice Johnson", "alice@example.com", RoleType.TEAM_MEMBER,
                true);

        assertEquals(1, dto.getAccountId());
        assertEquals("Alice Johnson", dto.getUserName());
        assertEquals("alice@example.com", dto.getUserEmail());
        assertTrue(dto.isIsTeamLead());
    }
    
    @Test
    void testDTOSetters() {
        TeamMemberInTeamDTO dto = new TeamMemberInTeamDTO(1, "Alice Johnson", "alice@example.com", RoleType.TEAM_MEMBER, false);

        dto.setAccountId(2);
        dto.setUserName("Updated Name");
        dto.setUserEmail("updated@example.com");
        dto.setIsTeamLead(true);

        assertEquals(2, dto.getAccountId());
        assertEquals("Updated Name", dto.getUserName());
        assertEquals("updated@example.com", dto.getUserEmail());
        assertTrue(dto.isIsTeamLead());
    }
}
