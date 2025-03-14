package com.example.task_manager.repository_tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.task_manager.entity.IsMemberOf;
import com.example.task_manager.entity.Team;
import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.repository.AuthInfoRepository;
import com.example.task_manager.repository.IsMemberOfRepository;
import com.example.task_manager.repository.TeamMemberRepository;
import com.example.task_manager.repository.TeamRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class IsMemberOfRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private IsMemberOfRepository isMemberOfRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private AuthInfoRepository authInfoRepository;

    /**
     * Creates and persists a unique Team.
     */
    private Team createUniqueTeam() {
        Team team = new Team();
        team.setTeamName("Test Team " + System.nanoTime());
        entityManager.persist(team);
        entityManager.flush();
        return team;
    }

    /**
     * Creates and persists a unique TeamMember.
     */
    private TeamMember createUniqueTeamMember() {
        TeamMember teamMember = new TeamMember("TestUser" + System.nanoTime(),
                "test" + System.nanoTime() + "@example.com", "defaultpw");
        entityManager.persist(teamMember);
        entityManager.flush();
        return teamMember;
    }

    /**
     * Creates and persists a unique IsMemberOf relationship.
     */
    private IsMemberOf createUniqueMembership(Team team, TeamMember teamMember) {
        IsMemberOf isMemberOf = new IsMemberOf();
        isMemberOf.setTeam(team);
        isMemberOf.setTeamMember(teamMember);
        entityManager.persist(isMemberOf);
        entityManager.flush();
        return isMemberOf;
    }

    /**
     * Tests the findMembersByTeamId() method to ensure it retrieves the correct members.
     */
    @Test
    void testFindMembersByTeamId() {
        Team team = createUniqueTeam();
        TeamMember teamMember = createUniqueTeamMember();
        createUniqueMembership(team, teamMember);

        List<IsMemberOf> members = isMemberOfRepository.findMembersByTeamId(team.getTeamId());

        assertNotNull(members);
        assertEquals(1, members.size());
        assertEquals(teamMember.getAccountId(), members.get(0).getTeamMember().getAccountId());
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
        Team team = createUniqueTeam();
        TeamMember teamMember = createUniqueTeamMember();
        createUniqueMembership(team, teamMember);

        boolean exists = isMemberOfRepository.existsByTeamMemberAccountIdAndTeamTeamId(
                teamMember.getAccountId(), team.getTeamId());
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
        Team team = createUniqueTeam();
        TeamMember teamMember = createUniqueTeamMember();
        IsMemberOf isMemberOf = createUniqueMembership(team, teamMember);

        Optional<IsMemberOf> foundMembership = isMemberOfRepository.findByTeamMemberAndTeam(teamMember, team);
        assertTrue(foundMembership.isPresent());
        assertEquals(isMemberOf.getId(), foundMembership.get().getId());
    }

    /**
     * Tests deleting a membership and ensuring it no longer exists.
     */
    @Test
    void testDeleteMembership() {
        Team team = createUniqueTeam();
        TeamMember teamMember = createUniqueTeamMember();
        IsMemberOf isMemberOf = createUniqueMembership(team, teamMember);

        isMemberOfRepository.delete(isMemberOf);
        entityManager.flush();

        Optional<IsMemberOf> deletedMembership = isMemberOfRepository.findByTeamMemberAndTeam(teamMember, team);
        assertFalse(deletedMembership.isPresent());

        boolean exists = isMemberOfRepository.existsByTeamMemberAccountIdAndTeamTeamId(
                teamMember.getAccountId(), team.getTeamId());
        assertFalse(exists);
    }
}
