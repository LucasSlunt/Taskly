package com.example.task_manager.service;

import org.springframework.stereotype.Service;

import com.example.task_manager.DTO.IsAssignedDTO;
import com.example.task_manager.entity.IsAssigned;
import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.repository.IsAssignedRepository;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.repository.TeamMemberRepository;

import jakarta.transaction.Transactional;

@Service // Marks this class as a Spring service, allowing it to be injected where needed
@Transactional
public class IsAssignedService {
	
	private final TaskRepository taskRepository;
	private final TeamMemberRepository teamMemberRepository;
	private final IsAssignedRepository isAssignedRepository;

	// Constructor injection for required repositories
	public IsAssignedService(TaskRepository taskRepository, 
							 TeamMemberRepository teamMemberRepository, 
							 IsAssignedRepository isAssignedRepository) {
		this.taskRepository = taskRepository;
		this.teamMemberRepository = teamMemberRepository;
		this.isAssignedRepository = isAssignedRepository;
	}

	/**
	 * Assigns a TeamMember to a Task.
	 * Prevents duplicate assignments by checking if the member is already assigned.
	 *
	 * @param teamMemberId The ID of the team member to be assigned.
	 * @param taskId The ID of the task to assign the member to.
	 */
	public IsAssignedDTO assignToTask(int teamMemberId, int taskId) {
		Task task = taskRepository.findById(taskId)
			.orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));

		TeamMember teamMember = teamMemberRepository.findById(teamMemberId)
			.orElseThrow(() -> new RuntimeException("Team Member not found with ID: " + teamMemberId));

		// Check if the member is already assigned to this task
		boolean alreadyAssigned = isAssignedRepository.existsByTeamMember_AccountIdAndTask_TaskId(teamMemberId, taskId);
		if (alreadyAssigned) {
			throw new RuntimeException("Team Member is already assigned to this task. No action needed.");
		}

		// Create assignment entry
		IsAssigned isAssigned = new IsAssigned(task, teamMember, task.getTeam());
		isAssigned = isAssignedRepository.save(isAssigned);

		// Return assignment details as DTO
		return convertToDTO(isAssigned);
	}

	/**
	 * Unassigns a TeamMember from a Task.
	 * If the member is not assigned to the task, no changes are made.
	 *
	 * @param teamMemberId The ID of the team member to be unassigned.
	 * @param taskId The ID of the task from which the member should be unassigned.
	 */
	public IsAssignedDTO unassignFromTask(int teamMemberId, int taskId) {
		TeamMember teamMember = teamMemberRepository.findById(teamMemberId)
			.orElseThrow(() -> new RuntimeException("Team Member not found with ID: " + teamMemberId));
	
		Task task = taskRepository.findById(taskId)
			.orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));

		// Find the existing assignment
		IsAssigned toRemove = isAssignedRepository.findByTeamMemberAndTask(teamMember, task)
			.orElseThrow(() -> new RuntimeException("Assignment not found."));
	
		isAssignedRepository.delete(toRemove);

		return convertToDTO(toRemove);
	}

	/**
	 * Checks if a TeamMember is assigned to a specific Task.
	 *
	 * @param teamMemberId The ID of the team member.
	 * @param taskId The ID of the task.
	 * @return true if the team member is assigned to the task, false otherwise.
	 */
	public boolean isAssignedToTask(int teamMemberId, int taskId) {
		return isAssignedRepository.existsByTeamMember_AccountIdAndTask_TaskId(teamMemberId, taskId);
	}	

	private IsAssignedDTO convertToDTO(IsAssigned isAssigned) {
		return new IsAssignedDTO(
			isAssigned.getId(),
			isAssigned.getTask().getTaskId(),
			isAssigned.getTeamMember().getAccountId(),
			isAssigned.getTeam().getTeamId()
		);
	}
}
