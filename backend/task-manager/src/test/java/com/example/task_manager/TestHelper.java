package com.example.task_manager;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.task_manager.DTO.TeamDTO;
import com.example.task_manager.DTO.TeamMemberDTO;
import com.example.task_manager.entity.Admin;
import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.Team;
import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.enums.TaskPriority;
import com.example.task_manager.repository.AdminRepository;
import com.example.task_manager.repository.AuthInfoRepository;
import com.example.task_manager.repository.IsAssignedRepository;
import com.example.task_manager.repository.IsMemberOfRepository;
import com.example.task_manager.repository.NotificationRepository;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.repository.TeamMemberRepository;
import com.example.task_manager.repository.TeamRepository;
import com.example.task_manager.service.AdminService;
import com.example.task_manager.service.AuthInfoService;
import com.example.task_manager.service.IsAssignedService;
import com.example.task_manager.service.IsMemberOfService;
import com.example.task_manager.service.NotificationService;
import com.example.task_manager.service.TeamMemberService;
import com.example.task_manager.service.TeamService;

public class TestHelper {
    @Autowired
    protected AdminService adminService;

    @Autowired
    protected TeamService teamService;

    @Autowired
    protected TeamMemberService teamMemberService;

    @Autowired
    protected AuthInfoService authInfoService;

    @Autowired
    protected IsAssignedService isAssignedService;

    @Autowired
    protected IsMemberOfService isMemberOfService;

    @Autowired
    protected NotificationService notificationService;

    @Autowired
    protected NotificationRepository notificationRepository;

    @Autowired
    protected AdminRepository adminRepository;

    @Autowired
    protected TeamMemberRepository teamMemberRepository;

    @Autowired
    protected TaskRepository taskRepository;

    @Autowired
    protected TeamRepository teamRepository;

    @Autowired
    protected IsAssignedRepository isAssignedRepository;

    @Autowired
    protected IsMemberOfRepository isMemberOfRepository;

    @Autowired
    protected AuthInfoRepository authInfoRepository;

    protected TeamMember createUniqueTeamMember() {
        return teamMemberRepository.save(new TeamMember(
            "TeamMember_" + System.nanoTime(),
            "team_member" + System.nanoTime() + "@secure.com",
            "defaultpw"
        ));
    }

    protected TeamMember createUniqueTeamMember(String role) {
        return teamMemberRepository.save(new TeamMember(
                role + "_" + System.nanoTime(),
                role.toLowerCase() + System.nanoTime() + "@example.com",
                "defaultpw"
        ));
    }

    protected Admin createUniqueAdmin() {
        return adminRepository.save(new Admin(
            "Admin_" + System.nanoTime(),
            "admin_" + System.nanoTime() + "@secure.com",
            "adminpw"
        ));
    }

    protected Team createUniqueTeam() {
        return teamRepository.save(new Team(
            "Team_" + System.nanoTime(),
            null
        ));
    }

    protected Team createUniqueTeam(TeamMember teamLead) {
        return teamRepository.save(new Team(
            "Team_" + System.nanoTime(),
            teamLead
        ));
    }

    protected Task createUniqueTask(Team team) {
        return taskRepository.save(new Task(
            "Task_" + System.nanoTime(),
            "Description for task",
            team,
            false,
            "Open",
            LocalDate.now(),
            TaskPriority.LOW
        ));
    }

    protected TeamMemberDTO createUniqueTeamMemberDTO() {
        return adminService.createTeamMember(
            "TeamMember_" + System.nanoTime(),
            "team_member" + System.nanoTime() + "@example.com",
            "defaultpw"
        );
    }

    protected TeamDTO createUniqueTeamDTO(TeamMemberDTO teamLead) {
        return teamService.createTeam(
            "Team_" + System.nanoTime(),
            teamLead.getAccountId()
        );
    }
}
