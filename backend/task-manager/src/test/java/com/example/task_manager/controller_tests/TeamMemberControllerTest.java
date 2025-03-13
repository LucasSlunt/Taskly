package com.example.task_manager.controller_tests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import com.example.task_manager.DTO.TaskDTO;
import com.example.task_manager.DTO.TeamDTO;
import com.example.task_manager.DTO.IsAssignedDTO;
import com.example.task_manager.controller.TeamMemberController;
import com.example.task_manager.service.TeamMemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    private TaskDTO mockTask;

    @BeforeEach
    void setUp() {
        mockTask = new TaskDTO(1, "Task Title", "Description", false, "Open", LocalDate.now(), 1);
    }

    // Create a Task
    @Test
    void testCreateTask() throws Exception {
        when(teamMemberService.createTask(anyString(), anyString(), anyBoolean(), anyString(),
                any(LocalDate.class), any(LocalDate.class), any(), any()))
                .thenReturn(mockTask);

        mockMvc.perform(post("/api/tasks")
                .param("title", "Task Title")
                .param("description", "Description")
                .param("isLocked", "false")
                .param("status", "Open")
                .param("teamId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task Title"));
    }

    // Delete Task
    @Test
    void testDeleteTask() throws Exception {
        doNothing().when(teamMemberService).deleteTask(1);

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());
    }

    // Edit Task
    @Test
    void testEditTask() throws Exception {
        TaskDTO mockTask = new TaskDTO(1, "Updated Title", "Updated Description", false, "In Progress", LocalDate.now().plusDays(3), 1);

        when(teamMemberService.editTask(anyInt(), anyString(), anyString(), anyBoolean(),
                anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(mockTask);

        mockMvc.perform(put("/api/tasks/1")
                .param("title", "Updated Title")
                .param("description", "Updated Description")
                .param("status", "In Progress"));
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
                new TeamDTO(2, "Team 2", 1));

        when(teamMemberService.getTeamsForMember(1)).thenReturn(mockTeams);

        MvcResult result = mockMvc.perform(get("/api/tasks/1/teams"))
                .andExpect(status().isOk())
                .andReturn();

        // System.out.println("Response: " + result.getResponse().getContentAsString());
    }
    
    @Test
    void testGetAssignedTasks() throws Exception {
        List<TaskDTO> mockTasks = Arrays.asList(
                new TaskDTO(1, "Task Title 1", "Task 1 description", false, "Open", LocalDate.now(), 1),
                new TaskDTO(2, "Task Title 2", "Task 2 description", true, "Closed", LocalDate.now(), 1));

        when(teamMemberService.getAssignedTasks(1)).thenReturn(mockTasks);

        MvcResult result = mockMvc.perform(get("/api/tasks/1/tasks"))
                .andExpect(status().isOk())
                .andReturn();

    }
}
