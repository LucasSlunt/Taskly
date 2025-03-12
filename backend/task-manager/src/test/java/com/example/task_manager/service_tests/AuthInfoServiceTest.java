package com.example.task_manager.service_tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.transaction.Transactional;

import com.example.task_manager.DTO.AuthInfoDTO;
import com.example.task_manager.entity.Admin;
import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.repository.AuthInfoRepository;
import com.example.task_manager.repository.IsAssignedRepository;
import com.example.task_manager.repository.IsMemberOfRepository;
import com.example.task_manager.repository.TeamMemberRepository;
import com.example.task_manager.service.AuthInfoService;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class AuthInfoServiceTest {

    @Autowired
    private AuthInfoService authInfoService;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private IsAssignedRepository isAssignedRepository;

    @Autowired
    private IsMemberOfRepository isMemberOfRepository;
        
    @Autowired
    private AuthInfoRepository authInfoRepository;

    private TeamMember teamMember;
    private Admin admin;

    @BeforeEach
	void setUp() {
        isAssignedRepository.deleteAllInBatch();
        isMemberOfRepository.deleteAllInBatch();
        authInfoRepository.deleteAllInBatch();
		teamMemberRepository.deleteAllInBatch();

        teamMember = new TeamMember("Authentication Tester", "auth_test" + System.nanoTime() + "@secure.com","defaultpw");
        teamMember = teamMemberRepository.save(teamMember);
         
        admin = new Admin("Admin User", "admin" + System.nanoTime() + "@secure.com", "adminpw");
        admin = teamMemberRepository.save(admin);
    }

    @Test
    void testHashPassword() {
        //check to see if the password is hashed correctly
        String expectedHash = "B0F900221C7BA3474D782D4FE34FC165CE98414F66F2AC74DAEAA08A1D80B5C6";
        String generatedHash = AuthInfoService.hashPassword("awesomeSecurePassword123","sixteenByteSalt!");
        assertEquals(expectedHash,generatedHash);
    }

    @Test
    void testGenerateSalt() {
        String salt = AuthInfoService.generateSalt();
        assertTrue(salt.length() == 16);
    }

    @Test 
    void testSaltsAreUnique(){
        String salt1 = AuthInfoService.generateSalt();
        String salt2 = AuthInfoService.generateSalt();
        assertFalse(salt1.equals(salt2));
    }

    @Test
    void testApproveLogin() {
        int teamMemberId = teamMember.getAccountId();
        assertTrue(authInfoService.approveLogin(teamMemberId,"defaultpw"));
    }

    @Test
    void testApproveLoginWithIncorrectPassword() {
        int teamMemberId = teamMember.getAccountId();
        assertFalse(authInfoService.approveLogin(teamMemberId,"wrongpw"));
    }

    //team member is not an admin
    @Test
    void testAuthenticateUserWithSuccess() {
        List<TeamMember> allMembers = teamMemberRepository.findAll();

        AuthInfoDTO response = authInfoService.authenticateUser(teamMember.getAccountId(), "defaultpw");

        assertNotNull(response);
        assertEquals(teamMember.getAccountId(), response.getAccountId());
        assertEquals(teamMember.getUserName(), response.getUserName());
        assertFalse(response.getIsAdmin()); // Regular member, not admin
    }

    //wrong password put in
    @Test
    void testAuthenticateUser_Fail_WrongPassword() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authInfoService.authenticateUser(teamMember.getAccountId(), "wrongpw");
        });

        assertTrue(exception.getMessage().contains("Invalid Credentials"));
    }

    //non existent user
    @Test
    void testAuthenticateUser_Fail_NonExistentUser() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authInfoService.authenticateUser(999999, "somepassword");
        });

        assertTrue(exception.getMessage().contains("Team Member not found"));
    }

    //should return true with a real admin
    @Test
    void testAuthenticateUser_Admin() {
        AuthInfoDTO response = authInfoService.authenticateUser(admin.getAccountId(), "adminpw");

        assertNotNull(response);
        assertEquals(admin.getAccountId(), response.getAccountId());
        assertEquals(admin.getUserName(), response.getUserName());
        assertTrue(response.getIsAdmin());
    }

    //should return false with a member that is not an admin
    @Test
    void testAuthenticateUser_NonAdmin() {
        AuthInfoDTO response = authInfoService.authenticateUser(teamMember.getAccountId(), "defaultpw");

        assertNotNull(response);
        assertEquals(teamMember.getAccountId(), response.getAccountId());
        assertEquals(teamMember.getUserName(), response.getUserName());
        assertFalse(response.getIsAdmin());
    }
}
