package com.example.task_manager.service_tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.transaction.Transactional;

import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.repository.AdminRepository;
import com.example.task_manager.repository.AuthInfoRepository;
import com.example.task_manager.repository.IsAssignedRepository;
import com.example.task_manager.repository.IsMemberOfRepository;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.repository.TeamMemberRepository;
import com.example.task_manager.repository.TeamRepository;
import com.example.task_manager.service.AuthInfoService;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class AuthInfoServiceTest {

    @Autowired
    private AuthInfoService authInfoService;

    @Autowired
	private AdminRepository adminRepository;

	@Autowired
	private TeamMemberRepository teamMemberRepository;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private IsAssignedRepository isAssignedRepository;

	@Autowired
	private IsMemberOfRepository isMemberOfRepository;
	
	@Autowired
	private AuthInfoRepository authInfoRepository;



private TeamMember teamMember;

@BeforeEach
	void setUp() {
        isAssignedRepository.deleteAll();
		isMemberOfRepository.deleteAll();
		taskRepository.deleteAll();
		teamMemberRepository.deleteAll();
		authInfoRepository.deleteAll();
		adminRepository.deleteAll();
		teamRepository.deleteAll();

        teamMember = new TeamMember("Authentication Tester", "auth_test" + System.nanoTime() + "@secure.com","defaultpw");
		teamMember = teamMemberRepository.save(teamMember);
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
}
