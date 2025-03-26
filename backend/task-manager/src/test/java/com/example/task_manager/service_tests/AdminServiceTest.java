package com.example.task_manager.service_tests;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import jakarta.transaction.Transactional;

import com.example.task_manager.TestHelper;
import com.example.task_manager.DTO.AdminDTO;
import com.example.task_manager.DTO.TaskDTO;
import com.example.task_manager.DTO.TaskRequestDTO;
import com.example.task_manager.DTO.TeamDTO;
import com.example.task_manager.DTO.TeamMemberDTO;
import com.example.task_manager.DTO.TeamMemberWithTeamLeadDTO;
import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.Team;
import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.enums.RoleType;
import com.example.task_manager.enums.TaskPriority;
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AdminServiceTest extends TestHelper{

    

    @Test
    void testCreateAdmin() {
        AdminDTO adminDTO = adminService.createAdmin("Admin" + System.nanoTime(),
                "admin" + System.nanoTime() + "@example.com", "password");
        assertNotNull(adminDTO);
        assertTrue(adminDTO.getUserEmail().contains("@example.com"));
    }

    @Test
    void testDeleteAdmin() {
        AdminDTO admin = adminService.createAdmin("AdminDelete" + System.nanoTime(),
                "delete_admin" + System.nanoTime() + "@example.com", "password");
        adminService.deleteAdmin(admin.getAccountId());
        assertFalse(adminRepository.findById(admin.getAccountId()).isPresent());
    }

    @Test
    void testModifyAdminName() {
        AdminDTO admin = adminService.createAdmin("AdminOriginal",
                "original_admin" + System.nanoTime() + "@example.com", "password");
        AdminDTO updatedAdminDTO = adminService.modifyAdminName(admin.getAccountId(), "UpdatedAdmin");
        assertEquals("UpdatedAdmin", updatedAdminDTO.getUserName());
    }

    @Test
    void testModifyAdminEmail() {
        AdminDTO admin = adminService.createAdmin("AdminEmailChange",
                "email_admin" + System.nanoTime() + "@example.com", "password");
        String newEmail = "updated" + System.nanoTime() + "@example.com";
        AdminDTO updatedAdmin = adminService.modifyAdminEmail(admin.getAccountId(), newEmail);
        assertEquals(newEmail, updatedAdmin.getUserEmail());
    }

    @Test
    void testCreateTeamMember() {
        TeamMemberDTO teamMemberDTO = adminService.createTeamMember("Member" + System.nanoTime(),
                "member" + System.nanoTime() + "@example.com", "password");
        assertNotNull(teamMemberDTO);
        assertTrue(teamMemberDTO.getUserEmail().contains("@example.com"));
    }

    @Test
    void testDeleteTeamMember() {
        TeamMemberDTO teamMember = adminService.createTeamMember("ToDelete" + System.nanoTime(),
                "delete_member" + System.nanoTime() + "@example.com", "password");
        adminService.deleteTeamMember(teamMember.getAccountId());
        assertFalse(teamMemberRepository.findById(teamMember.getAccountId()).isPresent());
    }

    @Test
    void testModifyTeamMemberName() {
        TeamMemberDTO teamMember = adminService.createTeamMember("OldName",
                "name_member" + System.nanoTime() + "@example.com", "password");
        TeamMemberDTO updatedTeamMember = adminService.modifyTeamMemberName(teamMember.getAccountId(), "NewName");
        assertEquals("NewName", updatedTeamMember.getUserName());
    }

    @Test
    void testChangeRoleToTeamMember() {
        AdminDTO admin = adminService.createAdmin("OssyOsbourne" + System.nanoTime(),
                "NoMoreTears" + System.nanoTime() + "@rock.com", "music_password");

        TeamMemberDTO result = (TeamMemberDTO) adminService.changeRole(admin.getAccountId(), RoleType.TEAM_MEMBER);

        assertEquals(RoleType.TEAM_MEMBER, result.getRole());
        assertNotEquals(admin.getAccountId(), result.getAccountId());
    }

    @Test
    void testChangeRoleToAdmin() {
        TeamMemberDTO teamMember = adminService.createTeamMember("Whitesnake" + System.nanoTime(),
                "HereIGoAgain" + System.nanoTime() + "@rock.com", "on_my_own");

        AdminDTO result = (AdminDTO) adminService.changeRole(teamMember.getAccountId(), RoleType.ADMIN);

        assertEquals(RoleType.ADMIN, result.getRole());
        assertNotEquals(teamMember.getAccountId(), result.getAccountId());
    }
    
    @Test
    void testAssignToTeam() {
        TeamMemberDTO teamMember = adminService.createTeamMember("AssignedMember",
                "assigned_member" + System.nanoTime() + "@example.com", "password");
        TeamMemberDTO teamLead = adminService.createTeamMember("TeamLead", "lead" + System.nanoTime() + "@example.com",
                "password");

        TeamDTO team = teamService.createTeam("Team-" + System.nanoTime(), teamLead.getAccountId());

        adminService.assignToTeam(teamMember.getAccountId(), team.getTeamId());

        TeamMember updatedTeamMember = teamMemberRepository.findById(teamMember.getAccountId()).orElseThrow();
        assertTrue(updatedTeamMember.getTeams().stream()
                .anyMatch(isMember -> isMember.getTeam().getTeamId() == team.getTeamId()));
    }

    @Test
    void testLockTask() {
        Team team = teamRepository.save(new Team("LockTeam-" + System.nanoTime(), null));
        TaskRequestDTO taskRequest = new TaskRequestDTO("TaskToLock", "Lock Task Desc", false, "Open", LocalDate.now(), null, team.getTeamId(), TaskPriority.MEDIUM);
        TaskDTO task = adminService.createTask(taskRequest);

        adminService.lockTask(task.getTaskId());
        Task updatedTask = taskRepository.findById(task.getTaskId()).orElseThrow();
        assertTrue(updatedTask.isLocked());
    }

    @Test
    void testUnlockTask() {
        Team team = teamRepository.save(new Team("UnlockTeam-" + System.nanoTime(), null));
        TaskRequestDTO taskRequest = new TaskRequestDTO("TaskToUnlock", "Unlock Task Desc", true, "Open", LocalDate.now(), null, team.getTeamId(), TaskPriority.MEDIUM);
        TaskDTO task = adminService.createTask(taskRequest);

        adminService.unlockTask(task.getTaskId());
        Task updatedTask = taskRepository.findById(task.getTaskId()).orElseThrow();
        assertFalse(updatedTask.isLocked());
    }

    @Test
    void testModifyNonExistentAdmin() {
        Exception exception = assertThrows(RuntimeException.class,
                () -> adminService.modifyAdminName(9999, "New Name"));
        assertNotNull(exception);
    }

    @Test
    void testAssignToNonexistentTeam() {
        TeamMemberDTO teamMember = adminService.createTeamMember("NoTeamMember", "noteam@example.com", "password");
        Exception exception = assertThrows(RuntimeException.class,
                () -> adminService.assignToTeam(teamMember.getAccountId(), 9999));
        assertNotNull(exception);
    }

    @Test
    void testLockNonexistentTask() {
        Exception exception = assertThrows(RuntimeException.class,
                () -> adminService.lockTask(9999));
        assertNotNull(exception);
    }

    @Test
    void testGetAllTeamMembers() {
        TeamMemberDTO teamMember = adminService.createTeamMember("Led Zeppelin", "ramble_on@music.com", "led_zeppelin");
        TeamDTO team = teamService.createTeam("Testing Zeppelin Team", teamMember.getAccountId());

        List<TeamMemberWithTeamLeadDTO> results = adminService.getAllTeamMembers();

        TeamMemberWithTeamLeadDTO result = results.stream()
                .filter(dto -> dto.getAccountId() == teamMember.getAccountId())
                .findFirst()
                .orElseThrow();

        assertEquals(teamMember.getAccountId(), result.getAccountId());
        assertTrue(result.isIsTeamLead());
        assertEquals(team.getTeamId(), result.getTeamLeadOfId().get(0));
        assertEquals(team.getTeamName(), result.getTeamLeadOfName().get(0));
    }
}
