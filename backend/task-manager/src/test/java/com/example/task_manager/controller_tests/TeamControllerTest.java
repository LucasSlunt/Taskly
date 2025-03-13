package com.example.task_manager.controller_tests;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import com.example.task_manager.DTO.TeamDTO;
import com.example.task_manager.DTO.TeamMemberDTO;
import com.example.task_manager.DTO.TeamRequestDTO;
import com.example.task_manager.controller.TeamController;
import com.example.task_manager.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest(TeamController.class)
public class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeamService teamService;

    @InjectMocks
    private TeamController teamController;

    @Autowired
    ObjectMapper objectMapper;

    private TeamDTO mockTeam;
    private TeamMemberDTO mockTeamMember;

    @BeforeEach
    void setUp() {
        mockTeam = new TeamDTO(1, "Development Team", 1);
        mockTeamMember = new TeamMemberDTO(1, "John Doe", "john.doe@example.com");
    }

    // Create a Team
    @Test
    void testCreateTeam() throws Exception {
        TeamRequestDTO requestDTO = new TeamRequestDTO(1, "Development Team", 1);
        TeamDTO mockTeam = new TeamDTO(1, "Development Team", 1);
        
        when(teamService.createTeam(anyString(), anyInt())).thenReturn(mockTeam);

        mockMvc.perform(post("/api/teams")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teamName").value("Development Team"));
    }

    // Delete Team
    @Test
    void testDeleteTeam() throws Exception {
        doNothing().when(teamService).deleteTeam(1);

        mockMvc.perform(delete("/api/teams/1"))
                .andExpect(status().isNoContent());
    }

    // Change Team Lead
    @Test
    void testChangeTeamLead() throws Exception {
        TeamRequestDTO requestDTO = new TeamRequestDTO(1, "Engineering Team", 2);
        TeamDTO mockResponse = new TeamDTO(1, "Engineering Team", 2);

        when(teamService.changeTeamLead(1, "Engineering Team", 2)).thenReturn(mockResponse);

        mockMvc.perform(put("/api/teams/1/change-lead")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.teamId").value(1))
            .andExpect(jsonPath("$.teamName").value("Engineering Team"))
            .andExpect(jsonPath("$.teamLeadId").value(2));
    }

    // Get Team Members
    @Test
    void testGetTeamMembers() throws Exception {
        List<TeamMemberDTO> teamMembers = Collections.singletonList(mockTeamMember);
        when(teamService.getTeamMembers(1)).thenReturn(teamMembers);

        mockMvc.perform(get("/api/teams/1/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userName").value("John Doe"));
    }
}
