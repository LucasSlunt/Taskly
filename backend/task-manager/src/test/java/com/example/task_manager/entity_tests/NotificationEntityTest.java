package com.example.task_manager.entity_tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.task_manager.entity.Notification;
import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.Team;
import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.enums.NotificationType;
import jakarta.persistence.PersistenceException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class NotificationEntityTest extends EntityTestHelper{

    @Test
    void testNotificationPersistence() {
        Team team = createAndPersistTeam();
        TeamMember teamMember = createAndPersistTeamMember();
        Task task = createAndPersistTask(team);

        Notification notification = new Notification(NotificationType.TASK_ASSIGNED, "You have been assigned a task.", task, teamMember);
        entityManager.persist(notification);
        entityManager.flush();

        Notification savedNotification = entityManager.find(Notification.class, notification.getNotificationId());

        assertNotNull(savedNotification);
        assertEquals(notification.getMessage(), savedNotification.getMessage());
        assertEquals(notification.getType(), savedNotification.getType());
    }

    @Test
    void testPrePersistCreatedAtField() {
        Team team = createAndPersistTeam();
        TeamMember teamMember = createAndPersistTeamMember();
        Task task = createAndPersistTask(team);

        Notification notification = new Notification(NotificationType.TASK_STATUS_EDITED, "Task status updated", task, teamMember);
        entityManager.persist(notification);
        entityManager.flush();

        Notification savedNotification = entityManager.find(Notification.class, notification.getNotificationId());

        assertNotNull(savedNotification.getCreatedAt());
    }

    @Test
    void testNotificationAssociations() {
        Team team = createAndPersistTeam();
        TeamMember teamMember = createAndPersistTeamMember();
        Task task = createAndPersistTask(team);

        Notification notification = new Notification(NotificationType.TASK_DUE_DATE_EDITED, "Task due date changed", task, teamMember);
        entityManager.persist(notification);
        entityManager.flush();

        Notification savedNotification = entityManager.find(Notification.class, notification.getNotificationId());

        assertNotNull(savedNotification.getTask());
        assertEquals(task.getTaskId(), savedNotification.getTask().getTaskId());

        assertNotNull(savedNotification.getTeamMember());
        assertEquals(teamMember.getAccountId(), savedNotification.getTeamMember().getAccountId());
    }

    @Test
    void testReadUnreadStatusToggle() {
        Team team = createAndPersistTeam();
        TeamMember teamMember = createAndPersistTeamMember();
        Task task = createAndPersistTask(team);

        Notification notification = new Notification(NotificationType.TASK_ASSIGNED, "Task assigned", task, teamMember);
        notification.setIsRead(false);
        entityManager.persist(notification);
        entityManager.flush();

        Notification savedNotification = entityManager.find(Notification.class, notification.getNotificationId());

        assertFalse(savedNotification.getIsRead());

        savedNotification.setIsRead(true);
        entityManager.merge(savedNotification);
        entityManager.flush();

        Notification updatedNotification = entityManager.find(Notification.class, savedNotification.getNotificationId());
        assertTrue(updatedNotification.getIsRead());
    }

    @Test
    void testDeletingNotification() {
        Team team = createAndPersistTeam();
        TeamMember teamMember = createAndPersistTeamMember();
        Task task = createAndPersistTask(team);

        Notification notification = new Notification(NotificationType.TASK_UNASSIGNED, "Task unassigned", task, teamMember);
        entityManager.flush();

        int notificationId = notification.getNotificationId();

        entityManager.remove(notification);
        entityManager.flush();

        assertNull(entityManager.find(Notification.class, notificationId));
    }

    @Test
    void testNotificationWithoutTeamMemberFails() {
        Team team = createAndPersistTeam();
        Task task = createAndPersistTask(team);

        Notification notification = new Notification(NotificationType.TASK_ASSIGNED, "Task assigned", task, null);

        Exception e = assertThrows(PersistenceException.class, () -> {
            entityManager.persist(notification);
            entityManager.flush();
        });

        assertNotNull(e);
    }
}
