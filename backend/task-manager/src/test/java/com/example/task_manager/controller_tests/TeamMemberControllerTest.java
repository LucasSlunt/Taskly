package com.example.task_manager.controller_tests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.example.task_manager.DTO.TaskDTO;
import com.example.task_manager.DTO.TaskRequestDTO;
import com.example.task_manager.DTO.IsAssignedDTO;
import com.example.task_manager.controller.TeamMemberController;
import com.example.task_manager.service.TeamMemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

@WebMvcTest(TeamMemberController.class)
public class TeamMemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeamMemberService teamMemberService;

    @InjectMocks
    private TeamMemberController teamMemberController;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test Create Task
     */
    @Test
    void testCreateTask() throws Exception {
        int uniqueId = (int) System.nanoTime();
        int teamId = uniqueId + 1;
        TaskDTO mockTask = new TaskDTO(uniqueId, "Task Title " + uniqueId, "Description", false, "Open", LocalDate.now(), teamId);

        TaskRequestDTO requestDTO = new TaskRequestDTO(
                "Task Title " + uniqueId,
                "Description",
                false,
                "Open",
                LocalDate.of(2025, 3, 11),
                Arrays.asList(1, 2, 3),
                teamId
        );

        when(teamMemberService.createTask(any(TaskRequestDTO.class))).thenReturn(mockTask);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task Title " + uniqueId))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.isLocked").value(false))
                .andExpect(jsonPath("$.status").value("Open"))
                .andExpect(jsonPath("$.teamId").value(teamId));
    }

    /**
     * Test Delete Task
     */
    @Test
    void testDeleteTask() throws Exception {
        int uniqueId = (int) System.nanoTime();
        doNothing().when(teamMemberService).deleteTask(uniqueId);

        mockMvc.perform(delete("/api/tasks/" + uniqueId))
                .andExpect(status().isNoContent());
    }

    /**
     * Test Edit Task
     */
    @Test
    void testEditTask() throws Exception {
        int uniqueId = (int) System.nanoTime();
        int teamId = uniqueId + 1;

        TaskDTO requestDTO = new TaskDTO(
                uniqueId,
                "Updated Title " + uniqueId,
                "Updated Description",
                false,
                "In Progress",
                LocalDate.now().plusDays(3),
                teamId
        );

        when(teamMemberService.editTask(eq(uniqueId), any(TaskDTO.class))).thenReturn(requestDTO);

        mockMvc.perform(put("/api/tasks/" + uniqueId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title " + uniqueId))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.isLocked").value(false))
                .andExpect(jsonPath("$.status").value("In Progress"))
                .andExpect(jsonPath("$.teamId").value(teamId));
    }

    /**
     * Test Assign Member to Task
     */
    @Test
    void testAssignToTask() throws Exception {
        int uniqueId = (int) System.nanoTime();
        int taskId = uniqueId + 1;
        int teamMemberId = uniqueId + 2;

        IsAssignedDTO assignedDTO = new IsAssignedDTO(uniqueId, taskId, teamMemberId, uniqueId);
        when(teamMemberService.assignToTask(taskId, teamMemberId)).thenReturn(assignedDTO);

        mockMvc.perform(post("/api/tasks/" + taskId + "/assign/" + teamMemberId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value(taskId));
    }

    /**
     * Placeholder: Change Password
     */
    @Test
    void testChangePassword() throws Exception {
        // TODO: Implement Change Password Test
    }
}
