package com.example.task_manager.DTO_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.example.task_manager.DTO.TeamRequestDTO;

public class TeamRequestDTOTest {
    @Test
    void testSettersAndGetters() {
        TeamRequestDTO teamRequestDTO = new TeamRequestDTO();

        teamRequestDTO.setTeamId(2);
        teamRequestDTO.setTeamName("Marketing Team");
        teamRequestDTO.setTeamLeadId(200);

        assertEquals(2, teamRequestDTO.getTeamId());
        assertEquals("Marketing Team", teamRequestDTO.getTeamName());
        assertEquals(200, teamRequestDTO.getTeamLeadId());
    }
}
