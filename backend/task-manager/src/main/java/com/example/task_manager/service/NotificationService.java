package com.example.task_manager.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.task_manager.entity.IsAssigned;
import com.example.task_manager.entity.Notification;
import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.Team;
import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.enums.NotificationType;
import com.example.task_manager.repository.IsAssignedRepository;
import com.example.task_manager.repository.NotificationRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class NotificationService {
    @Autowired
    private final NotificationRepository notifRepository;

    @Autowired
    private final IsAssignedRepository isAssignedRepository;

    public NotificationService(NotificationRepository notifRepository, IsAssignedRepository isAssignedRepository) {
        this.notifRepository = notifRepository;
        this.isAssignedRepository = isAssignedRepository;
    }

    //helper method for creating notifications
    private void createNotification(TeamMember teamMember, Task task, NotificationType type, String message) {
        Notification notif = new Notification(type, message, task, teamMember);
        notifRepository.save(notif);
    }

    //notify assigned members when a task is edited
    public void notifyAssignedMembersAboutTaskEdit(Task oldTask, Task updatedTask, TeamMember updatedBy) {
        Collection<IsAssigned> assignedMembers = isAssignedRepository.findByTask(oldTask);

        for (IsAssigned isAssigned : assignedMembers) {
            TeamMember teamMember = isAssigned.getTeamMember();
            
            //checkign if title was changed
            if (!oldTask.getTitle().equals(updatedTask.getTitle())) {
                createNotification(
                        teamMember,
                        updatedTask,
                        NotificationType.TASK_TITLE_EDITED,
                        updatedBy.getAccountId() + " edited the title of \"" + updatedTask.getTitle() + "\""
                );
            }
            
            //checking if description was changed
            if (!oldTask.getDescription().equals(updatedTask.getDescription())) {
                createNotification(
                    teamMember,
                    updatedTask,
                    NotificationType.TASK_DESCRIPTION_EDITED,
                    updatedBy.getAccountId() + " edited the title of \"" + updatedTask.getTitle() + "\""
                );
            }

            //checking if due date was changed
            if (!oldTask.getDueDate().equals(updatedTask.getDueDate())) {
                createNotification(
                        teamMember,
                        updatedTask,
                        NotificationType.TASK_DUE_DATE_EDITED,
                        updatedBy.getAccountId() + " edited the title of \"" + updatedTask.getTitle() + "\""
                );
            }

            //checking if status was changed
            if (!oldTask.getStatus().equals(updatedTask.getStatus())) {
                createNotification(
                    teamMember,
                    updatedTask,
                    NotificationType.TASK_STATUS_EDITED,
                    updatedBy.getAccountId() + " edited the title of \"" + updatedTask.getTitle() + "\""
                );
            }

            //checking if locked or unlocked
            if (!oldTask.isLocked() != updatedTask.isLocked()) {
                createNotification(
                        teamMember,
                        updatedTask,
                        NotificationType.TASK_LOCK_STATUS_CHANGED,
                        updatedBy.getAccountId() + " edited the title of \"" + updatedTask.getTitle() + "\""
                );
            }
        }
    }

    //notify member when task is assigned
    public void notifyTaskAssignment(TeamMember teamMember, Task task) {
        createNotification(teamMember, task, NotificationType.TASK_ASSIGNED, "You have been assigned to a new task: \"" + task.getTitle());
    }

    //notify member when they are added to a team
    public void notifyTeamAssignment(TeamMember teamMember, Team team) {
        createNotification(teamMember, null, NotificationType.TEAM_ASSIGNED, "You have been assigned to a new team: \"" + team.getTeamName());
    }

    //get unread notifications for a team member
    public List<Notification> getUnreadNotifications(int teamMemberId) {
        return notifRepository.findByTeamMemberIdAndIsReadFalse(teamMemberId);
    }

    //get read notifications for a team member
    public List<Notification> getReadNotifications(int teamMemberId) {
        return notifRepository.findByTeamMemberIdAndIsReadTrue(teamMemberId);
    }

    //mark notifications as read
    public void markAsRead(int notificationId) {
        Notification notif = notifRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found."));

        notif.setIsRead(true);
        notifRepository.save(notif);
    }

    //delete a notification
    public void deleteNotification(int notificationId) {
        notifRepository.deleteById(notificationId);
    }


}
