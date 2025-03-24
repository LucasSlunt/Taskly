package com.example.task_manager.service_tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import jakarta.transaction.Transactional;

import com.example.task_manager.DTO.AuthInfoDTO;
import com.example.task_manager.entity.Admin;
import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.repository.AdminRepository;
import com.example.task_manager.repository.AuthInfoRepository;
import com.example.task_manager.repository.TeamMemberRepository;
import com.example.task_manager.service.AuthInfoService;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@ActiveProfiles("test")
public class AuthInfoServiceTest {

    @Autowired
    private AuthInfoService authInfoService;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private AuthInfoRepository authInfoRepository;

    private TeamMember createUniqueTeamMember() {
        return teamMemberRepository.save(new TeamMember(
            "TeamMember_" + System.nanoTime(),
            "team_member" + System.nanoTime() + "@secure.com",
            "defaultpw"
        ));
    }

    private Admin createUniqueAdmin() {
        return adminRepository.save(new Admin(
            "Admin_" + System.nanoTime(),
            "admin_" + System.nanoTime() + "@secure.com",
            "adminpw"
        ));
    }

    @Test
    void testHashPassword() {
        String expectedHash = "B0F900221C7BA3474D782D4FE34FC165CE98414F66F2AC74DAEAA08A1D80B5C6";
        String generatedHash = AuthInfoService.hashPassword("awesomeSecurePassword123", "sixteenByteSalt!");
        assertEquals(expectedHash, generatedHash);
    }

    @Test
    void testGenerateSalt() {
        String salt = AuthInfoService.generateSalt();
        assertEquals(16, salt.length());
    }

    @Test
    void testSaltsAreUnique() {
        String salt1 = AuthInfoService.generateSalt();
        String salt2 = AuthInfoService.generateSalt();
        assertNotEquals(salt1, salt2);
    }

    @Test
    void testApproveLogin() {
        TeamMember teamMember = createUniqueTeamMember();
        assertTrue(authInfoService.approveLogin(teamMember.getAccountId(), "defaultpw"));
    }

    @Test
    void testApproveLoginWithIncorrectPassword() {
        TeamMember teamMember = createUniqueTeamMember();
        assertFalse(authInfoService.approveLogin(teamMember.getAccountId(), "wrongpw"));
    }

    @Test
    void testAuthenticateUserWithSuccess() {
        TeamMember teamMember = createUniqueTeamMember();
        AuthInfoDTO response = authInfoService.authenticateUser(teamMember.getAccountId(), "defaultpw");

        assertNotNull(response);
        assertEquals(teamMember.getAccountId(), response.getAccountId());
        assertEquals(teamMember.getUserName(), response.getUserName());
        assertEquals(teamMember.getRole(), response.getRole());
    }

    @Test
    void testAuthenticateUser_Fail_WrongPassword() {
        TeamMember teamMember = createUniqueTeamMember();
        Exception exception = assertThrows(RuntimeException.class, () -> 
            authInfoService.authenticateUser(teamMember.getAccountId(), "wrongpw")
        );

        assertTrue(exception.getMessage().contains("Invalid Credentials"));
    }

    @Test
    void testAuthenticateUser_Fail_NonExistentUser() {
        Exception exception = assertThrows(RuntimeException.class, () -> 
            authInfoService.authenticateUser(999999, "somepassword")
        );

        assertTrue(exception.getMessage().contains("Team Member not found"));
    }

    @Test
    void testAuthenticateUser_Admin() {
        Admin admin = createUniqueAdmin();
        AuthInfoDTO response = authInfoService.authenticateUser(admin.getAccountId(), "adminpw");

        assertNotNull(response);
        assertEquals(admin.getAccountId(), response.getAccountId());
        assertEquals(admin.getUserName(), response.getUserName());
        assertEquals(admin.getRole(), response.getRole());
    }

    @Test
    void testAuthenticateUser_NonAdmin() {
        TeamMember teamMember = createUniqueTeamMember();
        AuthInfoDTO response = authInfoService.authenticateUser(teamMember.getAccountId(), "defaultpw");

        assertNotNull(response);
        assertEquals(teamMember.getAccountId(), response.getAccountId());
        assertEquals(teamMember.getUserName(), response.getUserName());
        assertEquals(teamMember.getRole(), response.getRole());
    }
}
