package com.example.task_manager.service_tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import jakarta.transaction.Transactional;

import com.example.task_manager.DTO.IsMemberOfDTO;
import com.example.task_manager.DTO.TeamDTO;
import com.example.task_manager.DTO.TeamMemberDTO;
import com.example.task_manager.test_helpers.ServiceTestHelper;

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
    void testMassAddMembersToTeam() {
        TeamMemberDTO teamLead = createUniqueTeamMemberDTO();
        TeamDTO team = createUniqueTeamDTO(teamLead);

        TeamMemberDTO member1 = createUniqueTeamMemberDTO();
        TeamMemberDTO member2 = createUniqueTeamMemberDTO();
        TeamMemberDTO member3 = createUniqueTeamMemberDTO();

        List<Integer> memberIds = List.of(
            member1.getAccountId(),
            member2.getAccountId(),
            member3.getAccountId()
        );

        List<IsMemberOfDTO> results = isMemberOfService.massAssignToTeam(team.getTeamId(), memberIds);

        assertEquals(3, results.size());

        assertTrue(isMemberOfService.isMemberOfTeam(member1.getAccountId(), team.getTeamId()));
        assertTrue(isMemberOfService.isMemberOfTeam(member2.getAccountId(), team.getTeamId()));
        assertTrue(isMemberOfService.isMemberOfTeam(member3.getAccountId(), team.getTeamId()));
    }

    @Test
    void testMassAddMembersWithOneNonExistentId() {
        TeamMemberDTO teamLead = createUniqueTeamMemberDTO();
        TeamDTO team = createUniqueTeamDTO(teamLead);

        TeamMemberDTO member1 = createUniqueTeamMemberDTO();
        TeamMemberDTO member2 = createUniqueTeamMemberDTO();
        int nonExistentId = 999999;

        List<Integer> memberIds = List.of(
            member1.getAccountId(),
            nonExistentId,
            member2.getAccountId()
        );

        Exception exception = assertThrows(RuntimeException.class, () ->
            isMemberOfService.massAssignToTeam(team.getTeamId(), memberIds)
        );

        assertTrue(exception.getMessage().contains("Team Member not found"));
    }

    @Test
    void testMassAddMembersToNonExistentTeam() {
        TeamMemberDTO member = createUniqueTeamMemberDTO();

        Exception exception = assertThrows(RuntimeException.class, () ->
            isMemberOfService.massAssignToTeam(999999, List.of(member.getAccountId()))
        );

        assertTrue(exception.getMessage().contains("Team not found"));
    }

    @Test
    void testMassAddMembersAlreadyInTeam() {
        TeamMemberDTO teamLead = createUniqueTeamMemberDTO();
        TeamDTO team = createUniqueTeamDTO(teamLead);

        TeamMemberDTO member1 = createUniqueTeamMemberDTO();
        TeamMemberDTO member2 = createUniqueTeamMemberDTO();

        isMemberOfService.addMemberToTeam(member1.getAccountId(), team.getTeamId());

        List<Integer> memberIds = List.of(
            member1.getAccountId(), // already in team
            member2.getAccountId()
        );

        List<IsMemberOfDTO> results = isMemberOfService.massAssignToTeam(team.getTeamId(), memberIds);

        assertEquals(1, results.size());
        assertTrue(isMemberOfService.isMemberOfTeam(member2.getAccountId(), team.getTeamId()));
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
