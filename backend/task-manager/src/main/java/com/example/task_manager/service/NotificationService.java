package com.example.task_manager.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.task_manager.DTO.NotificationDTO;
import com.example.task_manager.entity.IsAssigned;
import com.example.task_manager.entity.Notification;
import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.Team;
import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.enums.NotificationType;
import com.example.task_manager.repository.IsAssignedRepository;
import com.example.task_manager.repository.NotificationRepository;
import com.example.task_manager.repository.TeamMemberRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class NotificationService {
    private final NotificationRepository notifRepository;
    private final IsAssignedRepository isAssignedRepository;
    private final TeamMemberRepository teamMemberRepository;

    public NotificationService(NotificationRepository notifRepository, IsAssignedRepository isAssignedRepository, TeamMemberRepository teamMemberRepository) {
        this.notifRepository = notifRepository;
        this.isAssignedRepository = isAssignedRepository;
        this.teamMemberRepository = teamMemberRepository;
    }

    //helper method for creating notifications
    private NotificationDTO createNotification(TeamMember teamMember, Task task, NotificationType type,
            String message) {
        Notification notif = new Notification(type, message, task, teamMember);
        teamMember.addNotification(notif);
        notifRepository.save(notif);
        return convertToDTO(notif);
    }
    
    //notify members that the task title was changed
    public void notifyTaskTitleChange(Task updatedTask, String oldTitle) {
        Collection<IsAssigned> assignedMembers = isAssignedRepository.findByTask(updatedTask);
        for (IsAssigned isAssigned : assignedMembers) {
            TeamMember teamMember = isAssigned.getTeamMember();

            createNotification(
                teamMember,
                updatedTask,
                NotificationType.TASK_TITLE_EDITED,
                "Task Update: The title of a task was changed from \"" + oldTitle + "\" to \"" + updatedTask.getTitle() + "\""
            );
        }
    }

    //notify members that the task description was changed
    public void notifyTaskDescriptionChange(Task updatedTask, String oldDescription) {
        Collection<IsAssigned> assignedMembers = isAssignedRepository.findByTask(updatedTask);
        for (IsAssigned isAssigned : assignedMembers) {
            TeamMember teamMember = isAssigned.getTeamMember();

            createNotification(
                teamMember,
                updatedTask,
                NotificationType.TASK_DESCRIPTION_EDITED,
                "Task Update: The description of a task was changed from \"" + oldDescription + "\" to \"" + updatedTask.getDescription() + "\""
            );
        }
    }

    //notify members that the task lcoked status was changed
    public void notifyTaskLockChange(Task updatedTask, boolean oldLockStatus) {
        Collection<IsAssigned> assignedMembers = isAssignedRepository.findByTask(updatedTask);
        for (IsAssigned isAssigned : assignedMembers) {
            TeamMember teamMember = isAssigned.getTeamMember();

            createNotification(
                teamMember,
                updatedTask,
                NotificationType.TASK_LOCK_STATUS_CHANGED,
                "Task Update: The lock status of a task was changed from \"" + oldLockStatus + "\" to \"" + updatedTask.isLocked() + "\""
            );
        }
    }

    //notify members that the task due date was changed
    public void notifyTaskDueDateChange(Task updatedTask, LocalDate oldDueDate) {
        Collection<IsAssigned> assignedMembers = isAssignedRepository.findByTask(updatedTask);
        for (IsAssigned isAssigned : assignedMembers) {
            TeamMember teamMember = isAssigned.getTeamMember();

            createNotification(
                    teamMember,
                    updatedTask,
                    NotificationType.TASK_DUE_DATE_EDITED,
                    "Task Update: The due date of a task was changed from \"" + oldDueDate + "\" to \"" + updatedTask.getDueDate()
                            + "\"");
        }
    }
    
    //notify members that the task status was changed
    public void notifyTaskStatusChange(Task updatedTask, String oldStatus) {
        Collection<IsAssigned> assignedMembers = isAssignedRepository.findByTask(updatedTask);
        for (IsAssigned isAssigned : assignedMembers) {
            TeamMember teamMember = isAssigned.getTeamMember();

            createNotification(
                teamMember,
                updatedTask,
                NotificationType.TASK_STATUS_EDITED,
                "Task Update: The status of a task was changed from \"" + oldStatus + "\" to \"" + updatedTask.getStatus() + "\""
            );
        }
    }

    //notify member when task is assigned
    public void notifyTaskAssignment(TeamMember teamMember, Task task) {
        createNotification(teamMember, task, NotificationType.TASK_ASSIGNED,
                "You have been assigned to a task: \"" + task.getTitle());
    }
    
    //notify member when task is unassigned
    public void notifyTaskUnassignment(TeamMember teamMember, Task task) {
        createNotification(teamMember, task, NotificationType.TASK_UNASSIGNED,
                "You have been unassigned from a task: \"" + task.getTitle());
    }

    //notify member when they are added to a team
    public void notifyTeamAssignment(TeamMember teamMember, Team team) {
        createNotification(teamMember, null, NotificationType.TEAM_ASSIGNED,
                "You have been assigned to a team: \"" + team.getTeamName());
    }
    
    //notify member when they are removed from a team
    public void notifyTeamUnassignment(TeamMember teamMember, Team team) {
        createNotification(teamMember, null, NotificationType.TEAM_UNASSIGNED,
                "You have been unassigned from a team: \"" + team.getTeamName());
    }

    //get unread notifications for a team member
    public List<NotificationDTO> getUnreadNotifications(int teamMemberId) {
        TeamMember teamMember = teamMemberRepository.findById(teamMemberId)
                .orElseThrow(() -> new RuntimeException("TeamMember not found"));
        
        return notifRepository.findByTeamMemberIdAndIsReadFalse(teamMemberId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    //get read notifications for a team member
    public List<NotificationDTO> getReadNotifications(int teamMemberId) {
        TeamMember teamMember = teamMemberRepository.findById(teamMemberId)
                .orElseThrow(() -> new RuntimeException("TeamMember not found"));

        return notifRepository.findByTeamMemberIdAndIsReadTrue(teamMemberId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    //mark notification as read
    public void markAsRead(int notificationId) {
        Notification notif = notifRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found."));

        notif.setIsRead(true);
        notifRepository.save(notif);
    }

    //mark notification as unread
    public void markAsUnread(int notificationId) {
        Notification notif = notifRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found."));

        notif.setIsRead(false);
        notifRepository.save(notif);
    }

    //delete a notification
    public void deleteNotification(int notificationId) {
        notifRepository.deleteById(notificationId);
    }

    private NotificationDTO convertToDTO(Notification notification) {
        return new NotificationDTO(
            notification.getNotificationId(),
            notification.getMessage(),
            notification.getType(),
            notification.getIsRead(),
            notification.getCreatedAt(),
            notification.getTeamMember().getAccountId(),
            notification.getTask() != null ? notification.getTask().getTaskId() : null
        );
    }
}
