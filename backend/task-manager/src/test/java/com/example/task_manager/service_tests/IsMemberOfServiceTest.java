package com.example.task_manager.service_tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import jakarta.transaction.Transactional;

import com.example.task_manager.DTO.TeamDTO;
import com.example.task_manager.DTO.TeamMemberDTO;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@ActiveProfiles("test")
public class IsMemberOfServiceTest extends ServiceTestHelper{

    @Test
    void testAddMemberToTeam() {
        TeamMemberDTO teamLead = createUniqueTeamMemberDTO();
        TeamDTO team = createUniqueTeamDTO(teamLead);
        TeamMemberDTO teamMember = createUniqueTeamMemberDTO();

        isMemberOfService.addMemberToTeam(teamMember.getAccountId(), team.getTeamId());

        assertTrue(isMemberOfService.isMemberOfTeam(teamMember.getAccountId(), team.getTeamId()));
    }

    @Test
    void testAddMemberToNonExistentTeam() {
        TeamMemberDTO teamMember = createUniqueTeamMemberDTO();

        Exception exception = assertThrows(RuntimeException.class,
            () -> isMemberOfService.addMemberToTeam(teamMember.getAccountId(), 9999));

        assertTrue(exception.getMessage().contains("Team not found"));
    }

    @Test
    void testAddNonExistentMemberToTeam() {
        TeamMemberDTO teamLead = createUniqueTeamMemberDTO();
        TeamDTO team = createUniqueTeamDTO(teamLead);

        Exception exception = assertThrows(RuntimeException.class,
            () -> isMemberOfService.addMemberToTeam(9999, team.getTeamId()));

        assertTrue(exception.getMessage().contains("Team Member not found"));
    }

    @Test
    void testAddMemberToSameTeamTwice() {
        TeamMemberDTO teamLead = createUniqueTeamMemberDTO();
        TeamDTO team = createUniqueTeamDTO(teamLead);
        TeamMemberDTO teamMember = createUniqueTeamMemberDTO();

        isMemberOfService.addMemberToTeam(teamMember.getAccountId(), team.getTeamId());

        Exception exception = assertThrows(RuntimeException.class,
            () -> isMemberOfService.addMemberToTeam(teamMember.getAccountId(), team.getTeamId()));

        assertTrue(exception.getMessage().contains("Team Member is already in this team"));
    }

    @Test
    void testRemoveMemberFromTeam() {
        TeamMemberDTO teamLead = createUniqueTeamMemberDTO();
        TeamDTO team = createUniqueTeamDTO(teamLead);
        TeamMemberDTO teamMember = createUniqueTeamMemberDTO();

        isMemberOfService.addMemberToTeam(teamMember.getAccountId(), team.getTeamId());
        isMemberOfService.removeMemberFromTeam(teamMember.getAccountId(), team.getTeamId());

        assertFalse(isMemberOfService.isMemberOfTeam(teamMember.getAccountId(), team.getTeamId()));
    }

    @Test
    void testRemoveNonExistentMemberFromTeam() {
        TeamMemberDTO teamLead = createUniqueTeamMemberDTO();
        TeamDTO team = createUniqueTeamDTO(teamLead);

        Exception exception = assertThrows(RuntimeException.class,
            () -> isMemberOfService.removeMemberFromTeam(9999, team.getTeamId()));

        assertTrue(exception.getMessage().contains("Team Member not found"));
    }

    @Test
    void testRemoveMemberFromNonExistentTeam() {
        TeamMemberDTO teamMember = createUniqueTeamMemberDTO();

        Exception exception = assertThrows(RuntimeException.class,
            () -> isMemberOfService.removeMemberFromTeam(teamMember.getAccountId(), 9999));

        assertTrue(exception.getMessage().contains("Team not found"));
    }

    @Test
    void testRemoveMemberNotInTeam() {
        TeamMemberDTO teamLead = createUniqueTeamMemberDTO();
        TeamDTO team = createUniqueTeamDTO(teamLead);
        TeamMemberDTO teamMember = createUniqueTeamMemberDTO();

        Exception exception = assertThrows(RuntimeException.class,
            () -> isMemberOfService.removeMemberFromTeam(teamMember.getAccountId(), team.getTeamId()));

        assertTrue(exception.getMessage().contains("Membership not found"));
    }

    @Test
    void testIsMemberOfTeam() {
        TeamMemberDTO teamLead = createUniqueTeamMemberDTO();
        TeamDTO team = createUniqueTeamDTO(teamLead);
        TeamMemberDTO teamMember = createUniqueTeamMemberDTO();

        isMemberOfService.addMemberToTeam(teamMember.getAccountId(), team.getTeamId());

        assertTrue(isMemberOfService.isMemberOfTeam(teamMember.getAccountId(), team.getTeamId()));

        isMemberOfService.removeMemberFromTeam(teamMember.getAccountId(), team.getTeamId());

        assertFalse(isMemberOfService.isMemberOfTeam(teamMember.getAccountId(), team.getTeamId()));
    }

    @Test
    void testIsMemberOfNonExistentTeam() {
        TeamMemberDTO teamMember = createUniqueTeamMemberDTO();
        assertFalse(isMemberOfService.isMemberOfTeam(teamMember.getAccountId(), 9999));
    }

    @Test
    void testIsMemberOfNonExistentMember() {
        TeamMemberDTO teamLead = createUniqueTeamMemberDTO();
        TeamDTO team = createUniqueTeamDTO(teamLead);
        assertFalse(isMemberOfService.isMemberOfTeam(9999, team.getTeamId()));
    }
}
