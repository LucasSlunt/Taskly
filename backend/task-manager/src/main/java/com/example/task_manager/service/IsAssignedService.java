package com.example.task_manager.service;

import org.springframework.stereotype.Service;
import com.example.task_manager.entity.IsAssigned;
import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.repository.IsAssignedRepository;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.repository.TeamMemberRepository;

@Service // Marks this class as a Spring service, allowing it to be injected where needed
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
	public void assignToTask(int teamMemberId, int taskId) {
		Task task = taskRepository.findById(taskId)
			.orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));

		TeamMember teamMember = teamMemberRepository.findById(teamMemberId)
			.orElseThrow(() -> new RuntimeException("Team Member not found with ID: " + teamMemberId));

		// Check if the member is already assigned to this task
		if (teamMember.getAssignedTasks().stream()
			.anyMatch(t -> t.getId() == taskId)) {
			throw new RuntimeException("Team Member is already assigned to this task. No action needed.");
		}

		// Create a new assignment entry
		IsAssigned isAssigned = new IsAssigned();
		isAssigned.setTask(task);
		isAssigned.setTeamMember(teamMember);
		isAssignedRepository.save(isAssigned);

		// Update bidirectional relationship
		teamMember.getAssignedTasks().add(isAssigned);
		teamMemberRepository.save(teamMember);

		// Flush changes to ensure consistency in the database
		teamMemberRepository.flush();
	}

	/**
	 * Unassigns a TeamMember from a Task.
	 * If the member is not assigned to the task, no changes are made.
	 *
	 * @param teamMemberId The ID of the team member to be unassigned.
	 * @param taskId The ID of the task from which the member should be unassigned.
	 */
	public void unassignFromTask(int teamMemberId, int taskId) {
		TeamMember teamMember = teamMemberRepository.findById(teamMemberId)
			.orElseThrow(() -> new RuntimeException("Team Member not found with ID: " + teamMemberId));
	
		Task task = taskRepository.findById(taskId)
			.orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));

		// Find the existing assignment
		IsAssigned toRemove = task.getAssignedMembers().stream()
			.filter(isAssigned -> isAssigned.getTeamMember().getAccountId() == teamMemberId)
			.findFirst()
			.orElse(null);
	
		if (toRemove != null) {
			// Remove the assignment from both sides of the relationship
			task.getAssignedMembers().remove(toRemove);
			teamMember.getAssignedTasks().remove(toRemove);
			isAssignedRepository.delete(toRemove);
		} else {
			System.out.println("Team Member is not assigned to this task. No action needed.");
		}
	}

	/**
	 * Checks if a TeamMember is assigned to a specific Task.
	 *
	 * @param teamMemberId The ID of the team member.
	 * @param taskId The ID of the task.
	 * @return true if the team member is assigned to the task, false otherwise.
	 */
	public boolean isAssignedToTask(int teamMemberId, int taskId) {
		TeamMember teamMember = teamMemberRepository.findById(teamMemberId)
			.orElseThrow(() -> new RuntimeException("Team Member not found with ID: " + teamMemberId));

		return teamMember.getAssignedTasks().stream()
			.anyMatch(isAssigned -> isAssigned.getTask().getTaskId() == taskId);
	}
}
