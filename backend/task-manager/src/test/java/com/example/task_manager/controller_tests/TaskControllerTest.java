package com.example.task_manager.controller_tests;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.example.task_manager.controller.TaskController;
import com.example.task_manager.service.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    /**
     * Test notifying members (Success Case)
     */
    @Test
    void testNotifyMembers_Success() throws Exception {
        int taskId = (int) System.nanoTime();
        String message = "Reminder: Task deadline is approaching. " + taskId;

        doNothing().when(taskService).notifyMembers(taskId, message);

        mockMvc.perform(post("/api/tasks/" + taskId + "/notify")
                .param("message", message))
                .andExpect(status().isOk());

        verify(taskService, times(1)).notifyMembers(taskId, message);
    }

    /**
     * Test notifying members when Task is not found
     */
    @Test
    void testNotifyMembers_TaskNotFound() throws Exception {
        int taskId = (int) System.nanoTime();
        String message = "Reminder: Task deadline is approaching. " + taskId;

        doThrow(new RuntimeException("Task not found with ID: " + taskId))
                .when(taskService).notifyMembers(taskId, message);

        mockMvc.perform(post("/api/tasks/" + taskId + "/notify")
                .param("message", message))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Task not found with ID: " + taskId));
    }
}
