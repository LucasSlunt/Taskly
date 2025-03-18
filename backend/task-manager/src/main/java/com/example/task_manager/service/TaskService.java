package com.example.task_manager.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.task_manager.DTO.TeamMemberDTO;
import com.example.task_manager.entity.IsAssigned;
import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.repository.TaskRepository;

import jakarta.transaction.Transactional;

@Service // Marks this class as a Spring service, allowing it to be injected where needed
@Transactional
public class TaskService {

	private final TaskRepository taskRepository;

	// Constructor injection for the repository
	public TaskService(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	/**
	 * Notifies all team members assigned to a specific task.
	 * If no members are assigned, a message is logged instead.
	 *
	 * @param taskId  The ID of the task.
	 * @param message The notification message to send.
	 */
	public void notifyMembers(int taskId, String message) {
		Task task = taskRepository.findById(taskId)
				.orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));

		Set<IsAssigned> assignedMembers = task.getAssignedMembers();

		if (assignedMembers.isEmpty()) {
			System.out.println("No workers assigned to this task.");
			return;
		}

		for (IsAssigned isAssigned : assignedMembers) {
			TeamMember member = isAssigned.getTeamMember();

			// Notification system is not yet implemented
			// sendNotification(member, message);

			//should use the DTO's to avoid sharing database info with the frontend
		}
	}
	
	

	/**
	 * Converts TeamMember entity to TeamMemberDTO.
	 */
	private TeamMemberDTO convertToDTO(TeamMember teamMember) {
		return new TeamMemberDTO(teamMember.getAccountId(), teamMember.getUserName(), teamMember.getUserEmail());
	}
}

