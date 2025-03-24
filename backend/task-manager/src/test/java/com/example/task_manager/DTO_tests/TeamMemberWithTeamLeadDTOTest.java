package com.example.task_manager.DTO_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.example.task_manager.DTO.TeamMemberWithTeamLeadDTO;
import com.example.task_manager.enums.RoleType;

public class TeamMemberWithTeamLeadDTOTest {

    @Test
    void testDTOConstructorAndGetters() {
        TeamMemberWithTeamLeadDTO dto = new TeamMemberWithTeamLeadDTO(1, "Alice Johnson", "alice@example.com",
                RoleType.TEAM_MEMBER, false, null, null);

        assertEquals(1, dto.getAccountId());
        assertEquals("Alice Johnson", dto.getUserName());
        assertEquals("alice@example.com", dto.getUserEmail());
        assertEquals(false, dto.isIsTeamLead());
    }

    @Test
    void testDTOSetters() {
        TeamMemberWithTeamLeadDTO dto = new TeamMemberWithTeamLeadDTO(1, "Alice Johnson", "alice@example.com", RoleType.TEAM_MEMBER, false, null, null);

        dto.setAccountId(2);
        dto.setUserName("Updated Name");
        dto.setUserEmail("updated@example.com");
        dto.setIsTeamLead(true);

        assertEquals(2, dto.getAccountId());
        assertEquals("Updated Name", dto.getUserName());
        assertEquals("updated@example.com", dto.getUserEmail());
        assertEquals(true, dto.isIsTeamLead());
    }
}
