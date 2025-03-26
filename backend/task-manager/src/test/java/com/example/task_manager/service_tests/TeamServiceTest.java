package com.example.task_manager.service_tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import jakarta.transaction.Transactional;

import com.example.task_manager.TestHelper;
import com.example.task_manager.DTO.TeamDTO;
import com.example.task_manager.DTO.TeamMemberDTO;
import com.example.task_manager.entity.Team;
import com.example.task_manager.entity.TeamMember;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@ActiveProfiles("test")
public class TeamServiceTest extends TestHelper{
    @Test
    void testCreateTeam() {
        TeamMember teamLead = createUniqueTeamMember("Lead");
        String teamName = "QA Team " + System.nanoTime();

        TeamDTO newTeam = teamService.createTeam(teamName, teamLead.getAccountId());

        assertNotNull(newTeam);
        assertEquals(teamName, newTeam.getTeamName());
        assertEquals(teamLead.getAccountId(), newTeam.getTeamLeadId());
    }

    @Test
    void testCreateTeamWithEmptyName() {
        TeamMember teamLead = createUniqueTeamMember("Lead");

        Exception exception = assertThrows(RuntimeException.class,
                () -> teamService.createTeam("", teamLead.getAccountId()));

        assertTrue(exception.getMessage().contains("Team name cannot be empty"));
    }

    @Test
    void testCreateTeamWithNonExistentLead() {
        Exception exception = assertThrows(RuntimeException.class,
                () -> teamService.createTeam("New Team", 9999));

        assertTrue(exception.getMessage().contains("Team Lead not found"));
    }

    @Test
    void testDeleteTeam() {
        TeamMember teamLead = createUniqueTeamMember("Lead");
        Team team = createUniqueTeam(teamLead);

        teamService.deleteTeam(team.getTeamId());

        Optional<Team> deletedTeam = teamRepository.findById(team.getTeamId());
        assertFalse(deletedTeam.isPresent());
    }

    @Test
    void testDeleteNonExistentTeam() {
        Exception exception = assertThrows(RuntimeException.class,
                () -> teamService.deleteTeam(9999));

        assertTrue(exception.getMessage().contains("Team not found"));
    }

    @Test
    void testChangeTeamLead() {
        TeamMember oldLead = createUniqueTeamMember("OldLead");
        TeamMember newLead = createUniqueTeamMember("NewLead");
        Team team = createUniqueTeam(oldLead);

        String newTeamName = "Updated Team Name";

        TeamDTO updatedTeamDTO = teamService.changeTeamLead(team.getTeamId(), newTeamName, newLead.getAccountId());

        assertNotNull(updatedTeamDTO);
        assertEquals(newTeamName, updatedTeamDTO.getTeamName());
        assertEquals(newLead.getAccountId(), updatedTeamDTO.getTeamLeadId());

        Team updatedTeam = teamRepository.findById(team.getTeamId()).orElseThrow();
        assertEquals(newTeamName, updatedTeam.getTeamName());
        assertEquals(newLead.getAccountId(), updatedTeam.getTeamLead().getAccountId());
    }

    @Test
    void testChangeTeamLeadToNonExistentMember() {
        TeamMember oldLead = createUniqueTeamMember("OldLead");
        Team team = createUniqueTeam(oldLead);

        String newTeamName = "Updated Team Name";

        Exception exception = assertThrows(RuntimeException.class,
                () -> teamService.changeTeamLead(team.getTeamId(), newTeamName, 9999));

        assertTrue(exception.getMessage().contains("Team Lead not found"));
    }

    @Test
    void testGetTeamMembers() {
        TeamMember teamLead = createUniqueTeamMember("Lead");
        TeamMember member = createUniqueTeamMember("Member");
        Team team = createUniqueTeam(teamLead);

        isMemberOfService.addMemberToTeam(member.getAccountId(), team.getTeamId());

        List<TeamMemberDTO> teamMembers = teamService.getTeamMembers(team.getTeamId());

        assertNotNull(teamMembers);
        assertFalse(teamMembers.isEmpty());

        assertTrue(teamMembers.stream()
                .anyMatch(t -> t.getAccountId() == member.getAccountId()));
    }

    @Test
    void testGetMembersOfNonExistentTeam() {
        Exception exception = assertThrows(RuntimeException.class,
                () -> teamService.getTeamMembers(9999));

        assertTrue(exception.getMessage().contains("Team not found"));
    }

    @Test
    void testAddMemberToNonExistentTeam() {
        TeamMember member = createUniqueTeamMember("Member");

        Exception exception = assertThrows(RuntimeException.class,
                () -> isMemberOfService.addMemberToTeam(member.getAccountId(), 9999));

        assertTrue(exception.getMessage().contains("Team not found"));
    }

    @Test
    void testGetMembersOfTeamWithNoMembers() {
        TeamMember teamLead = createUniqueTeamMember("Lead");
        Team team = createUniqueTeam(teamLead);

        List<TeamMemberDTO> members = teamService.getTeamMembers(team.getTeamId());
        assertTrue(members.isEmpty());
    }
}
