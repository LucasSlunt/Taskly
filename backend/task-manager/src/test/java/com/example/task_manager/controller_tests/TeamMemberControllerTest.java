package com.example.task_manager.controller_tests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import com.example.task_manager.DTO.TaskDTO;
import com.example.task_manager.DTO.TaskRequestDTO;
import com.example.task_manager.DTO.TeamDTO;
import com.example.task_manager.DTO.IsAssignedDTO;
import com.example.task_manager.controller.TeamMemberController;
import com.example.task_manager.service.TeamMemberService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(TeamMemberController.class)
public class TeamMemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeamMemberService teamMemberService;

    @InjectMocks
    private TeamMemberController teamMemberController;

    @Autowired
    ObjectMapper objectMapper;

    private TaskDTO mockTask;

    @BeforeEach
    void setUp() {
        mockTask = new TaskDTO(1, "Task Title", "Description", false, "Open", LocalDate.now(), 1);
    }

    @Test
    void testCreateTask() throws Exception {
        // Given: Creating a mock request DTO
        TaskRequestDTO requestDTO = new TaskRequestDTO(
            "Task Title",
            "Description",
            false,
            "Open",
            LocalDate.of(2025, 3, 11),
            Arrays.asList(1, 2, 3),
            1
        );

        when(teamMemberService.createTask(any(TaskRequestDTO.class))).thenReturn(mockTask);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task Title"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.isLocked").value(false))
                .andExpect(jsonPath("$.status").value("Open"))
                .andExpect(jsonPath("$.teamId").value(1));
    }

    // Delete Task
    @Test
    void testDeleteTask() throws Exception {
        doNothing().when(teamMemberService).deleteTask(1);

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEditTask() throws Exception {
        TaskDTO requestDTO = new TaskDTO(
                1,
                "Updated Title",
                "Updated Description",
                false,
                "In Progress",
                LocalDate.now().plusDays(3),
                1
        );

        when(teamMemberService.editTask(eq(1), any(TaskDTO.class))).thenReturn(requestDTO);

        mockMvc.perform(put("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.isLocked").value(false))
                .andExpect(jsonPath("$.status").value("In Progress"))
                .andExpect(jsonPath("$.teamId").value(1));
    }

    // Assign Member to Task
    @Test
    void testAssignToTask() throws Exception {
        IsAssignedDTO assignedDTO = new IsAssignedDTO(1, 1, 1, 1);
        when(teamMemberService.assignToTask(1, 1)).thenReturn(assignedDTO);

        mockMvc.perform(post("/api/tasks/1/assign/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value(1));
    }

    // Change Password (Placeholder)
    @Test
    void testChangePassword() throws Exception {

    }
    
    @Test
    void testGetTeamsForMember() throws Exception {
        List<TeamDTO> mockTeams = Arrays.asList(
                new TeamDTO(1, "Team 1", 1),
                new TeamDTO(2, "Team 2", 1)
        );

        when(teamMemberService.getTeamsForMember(1)).thenReturn(mockTeams);

        MvcResult result = mockMvc.perform(get("/api/tasks/1/teams"))
            .andExpect(status().isOk())
            .andReturn();
        
        // System.out.println("Response: " + result.getResponse().getContentAsString());
    }
}
