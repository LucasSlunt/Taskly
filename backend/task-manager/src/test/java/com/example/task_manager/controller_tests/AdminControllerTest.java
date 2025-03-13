package com.example.task_manager.controller_tests;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.example.task_manager.DTO.AdminDTO;
import com.example.task_manager.DTO.AdminRequestDTO;
import com.example.task_manager.DTO.TeamMemberDTO;
import com.example.task_manager.controller.AdminController;
import com.example.task_manager.entity.Admin;
import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.repository.AdminRepository;
import com.example.task_manager.service.AdminService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.task_manager.DTO.TeamDTO;
import com.example.task_manager.DTO.UpdateEmailRequestDTO;
import com.example.task_manager.DTO.UpdateNameRequestDTO;
import com.example.task_manager.repository.TeamMemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @MockBean
    private AdminRepository adminRepository;

    @MockBean
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private ObjectMapper objectMapper; //used top convert DTO's to json

    private AdminDTO mockAdmin;
    private TeamMemberDTO mockMember;
    private String mockAdminPassword;
    private String mockTMPassword;

    @BeforeEach
    void setUp() {
        mockAdmin = new AdminDTO(1, "Admin User", "admin@example.com");
        mockMember = new TeamMemberDTO(2, "John Doe", "john.doe@example.com");
        mockAdminPassword = "mock_password_admin";
        mockTMPassword = "mock_password_TM";
    }

    // Create an Admin
    @Test
    void testCreateAdmin() throws Exception {
        AdminRequestDTO requestDTO = new AdminRequestDTO("Admin User", "admin@example.com", mockAdminPassword);

        when(adminService.createAdmin("Admin User", "admin@example.com",mockAdminPassword)).thenReturn(mockAdmin);

        mockMvc.perform(post("/api/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(1))
                .andExpect(jsonPath("$.userName").value("Admin User"))
                .andExpect(jsonPath("$.userEmail").value("admin@example.com"));
    }

    // Delete Admin
    @Test
    void testDeleteAdmin() throws Exception {
        doNothing().when(adminService).deleteAdmin(1);

        mockMvc.perform(delete("/api/admin/1"))
                .andExpect(status().isNoContent());
    }

    // Modify Admin Name
    @Test
    void testModifyAdminName() throws Exception {
        UpdateNameRequestDTO requestDTO = new UpdateNameRequestDTO("New Admin Name");

        when(adminService.modifyAdminName(1, "New Admin Name")).thenReturn(new AdminDTO(1, "New Admin Name", "admin@example.com"));

        mockMvc.perform(put("/api/admin/1/update-name")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("New Admin Name"));
    }

    // Modify Admin Email
    @Test
    void testModifyAdminEmail() throws Exception {
        UpdateEmailRequestDTO requestDTO = new UpdateEmailRequestDTO("new_email@example.com");

        when(adminService.modifyAdminEmail(1, "new_email@example.com"))
                .thenReturn(new AdminDTO(1, "Admin User", "new_email@example.com"));

        mockMvc.perform(put("/api/admin/1/update-email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userEmail").value("new_email@example.com"));
    }

    // Create Team Member
    @Test
    void testCreateTeamMember() throws Exception {
        AdminRequestDTO requestDTO = new AdminRequestDTO("John Doe", "john.doe@example.com", mockTMPassword);

        when(adminService.createTeamMember("John Doe", "john.doe@example.com", mockTMPassword)).thenReturn(mockMember);

        mockMvc.perform(post("/api/admin/team-member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(2))
                .andExpect(jsonPath("$.userName").value("John Doe"))
                .andExpect(jsonPath("$.userEmail").value("john.doe@example.com"));
    }

    // Modify Team Member Name 
    @Test
    void testModifyTeamMemberName() throws Exception {
        UpdateNameRequestDTO requestDTO = new UpdateNameRequestDTO("TeamMember User");

        when(adminService.modifyTeamMemberName(1, "TeamMember User"))
                .thenReturn(new TeamMemberDTO(1, "TeamMember User", "TM@example.com"));

        mockMvc.perform(put("/api/admin/team-member/1/update-name")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("TeamMember User"));
    }

    // Modify Team Member Email 
    @Test
    void testModifyTeamMemberEmail() throws Exception {
        UpdateEmailRequestDTO requestDTO = new UpdateEmailRequestDTO("new_email@example.com");

        when(adminService.modifyTeamMemberEmail(1, "new_email@example.com"))
                .thenReturn(new TeamMemberDTO(1, "John Doe", "new_email@example.com"));

        mockMvc.perform(put("/api/admin/team-member/1/update-email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userEmail").value("new_email@example.com"));
    }

    // Modify Team Member Email (Invalid Format) 
    @Test
    void testModifyTeamMemberEmailInvalidFormat() throws Exception {
        UpdateEmailRequestDTO requestDTO = new UpdateEmailRequestDTO("invalid-email");

        when(adminService.modifyTeamMemberEmail(1, "invalid-email"))
                .thenThrow(new RuntimeException("Invalid email format"));

        mockMvc.perform(put("/api/admin/team-member/1/update-email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid email format"));
    }
    
    // Delete Team Member 
    @Test
    void testDeleteTeamMember() throws Exception {
        doNothing().when(adminService).deleteTeamMember(1);

        mockMvc.perform(delete("/api/admin/team-member/1"))
                .andExpect(status().isNoContent());
    }

    // Delete Team Member (Not Found) 
    @Test
    void testDeleteTeamMember_NotFound() throws Exception {
        doThrow(new RuntimeException("Team member not found")).when(adminService).deleteTeamMember(999);

        mockMvc.perform(delete("/api/admin/team-member/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Team member not found"));
    }

    // Promote to Admin
    @Test
    void testPromoteToAdmin() throws Exception {
        when(adminService.promoteToAdmin(2)).thenReturn(new AdminDTO(2, "John Doe", "john.doe@example.com"));

        mockMvc.perform(post("/api/admin/team-member/2/promote"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(2))
                .andExpect(jsonPath("$.userName").value("John Doe"));
    }

    // Assign to Team
    @Test
    void testAssignToTeam() throws Exception {
        TeamMemberDTO mockTeamMember = new TeamMemberDTO(2, "John Doe", "john.doe@example.com"); // Mock return value
        
        when(adminService.assignToTeam(2, 1)).thenReturn(mockTeamMember);

        mockMvc.perform(post("/api/admin/team-member/2/assign-to-team/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(2))
                .andExpect(jsonPath("$.userName").value("John Doe"))
                .andExpect(jsonPath("$.userEmail").value("john.doe@example.com"));
    }

    // Lock Task
    @Test
    void testLockTask() throws Exception {
        doNothing().when(adminService).lockTask(1);

        mockMvc.perform(put("/api/admin/tasks/1/lock"))
                .andExpect(status().isOk());
    }

    // Unlock Task
    @Test
    void testUnlockTask() throws Exception {
            doNothing().when(adminService).unlockTask(1);

            mockMvc.perform(put("/api/admin/tasks/1/unlock"))
                            .andExpect(status().isOk());
    }
    
    // Getting all admins
        @Test
        void testGetAllAdmins() throws Exception {
                List<AdminDTO> mockAdmins = Arrays.asList(
                                new AdminDTO(1, "Alice Johnson", "alice@example.com"),
                                new AdminDTO(2, "Bob Smith", "bob@example.com"));

                when(adminService.getAllAdmins()).thenReturn(mockAdmins);

                mockMvc.perform(get("/api/admin/admins"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.size()").value(2))
                                .andExpect(jsonPath("$[0].accountId").value(1))
                                .andExpect(jsonPath("$[0].userName").value("Alice Johnson"))
                                .andExpect(jsonPath("$[1].userEmail").value("bob@example.com"));
        }
        
        // Getting all admins
        @Test
        void getAllTeamMembers() throws Exception {
                List<TeamMemberDTO> mockTMs = Arrays.asList(
                                new TeamMemberDTO(1, "Alice Johnson", "alice@example.com"),
                                new TeamMemberDTO(2, "Bob Smith", "bob@example.com"));

                when(adminService.getAllTeamMembers()).thenReturn(mockTMs);

                mockMvc.perform(get("/api/admin/team-members"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.size()").value(2))
                                .andExpect(jsonPath("$[0].accountId").value(1))
                                .andExpect(jsonPath("$[0].userName").value("Alice Johnson"))
                                .andExpect(jsonPath("$[1].userEmail").value("bob@example.com"));
        }
        
        // Getting all teams
        @Test
        void getAllTeams() throws Exception {
                List<TeamDTO> mockTeams = Arrays.asList(
                                new TeamDTO(1, "Team 1", 1),
                                new TeamDTO(2, "Team 2", 2));

                when(adminService.getAllTeams()).thenReturn(mockTeams);

                mockMvc.perform(get("/api/admin/all-teams"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.size()").value(2))
                                .andExpect(jsonPath("$[0].teamId").value(1))
                                .andExpect(jsonPath("$[0].teamName").value("Team 1"))
                                .andExpect(jsonPath("$[1].teamLeadId").value("2"));
        }
        
        @Test
        void testGetAdminById() throws Exception {
            when(adminService.getAdminById(1)).thenReturn(mockAdmin);
    
            mockMvc.perform(get("/api/admin/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accountId").value(1))
                    .andExpect(jsonPath("$.userName").value("Admin User"))
                    .andExpect(jsonPath("$.userEmail").value("admin@example.com"));
        }
    
        @Test
        void testGetAdminById_NotFound() throws Exception {
            when(adminService.getAdminById(999)).thenThrow(new RuntimeException("Admin not found"));
    
            mockMvc.perform(get("/api/admin/999"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Admin not found"));
        }    
}
