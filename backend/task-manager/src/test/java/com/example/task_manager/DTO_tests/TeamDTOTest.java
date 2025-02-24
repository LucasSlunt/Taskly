package com.example.task_manager.DTO_tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.task_manager.DTO.TeamDTO;

public class TeamDTOTest {

    @Test
    void testDTOConstructorAndGetters() {
        TeamDTO dto = new TeamDTO(1, "Engineering Team", 1001);

        assertEquals(1, dto.getTeamId());
        assertEquals("Engineering Team", dto.getTeamName());
        assertEquals(1001, dto.getTeamLeadId());
    }

    @Test
    void testDTOSetters() {
        TeamDTO dto = new TeamDTO();
        
        dto.setTeamId(2);
        dto.setTeamName("Updated Team");
        dto.setTeamLeadId(2002);

        assertEquals(2, dto.getTeamId());
        assertEquals("Updated Team", dto.getTeamName());
        assertEquals(2002, dto.getTeamLeadId());
    }
}
