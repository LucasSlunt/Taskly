package com.example.task_manager.service;

import java.util.HashSet;

import org.springframework.stereotype.Service;

import com.example.task_manager.entity.Admin;
import com.example.task_manager.entity.IsMemberOf;
import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.Team;
import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.repository.AdminRepository;
import com.example.task_manager.repository.IsAssignedRepository;
import com.example.task_manager.repository.IsMemberOfRepository;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.repository.TeamMemberRepository;
import com.example.task_manager.repository.TeamRepository;

@Service // Marks this class as a Spring service, allowing it to be managed as a Spring bean
public class AdminService extends TeamMemberService {

	private final AdminRepository adminRepository;

	// Constructor injection for required repositories
	public AdminService(AdminRepository adminRepository, 
						TeamMemberRepository teamMemberRepository, 
						TeamRepository teamRepository, 
						IsMemberOfRepository isMemberOfRepository, 
						TaskRepository taskRepository,
						IsAssignedRepository isAssignedRepository) {
		super(teamMemberRepository, teamRepository, taskRepository, isMemberOfRepository, isAssignedRepository);
		this.adminRepository = adminRepository;
	}

	// Creates and saves a new Admin entity
	public Admin createAdmin(String adminName, String adminEmail) {
		Admin admin = new Admin(adminName, adminEmail);
		return adminRepository.save(admin);
	}

	// Deletes an Admin by ID
	public void deleteAdmin(int adminId) {
		adminRepository.deleteById(adminId);
	}

	// Updates an Admin's username
	public Admin modifyAdminName(int adminId, String newAdminName) {
		Admin admin = adminRepository.findById(adminId)
			.orElseThrow(() -> new RuntimeException("Admin not found with ID: " + adminId));

		admin.setUserName(newAdminName);
		return adminRepository.save(admin);
	}

	// Updates an Admin's email
	public Admin modifyAdminEmail(int adminId, String newAdminEmail) {
		Admin admin = adminRepository.findById(adminId)
			.orElseThrow(() -> new RuntimeException("Admin not found with ID: " + adminId));
	
		admin.setUserEmail(newAdminEmail);
		return adminRepository.save(admin);
	}

	// Creates and saves a new TeamMember entity
	public TeamMember createTeamMember(String userName, String userEmail) {
		TeamMember teamMember = new TeamMember(userName, userEmail);
		return teamMemberRepository.save(teamMember);
	}

	// Deletes a TeamMember by ID
	public void deleteTeamMember(int accountId) {
		teamMemberRepository.deleteById(accountId);
	}

	// Updates a TeamMember's username
	public TeamMember modifyTeamMemberName(int userId, String newUserName) {
		TeamMember teamMember = teamMemberRepository.findById(userId)
			.orElseThrow(() -> new RuntimeException("Team Member not found with ID: " + userId));
	
		teamMember.setUserName(newUserName);
		return teamMemberRepository.save(teamMember);
	}

	// Updates a TeamMember's email
	public TeamMember modifyTeamMemberEmail(int userId, String newUserEmail) {
		TeamMember teamMember = teamMemberRepository.findById(userId)
			.orElseThrow(() -> new RuntimeException("Team Member not found with ID: " + userId));

		teamMember.setUserEmail(newUserEmail);
		return teamMemberRepository.save(teamMember);
	}

	// Promotes a TeamMember to Admin by removing them from the TeamMember table and adding them to the Admin table
	public Admin promoteToAdmin(int teamMemberId) {
		TeamMember teamMember = teamMemberRepository.findById(teamMemberId)
			.orElseThrow(() -> new RuntimeException("Team Member not found with ID: " + teamMemberId));

		teamMemberRepository.delete(teamMember);
		teamMemberRepository.flush(); // Ensures deletion is immediately reflected in the database

		// Creates a new Admin using the TeamMember's existing details
		Admin admin = new Admin();
		admin.setUserName(teamMember.getUserName());
		admin.setUserEmail(teamMember.getUserEmail());
		admin.setAuthInfo(teamMember.getAuthInfo());
		admin.setTeams(new HashSet<>(teamMember.getTeams()));

		return adminRepository.save(admin);
	}

	// Assigns a TeamMember to a Team by creating an IsMemberOf entry
	public void assignToTeam(int teamMemberId, int teamId) {
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
		teamMemberRepository.save(teamMember);
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
}
