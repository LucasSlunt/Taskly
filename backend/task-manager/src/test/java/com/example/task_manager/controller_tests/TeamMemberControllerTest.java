package com.example.task_manager.controller_tests;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.example.task_manager.DTO.TaskDTO;
import com.example.task_manager.DTO.TeamDTO;
import com.example.task_manager.DTO.IsAssignedDTO;
import com.example.task_manager.controller.TeamMemberController;
import com.example.task_manager.service.TeamMemberService;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.example.task_manager.enums.TaskPriority;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(TeamMemberController.class)
@ActiveProfiles("test")
public class TeamMemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TeamMemberService teamMemberService;

    @InjectMocks
    private TeamMemberController teamMemberController;

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

        mockMvc.perform(post("/api/members/actions/" + taskId + "/assign/" + teamMemberId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value(taskId));
    }

    /*
     * Test mass assign members to task
     */
//     @Test
//     void testMassAssignToTask() throws Exception {
//         int uniqueId = 1;
//         int taskId = 2;

//         List<Integer> teamMemberIds = List.of(4, 5, 6);

//         List<IsAssignedDTO> mockAssignments = List.of(
//             new IsAssignedDTO(uniqueId, taskId, 4, uniqueId),
//             new IsAssignedDTO(uniqueId, taskId, 5, uniqueId),
//             new IsAssignedDTO(uniqueId, taskId, 6, uniqueId)
//         );
//         when(teamMemberService.massAssignToTask(taskId, teamMemberIds)).thenReturn(mockAssignments);

//         mockMvc.perform(post("/api/tasks/2/mass-assign"))
//             .andDo(print())
//             .andExpect(jsonPath("$.taskId").value(taskId))
//             .andExpect(status().isOk());
//     }

     @Test
    void testGetTeamsForMember() throws Exception {
        List<TeamDTO> mockTeams = Arrays.asList(
                new TeamDTO(1, "Team 1", 1),
                new TeamDTO(2, "Team 2", 1));

        when(teamMemberService.getTeamsForMember(1)).thenReturn(mockTeams);
        mockMvc.perform(get("/api/members/actions/1/teams"))
        .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void testGetAssignedTasks() throws Exception {
        List<TaskDTO> mockTasks = Arrays.asList(
                new TaskDTO(1, "Task Title 1", "Task 1 description", false, "Open", LocalDate.now(), null, 1, null, TaskPriority.MEDIUM),
                new TaskDTO(2, "Task Title 2", "Task 2 description", true, "Closed", LocalDate.now(), null, 1, null, TaskPriority.MEDIUM));

        when(teamMemberService.getAssignedTasks(1)).thenReturn(mockTasks);
        mockMvc.perform(get("/api/members/actions/1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].priority").value("MEDIUM"))
                .andExpect(jsonPath("$[1].priority").value("MEDIUM"))
                .andReturn();

    }
}