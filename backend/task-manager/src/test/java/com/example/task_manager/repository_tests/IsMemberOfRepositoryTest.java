package com.example.task_manager.repository_tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.task_manager.entity.IsMemberOf;
import com.example.task_manager.entity.Team;
import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.repository.IsMemberOfRepository;
import com.example.task_manager.repository.TeamMemberRepository;
import com.example.task_manager.repository.TeamRepository;

@DataJpaTest
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
    private IsMemberOf isMemberOf;

    /**
     * Sets up test data before each test method execution.
     * Ensures a clean database and inserts fresh data for consistent tests.
     */
    @BeforeEach
    void setUp() {
        isMemberOfRepository.deleteAllInBatch();
        teamRepository.deleteAllInBatch();
        teamMemberRepository.deleteAllInBatch();

        // Create and save a test Team
        testTeam = new Team();
        testTeam.setTeamName("Test Team " + System.nanoTime());
        testTeam = teamRepository.save(testTeam);

        // Create and save a test TeamMember
        testMember = new TeamMember("TestUser" + System.nanoTime(), "test" + System.nanoTime() + "@example.com","defaultpw");
        testMember = teamMemberRepository.save(testMember);

        // Create and save an IsMemberOf entry to associate the team member with the team
        isMemberOf = new IsMemberOf();
        isMemberOf.setTeam(testTeam);
        isMemberOf.setTeamMember(testMember);
        isMemberOf = isMemberOfRepository.save(isMemberOf);
    }

    /**
     * Tests the findMembersByTeamId() method to ensure it retrieves the correct members.
     */
    @Test
    void testFindMembersByTeamId() {
        List<IsMemberOf> members = isMemberOfRepository.findMembersByTeamId(testTeam.getTeamId());

        assertNotNull(members);
        assertEquals(1, members.size());
        assertEquals(testMember.getAccountId(), members.get(0).getTeamMember().getAccountId());
    }

    /**
     * Tests that finding members for a non-existent team returns an empty list.
     */
    @Test
    void testFindMembersByNonExistentTeamId() {
        List<IsMemberOf> members = isMemberOfRepository.findMembersByTeamId(9999);
        assertNotNull(members);
        assertTrue(members.isEmpty());
    }

    /**
     * Tests the existsByTeamMemberAccountIdAndTeamTeamId() method for an existing member.
     */
    @Test
    void testExistsByTeamMemberAndTeam() {
        boolean exists = isMemberOfRepository.existsByTeamMemberAccountIdAndTeamTeamId(
            testMember.getAccountId(), testTeam.getTeamId());
        assertTrue(exists);
    }

    /**
     * Tests the existsByTeamMemberAccountIdAndTeamTeamId() method for a non-existent membership.
     */
    @Test
    void testExistsByNonExistentMemberAndTeam() {
        boolean exists = isMemberOfRepository.existsByTeamMemberAccountIdAndTeamTeamId(9999, 9999);
        assertFalse(exists);
    }

    /**
     * Tests finding a specific TeamMember-Team relationship.
     */
    @Test
    void testFindByTeamMemberAndTeam() {
        Optional<IsMemberOf> foundMembership = isMemberOfRepository.findByTeamMemberAndTeam(testMember, testTeam);
        assertTrue(foundMembership.isPresent());
        assertEquals(isMemberOf.getId(), foundMembership.get().getId());
    }

    /**
     * Tests deleting a membership and ensuring it no longer exists.
     */
    @Test
    void testDeleteMembership() {
        isMemberOfRepository.delete(isMemberOf);

        Optional<IsMemberOf> deletedMembership = isMemberOfRepository.findByTeamMemberAndTeam(testMember, testTeam);
        assertFalse(deletedMembership.isPresent());

        boolean exists = isMemberOfRepository.existsByTeamMemberAccountIdAndTeamTeamId(
            testMember.getAccountId(), testTeam.getTeamId());
        assertFalse(exists);
    }
}
