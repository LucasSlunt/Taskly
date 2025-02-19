package com.example.task_manager.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.task_manager.DTO.IsAssignedDTO;
import com.example.task_manager.DTO.TaskDTO;
import com.example.task_manager.DTO.TeamMemberDTO;
import com.example.task_manager.entity.IsAssigned;
import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.Team;
import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.repository.IsAssignedRepository;
import com.example.task_manager.repository.IsMemberOfRepository;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.repository.TeamMemberRepository;
import com.example.task_manager.repository.TeamRepository;

import jakarta.transaction.Transactional;

@Service // Marks this class as a Spring service, making it eligible for dependency injection
@Transactional // Ensures all methods run within a transactional context
public class TeamMemberService {
	
	protected final TeamMemberRepository teamMemberRepository;
	protected final TeamRepository teamRepository;
	protected final IsMemberOfRepository isMemberOfRepository;
	protected final TaskRepository taskRepository;
	protected final IsAssignedRepository isAssignedRepository;

	// Constructor for required repositories
	public TeamMemberService(TeamMemberRepository teamMemberRepository, 
							 TeamRepository teamRepository, 
							 TaskRepository taskRepository, 
							 IsMemberOfRepository isMemberOfRepository, 
							 IsAssignedRepository isAssignedRepository) {
		this.teamMemberRepository = teamMemberRepository;
		this.teamRepository = teamRepository;
		this.isMemberOfRepository = isMemberOfRepository;
		this.taskRepository = taskRepository;
		this.isAssignedRepository = isAssignedRepository;
	}
	
	/**
	 * Creates and saves a new Task.
	 * Allows optional parameters such as description, expected completion date, and due date.
	 * 
	 * @param title                 The title of the task.
	 * @param description           The task description (nullable).
	 * @param isLocked              Indicates if the task is locked (nullable).
	 * @param status                The current status of the task.
	 * @param expectedCompletionDate Expected completion date (nullable).
	 * @param dueDate               The due date of the task (nullable).
	 * @param team                  The team responsible for the task.
	 * @param assignedMembers       The set of assigned members (nullable).
	 * @return The created Task entity.
	 */
	public TaskDTO createTask(String title, String description, Boolean isLocked, String status, 
						   LocalDate expectedCompletionDate, LocalDate dueDate, Team team, 
						   Set<IsAssigned> assignedMembers) {

		if (title == null || title.trim().isEmpty()) {
			throw new RuntimeException("Task title cannot be null or empty");
		}
	
		if (team == null) {
			throw new RuntimeException("Task must be assigned to a team");
		}

		Task task = new Task();
		task.setTitle(title);
		task.setIsLocked(isLocked);
		task.setStatus(status);
		task.setDateCreated(LocalDate.now());
		task.setTeam(team);

		if (description != null) {
			task.setDescription(description);
		}
		if (expectedCompletionDate != null) {
			task.setExpectedCompletionDate(expectedCompletionDate);
		}
		if (dueDate != null) {
			task.setDueDate(dueDate);
		}

		task = taskRepository.save(task);
		return convertToDTO(task);
	}

	/**
	 * Deletes a task by ID.
	 * Explicitly removes associated assignments before deleting the task.
	 *
	 * @param taskId The ID of the task to be deleted.
	 */
	public void deleteTask(int taskId) {
		Task task = taskRepository.findById(taskId)
				.orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));
	
		// Ensure all assignments are removed before deleting the task
		isAssignedRepository.deleteById(taskId);
	
		taskRepository.delete(task);
	}    

	/**
	 * Edits the details of an existing Task.
	 * Allows partial updates; only non-null and non-empty values are updated.
	 *
	 * @param taskId                The ID of the task to be updated.
	 * @param newTitle              The new title (nullable).
	 * @param newDescription        The new description (nullable).
	 * @param isLocked              The lock status (nullable).
	 * @param newStatus             The updated task status (nullable).
	 * @param newExpectedCompletionDate New expected completion date (nullable).
	 * @param newDueDate            The new due date (nullable).
	 * @return The updated Task entity.
	 */
	public TaskDTO editTask(int taskId, String newTitle, String newDescription, Boolean isLocked, 
						 String newStatus, LocalDate newExpectedCompletionDate, LocalDate newDueDate) {
		Task task = taskRepository.findById(taskId)
			.orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));
	
		if (newTitle != null && !newTitle.isEmpty()) {
			task.setTitle(newTitle);
		}
		if (newDescription != null && !newDescription.isEmpty()) {
			task.setDescription(newDescription);
		}
		if (isLocked != null) {
			task.setIsLocked(isLocked);
		}
		if (newStatus != null && !newStatus.isEmpty()) {
			task.setStatus(newStatus);
		}
		if (newExpectedCompletionDate != null) {
			task.setExpectedCompletionDate(newExpectedCompletionDate);
		}
		if (newDueDate != null) {
			task.setDueDate(newDueDate);
		}

		task = taskRepository.save(task);
		return convertToDTO(task);
	}

	/**
	 * Assigns a TeamMember to a Task.
	 * Ensures that a member cannot be assigned to the same task more than once.
	 *
	 * @param taskId       The ID of the task.
	 * @param teamMemberId The ID of the team member to be assigned.
	 */
	public IsAssignedDTO assignToTask(int taskId, int teamMemberId) {
		Task task = taskRepository.findById(taskId)
			.orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));

		TeamMember teamMember = teamMemberRepository.findById(teamMemberId)
			.orElseThrow(() -> new RuntimeException("Team Member not found with ID: " + teamMemberId));

		// Ensure that the member is not already assigned to this task
		boolean alreadyAssigned = isAssignedRepository.existsByTeamMember_AccountIdAndTask_TaskId(teamMemberId, taskId);
		if (alreadyAssigned) {
			throw new RuntimeException("Team Member is already assigned to this task.");
		}

		IsAssigned isAssigned = new IsAssigned(task, teamMember, task.getTeam());
		isAssigned = isAssignedRepository.save(isAssigned);

		return convertToDTO(isAssigned);	
	}

	/**
	 * Changes the password for a TeamMember.
	 * Currently, this method is a placeholder for future implementation.
	 *
	 * @param teamMemberId The ID of the team member.
	 * @param oldPassword  The current password (not yet implemented).
	 * @param newPassword  The new password to set (not yet implemented).
	 */
	public void changePassword(int teamMemberId, String oldPassword, String newPassword) {
		TeamMember teamMember = teamMemberRepository.findById(teamMemberId)
			.orElseThrow(() -> new RuntimeException("Team Member not found with ID: " + teamMemberId));

		// Password change logic to be implemented in the future
	}

	public TeamMemberDTO getTeamMember(int accountId) {
		TeamMember teamMember = teamMemberRepository.findById(accountId)
			.orElseThrow(() -> new RuntimeException("Team Member not found with ID: " + accountId));
		
		return convertToDTO(teamMember);
	}

	public List<TaskDTO> getAssignedTasks(int teamMemberId) {
		return isAssignedRepository.findByTeamMember_AccountId(teamMemberId).stream()
			.map(isAssigned -> convertToDTO(isAssigned.getTask()))
						.collect(Collectors.toList());
	}
			
				/*
	 * Converts TeamMember entity to a TeamMemberDTO
	 */
	private TeamMemberDTO convertToDTO(TeamMember teamMember) {
		return new TeamMemberDTO(
			teamMember.getAccountId(), 
			teamMember.getUserName(), 
			teamMember.getUserEmail()
		);
	}

	/**
	 * Converts a Task entity to a TaskDTO.
	 */
	private TaskDTO convertToDTO(Task task) {
		return new TaskDTO(
			task.getTaskId(),
			task.getTitle(),
			task.getDescription(),
			task.isLocked(),
			task.getStatus(),
			task.getDateCreated(),
			task.getTeam() != null ? task.getTeam().getTeamId() : null
		);
	}

	/**
	 * Converts an IsAssigned entity to an IsAssignedDTO.
	 */
	private IsAssignedDTO convertToDTO(IsAssigned isAssigned) {
		return new IsAssignedDTO(
			isAssigned.getId(),
			isAssigned.getTask().getTaskId(),
			isAssigned.getTeamMember().getAccountId(),
			isAssigned.getTeam().getTeamId()
		);
	}
}
