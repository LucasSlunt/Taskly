package com.example.task_manager.repository_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.task_manager.entity.IsMemberOf;
import com.example.task_manager.entity.Team;
import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.repository.IsMemberOfRepository;
import com.example.task_manager.repository.TeamMemberRepository;
import com.example.task_manager.repository.TeamRepository;

@SpringBootTest // Loads the full Spring Boot application context for integration testing
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class IsMemberOfRepositoryTest {

	@Autowired
	private IsMemberOfRepository isMemberOfRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private TeamMemberRepository teamMemberRepository;

	private Team testTeam;
	private TeamMember testMember;

	/**
	 * Sets up test data before each test method execution.
	 * Creates a Team, a TeamMember, and assigns the member to the team.
	 */
	@BeforeEach
	void setUp() {
		// Create and save a test Team
		testTeam = new Team();
		testTeam.setTeamName("Test Team " + System.nanoTime()); // Unique team name
		teamRepository.save(testTeam);

		// Create and save a test TeamMember
		testMember = new TeamMember("TestUser" + System.nanoTime(), "test" + System.nanoTime() + "@example.com");
		teamMemberRepository.save(testMember);

		// Create and save an IsMemberOf entry to associate the team member with the team
		IsMemberOf isMemberOf = new IsMemberOf();
		isMemberOf.setTeam(testTeam);
		isMemberOf.setTeamMember(testMember);
		isMemberOfRepository.save(isMemberOf);
	}

	/**
	 * Tests the findMembersByTeamId() method to ensure it retrieves the correct members.
	 */
	@Test
	void testFindMembersByTeamId() {
		// Query for members of testTeam
		List<IsMemberOf> members = isMemberOfRepository.findMembersByTeamId(testTeam.getTeamId());

		// Assertions to verify correct data retrieval
		assertNotNull(members, "The result list should not be null.");
		assertEquals(1, members.size(), "There should be exactly one member in the team.");
		assertEquals(testMember.getAccountId(), members.get(0).getTeamMember().getAccountId(), 
					 "The retrieved member ID should match the expected test member ID.");
	}
}
