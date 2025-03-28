package com.example.task_manager.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.task_manager.DTO.AdminDTO;
import com.example.task_manager.DTO.TeamDTO;
import com.example.task_manager.DTO.TeamMemberDTO;
import com.example.task_manager.DTO.TeamMemberWithTeamLeadDTO;
import com.example.task_manager.entity.*;
import com.example.task_manager.enums.RoleType;
import com.example.task_manager.repository.*;

import jakarta.transaction.Transactional;

@Service // Marks this class as a Spring service, allowing it to be managed as a Spring bean
@Transactional
public class AdminService extends TeamMemberService {

    private final AdminRepository adminRepository;
    private final NotificationRepository notificationRepository;
    private final AuthInfoRepository authInfoRepository;

    // Constructor injection for required repositories
    public AdminService(AdminRepository adminRepository,
            TeamMemberRepository teamMemberRepository,
            TeamRepository teamRepository,
            IsMemberOfRepository isMemberOfRepository,
            TaskRepository taskRepository,
            IsAssignedRepository isAssignedRepository,
            AuthInfoService authInfoService,
            NotificationService notifService,
            NotificationRepository notificationRepository,
            AuthInfoRepository authInfoRepository) {
        super(teamMemberRepository, teamRepository, taskRepository, isMemberOfRepository, isAssignedRepository,
                authInfoService, notifService, notificationRepository);
        this.adminRepository = adminRepository;
        this.notificationRepository = notificationRepository;
        this.authInfoRepository = authInfoRepository;
    }

    // Creates and saves a new Admin entity
    public AdminDTO createAdmin(String adminName, String adminEmail, String adminPassword) {
        Admin admin = new Admin(adminName, adminEmail, adminPassword);
        admin.setRole(RoleType.ADMIN);
        admin = adminRepository.save(admin);
        return convertToDTO(admin);
    }

    // Deletes an Admin by ID
    public void deleteAdmin(int adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with ID: " + adminId));
        adminRepository.delete(admin);
    }

    // Updates an Admin's username
    public AdminDTO modifyAdminName(int adminId, String newAdminName) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with ID: " + adminId));

        admin.setUserName(newAdminName);
        admin = adminRepository.save(admin);
        return convertToDTO(admin);
    }

    // Updates an Admin's email
    public AdminDTO modifyAdminEmail(int adminId, String newAdminEmail) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with ID: " + adminId));

        admin.setUserEmail(newAdminEmail);
        admin = adminRepository.save(admin);
        return convertToDTO(admin);
    }

    // Creates and saves a new TeamMember entity
    public TeamMemberDTO createTeamMember(String userName, String userEmail, String userPassword) {
        TeamMember teamMember = new TeamMember(userName, userEmail, userPassword);
        teamMember.setRole(RoleType.TEAM_MEMBER);
        teamMember = teamMemberRepository.save(teamMember);
        return convertToDTO(teamMember);
    }

    // Deletes a TeamMember by ID
    public void deleteTeamMember(int accountId) {
        teamMemberRepository.deleteById(accountId);
    }

    // Updates a TeamMember's username
    public TeamMemberDTO modifyTeamMemberName(int userId, String newUserName) {
        TeamMember teamMember = teamMemberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Team Member not found with ID: " + userId));

        teamMember.setUserName(newUserName);
        teamMember = teamMemberRepository.save(teamMember);
        return convertToDTO(teamMember);
    }

    // Updates a TeamMember's email
    public TeamMemberDTO modifyTeamMemberEmail(int userId, String newUserEmail) {
        TeamMember teamMember = teamMemberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Team Member not found with ID: " + userId));

        teamMember.setUserEmail(newUserEmail);
        teamMember = teamMemberRepository.save(teamMember);
        return convertToDTO(teamMember);
    }

    // Changes the role of a member
    public Object changeRole(int memberId, RoleType newRole) {
        if (newRole == RoleType.TEAM_MEMBER) {
            return demoteToTeamMember(memberId);
        } else if (newRole == RoleType.ADMIN) {
            return promoteToAdmin(memberId);
        } else {
            throw new IllegalArgumentException("Unsupported role: " + newRole + "\nRoles available: \n" + RoleType.ADMIN
                    + "\n" + RoleType.TEAM_MEMBER);
        }
    }

    @Transactional
    public AdminDTO promoteToAdmin(int teamMemberId) {
        TeamMember teamMember = teamMemberRepository.findById(teamMemberId)
                .orElseThrow(() -> new RuntimeException("Team Member not found with ID: " + teamMemberId));
    
        // Find all teams led by this member
        List<Team> teamsLed = teamRepository.findByTeamLead_AccountId(teamMember.getAccountId());
        for (Team team : teamsLed) {
            team.setTeamLead(null); // Explicitly remove team lead
        }
        teamRepository.saveAll(teamsLed);
        teamRepository.flush();
    
        // Extract info
        String oldName = teamMember.getUserName();
        String oldEmail = teamMember.getUserEmail();
        String oldHashedPassword = teamMember.getAuthInfo().getHashedPassword();
        String oldSalt = teamMember.getAuthInfo().getSalt();
    
        // Delete Notifications explicitly
        notificationRepository.deleteAll(teamMember.getNotifications());
        teamMember.getNotifications().clear();
    
        // DELETE OLD IsAssigned explicitly
        isAssignedRepository.deleteAll(teamMember.getAssignedTasks());
        teamMember.getAssignedTasks().clear();
    
        // DELETE OLD IsMemberOf explicitly
        isMemberOfRepository.deleteAll(teamMember.getTeams());
        teamMember.getTeams().clear();
    
        // DELETE AuthInfo explicitly
        authInfoRepository.delete(teamMember.getAuthInfo());
        teamMember.setAuthInfo(null);
    
        // Flush explicitly
        authInfoRepository.flush();
        isAssignedRepository.flush();
        isMemberOfRepository.flush();
        notificationRepository.flush();
    
        // Delete old TeamMember safely
        deleteTeamMember(teamMember.getAccountId());
        teamMemberRepository.flush();
    
        // NOW create the new Admin safely
        Admin newAdmin = new Admin(oldName, oldEmail);
    
        // Set new relationships as empty sets initially (you can add if needed)
        newAdmin.setAssignedTasks(new HashSet<>());
        newAdmin.setTeams(new HashSet<>());
    
        // Set new AuthInfo
        AuthInfo newAuthInfo = new AuthInfo();
        newAuthInfo.setHashedPassword(oldHashedPassword);
        newAuthInfo.setSalt(oldSalt);
        newAuthInfo.setTeamMember(newAdmin);
        newAdmin.setAuthInfo(newAuthInfo);
    
        // Save new Admin entity
        Admin savedAdmin = adminRepository.save(newAdmin);
    
        return convertToDTO(savedAdmin);
    }
    
    @Transactional
    public TeamMemberDTO demoteToTeamMember(int adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with ID: " + adminId));

        // Extract info
        String oldName = admin.getUserName();
        String oldEmail = admin.getUserEmail();
        String oldHashedPassword = admin.getAuthInfo().getHashedPassword();
        String oldSalt = admin.getAuthInfo().getSalt();

        // Delete Notifications explicitly
        notificationRepository.deleteAll(admin.getNotifications());
        admin.getNotifications().clear();

        // DELETE OLD IsAssigned explicitly
        isAssignedRepository.deleteAll(admin.getAssignedTasks());
        admin.getAssignedTasks().clear();

        // DELETE OLD IsMemberOf explicitly
        isMemberOfRepository.deleteAll(admin.getTeams());
        admin.getTeams().clear();

        // DELETE AuthInfo explicitly
        authInfoRepository.delete(admin.getAuthInfo());
        admin.setAuthInfo(null);

        // Flush all deletions
        authInfoRepository.flush();
        isAssignedRepository.flush();
        isMemberOfRepository.flush();
        notificationRepository.flush();

        // Delete old Admin safely
        deleteAdmin(admin.getAccountId());
        adminRepository.flush();

        // NOW create the new TeamMember safely
        TeamMember newTeamMember = new TeamMember(oldName, oldEmail);

        // Set new relationships as empty sets initially (you can add if needed)
        newTeamMember.setAssignedTasks(new HashSet<>());
        newTeamMember.setTeams(new HashSet<>());

        // Set new AuthInfo
        AuthInfo newAuthInfo = new AuthInfo();
        newAuthInfo.setHashedPassword(oldHashedPassword);
        newAuthInfo.setSalt(oldSalt);
        newAuthInfo.setTeamMember(newTeamMember);
        newTeamMember.setAuthInfo(newAuthInfo);

        // Save new TeamMember entity
        TeamMember savedTeamMember = teamMemberRepository.save(newTeamMember);

        return convertToDTO(savedTeamMember);
    }
    
    // Assigns a TeamMember to a Team by creating an IsMemberOf entry
    public TeamMemberDTO assignToTeam(int teamMemberId, int teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found with ID: " + teamId));

        TeamMember teamMember = teamMemberRepository.findById(teamMemberId)
                .orElseThrow(() -> new RuntimeException("Team Member not found with ID: " + teamMemberId));

        IsMemberOf isMemberOf = new IsMemberOf();
        isMemberOf.setTeam(team);
        isMemberOf.setTeamMember(teamMember);

        isMemberOfRepository.save(isMemberOf);

        // Ensures the association is also reflected in the TeamMember entity
        teamMember.getTeams().add(isMemberOf);
        return convertToDTO(teamMemberRepository.save(teamMember));
    }

    // Locks a Task by setting its isLocked property to true
    public void lockTask(int taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));

        task.setIsLocked(true);
        taskRepository.save(task);
    }

    // Unlocks a Task by setting its isLocked property to false
    public void unlockTask(int taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));

        task.setIsLocked(false);
        taskRepository.save(task);
    }

    //get all admins
    public List<AdminDTO> getAllAdmins() {
        return adminRepository.findAll().stream()
                .map(admin -> new AdminDTO(admin.getAccountId(), admin.getUserName(), admin.getUserEmail(),
                        admin.getRole()))
                .collect(Collectors.toList());
    }

    //get all teams
    public List<TeamDTO> getAllTeams() {
        return teamRepository.findAll().stream()
                .map(team -> new TeamDTO(
                    team.getTeamId(),
                    team.getTeamName(),
                    team.getTeamLead() != null ? team.getTeamLead().getAccountId() : -1 // 👈 use sentinel
                ))
                .collect(Collectors.toList());
    }

    //get a single admin
    public AdminDTO getAdminById(int adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        return new AdminDTO(admin.getAccountId(), admin.getUserName(), admin.getUserEmail(), admin.getRole());
    }

    //get a single team member
    public TeamMemberDTO getTeamMemberById(int teamMemberId) {
        TeamMember teamMember = teamMemberRepository.findById(teamMemberId)
                .orElseThrow(() -> new RuntimeException("Team Member not found"));
        return new TeamMemberDTO(teamMember.getAccountId(), teamMember.getUserName(), teamMember.getUserEmail(),
                teamMember.getRole());
    }

    private AdminDTO convertToDTO(Admin admin) {
        return new AdminDTO(admin.getAccountId(), admin.getUserName(), admin.getUserEmail(), admin.getRole());
    }

    //get all team members
    public List<TeamMemberWithTeamLeadDTO> getAllTeamMembers() {
        return teamMemberRepository.findAll().stream()
                .map(teamMember -> {
                    List<Team> teamLeadOf = teamRepository.findByTeamLead_AccountId(teamMember.getAccountId());
                    List<Integer> teamsLedIds = teamLeadOf.stream()
                            .map(Team::getTeamId)
                            .collect(Collectors.toList());
                    List<String> teamsLedNames = teamLeadOf.stream()
                            .map(Team::getTeamName)
                            .collect(Collectors.toList());
                    boolean isTeamLead = !teamsLedNames.isEmpty();

                    return new TeamMemberWithTeamLeadDTO(
                            teamMember.getAccountId(),
                            teamMember.getUserName(),
                            teamMember.getUserEmail(),
                            teamMember.getRole(),
                            isTeamLead,
                            teamsLedIds,
                            teamsLedNames);
                })
                .collect(Collectors.toList());
    }

    private TeamMemberDTO convertToDTO(TeamMember teamMember) {
        return new TeamMemberDTO(teamMember.getAccountId(), teamMember.getUserName(), teamMember.getUserEmail(),
                teamMember.getRole());
    }
}
