package com.example.task_manager.service_tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import com.example.task_manager.entity.Team;
import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.repository.TeamMemberRepository;
import com.example.task_manager.repository.TeamRepository;
import com.example.task_manager.service.AdminService;
import com.example.task_manager.service.IsMemberOfService;
import com.example.task_manager.service.TeamService;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class IsMemberOfServiceTest {

    @Autowired
    private TeamService teamService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private IsMemberOfService isMemberOfService;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    private Team team;
    private TeamMember teamLead;
    private TeamMember teamMember;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAllInBatch();
        teamRepository.deleteAllInBatch();
        teamMemberRepository.deleteAllInBatch();
    }

    @Test
    void testAddMemberToTeam() {
        teamLead = adminService.createTeamMember("Team Lead", "team_lead@example.com");
        team = teamService.createTeam("Team Title", teamLead.getAccountId());
        teamMember = adminService.createTeamMember("Team Member", "team_member@example.com");

        isMemberOfService.addMemberToTeam(teamMember.getAccountId(), team.getTeamId());

        teamRepository.flush();

        team = teamRepository.findById(team.getTeamId()).orElseThrow();

        assertTrue(team.getMembers().stream()
            .anyMatch(isMemberOf -> isMemberOf.getTeamMember().getAccountId() == teamMember.getAccountId()));
    }

    @Test
    void testRemoveMemberFromTeam() {
        teamLead = adminService.createTeamMember("Team Lead", "team_lead@example.com");
        team = teamService.createTeam("Team Title", teamLead.getAccountId());
        teamMember = adminService.createTeamMember("Team Member", "team_member@example.com");

        isMemberOfService.addMemberToTeam(teamMember.getAccountId(), team.getTeamId());
        isMemberOfService.removeMemberFromTeam(teamMember.getAccountId(), team.getTeamId());

        Team updatedTeam = teamRepository.findById(team.getTeamId()).orElseThrow();
        updatedTeam.getMembers().size();

        assertFalse(updatedTeam.getMembers().stream()
            .anyMatch(member -> member.getId() == teamMember.getAccountId()));
    }

    @Test
    void testIsMemberOfTeam() {
        teamLead = adminService.createTeamMember("Team Lead", "team_lead@example.com");
        team = teamService.createTeam("Team Title", teamLead.getAccountId());
        teamMember = adminService.createTeamMember("Team Member", "team_member@example.com");

        isMemberOfService.addMemberToTeam(teamMember.getAccountId(), team.getTeamId());

        boolean isMember = isMemberOfService.isMemberOfTeam(teamMember.getAccountId(), team.getTeamId());
        assertTrue(isMember);

        isMemberOfService.removeMemberFromTeam(teamMember.getAccountId(), team.getTeamId());

        teamRepository.flush();

        boolean isStillMember = isMemberOfService.isMemberOfTeam(teamMember.getAccountId(), team.getTeamId());
        assertFalse(isStillMember);
    }
}
