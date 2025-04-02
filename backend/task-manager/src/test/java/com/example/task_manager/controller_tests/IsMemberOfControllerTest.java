package com.example.task_manager.controller_tests;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.example.task_manager.DTO.IsMemberOfDTO;
import com.example.task_manager.controller.IsMemberOfController;
import com.example.task_manager.service.IsMemberOfService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(IsMemberOfController.class)
@ActiveProfiles("test")
public class IsMemberOfControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IsMemberOfService isMemberOfService;

    /**
     * Test Add Member to a Team
     */
    @Test
    void testAddMemberToTeam() throws Exception {
        int uniqueId = (int) System.nanoTime();
        int teamMemberId = uniqueId + 1;
        int teamId = uniqueId + 2;
        IsMemberOfDTO mockMembership = new IsMemberOfDTO(uniqueId, teamMemberId, teamId);

        when(isMemberOfService.addMemberToTeam(teamMemberId, teamId)).thenReturn(mockMembership);

        mockMvc.perform(post("/api/memberships/" + teamMemberId + "/team/" + teamId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isMemberOfId").value(uniqueId))
                .andExpect(jsonPath("$.teamMemberId").value(teamMemberId))
                .andExpect(jsonPath("$.teamId").value(teamId));
    }

    /*
     * Test adding multiple members to a team at once
     */
    @Test
    void testMassAssignToTeam() throws Exception {
        int teamId = 100;
        List<Integer> teamMemberIds = List.of(101, 102, 103);

        List<IsMemberOfDTO> mockMemberships = List.of(
                new IsMemberOfDTO(1, 101, teamId),
                new IsMemberOfDTO(2, 102, teamId),
                new IsMemberOfDTO(3, 103, teamId));

        when(isMemberOfService.massAssignToTeam(eq(teamId), eq(teamMemberIds))).thenReturn(mockMemberships);

        String jsonRequest = "[101, 102, 103]";

        mockMvc.perform(post("/api/memberships/team/" + teamId + "/mass-assign")
                .contentType("application/json")
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].teamMemberId").value(101))
                .andExpect(jsonPath("$[1].teamMemberId").value(102))
                .andExpect(jsonPath("$[2].teamMemberId").value(103));
    }
    
    @Test
    void testMassAssignToTeam_TeamNotFound() throws Exception {
        int teamId = 999;
        List<Integer> teamMemberIds = List.of(101, 102, 103);

        String jsonRequest = "[101, 102, 103]";

        when(isMemberOfService.massAssignToTeam(eq(teamId), eq(teamMemberIds)))
            .thenThrow(new RuntimeException("Team not found"));

        mockMvc.perform(post("/api/memberships/team/" + teamId + "/mass-assign")
                .contentType("application/json")
                .content(jsonRequest))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Team not found"));
    }

    /**
     * Test Remove Member from Team
     */
    @Test
    void testRemoveMemberFromTeam() throws Exception {
        int uniqueId = (int) System.nanoTime();
        int teamMemberId = uniqueId + 1;
        int teamId = uniqueId + 2;
        IsMemberOfDTO mockMembership = new IsMemberOfDTO(uniqueId, teamMemberId, teamId);

        when(isMemberOfService.removeMemberFromTeam(teamMemberId, teamId)).thenReturn(mockMembership);

        mockMvc.perform(delete("/api/memberships/" + teamMemberId + "/team/" + teamId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isMemberOfId").value(uniqueId));
    }

    /**
     * Test Check if Member of Team
     */
    @Test
    void testIsMemberOfTeam() throws Exception {
        int teamMemberId = (int) System.nanoTime();
        int teamId = teamMemberId + 1;

        when(isMemberOfService.isMemberOfTeam(teamMemberId, teamId)).thenReturn(true);

        mockMvc.perform(get("/api/memberships/" + teamMemberId + "/team/" + teamId))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}