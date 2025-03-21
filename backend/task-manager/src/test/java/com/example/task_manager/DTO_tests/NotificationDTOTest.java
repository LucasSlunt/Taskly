package com.example.task_manager.DTO_tests;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.example.task_manager.DTO.NotificationDTO;
import com.example.task_manager.enums.NotificationType;

public class NotificationDTOTest {

    @Test
    void testDTOConstructorAndGetters() {
        LocalDateTime now = LocalDateTime.now();
        NotificationDTO dto = new NotificationDTO(1, "Task assigned", NotificationType.TASK_ASSIGNED, false, now, 100, 200);

        assertEquals(1, dto.getNotificationId());
        assertEquals("Task assigned", dto.getMessage());
        assertEquals(NotificationType.TASK_ASSIGNED, dto.getType());
        assertFalse(dto.isIsRead());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(100, dto.getTeamMemberId());
        assertEquals(200, dto.getTaskId());
    }

    @Test
    void testDTOSetters() {
        LocalDateTime now = LocalDateTime.now();
        NotificationDTO dto = new NotificationDTO(1, "Task assigned", NotificationType.TASK_ASSIGNED, false, now, 100, 200);

        LocalDateTime newTime = LocalDateTime.now().plusDays(1);
        dto.setNotificationId(2);
        dto.setMessage("Task updated");
        dto.setType(NotificationType.TASK_DUE_DATE_EDITED);
        dto.setIsRead(true);
        dto.setCreatedAt(newTime);
        dto.setTeamMemberId(300);
        dto.setTaskId(400);

        assertEquals(2, dto.getNotificationId());
        assertEquals("Task updated", dto.getMessage());
        assertEquals(NotificationType.TASK_DUE_DATE_EDITED, dto.getType());
        assertTrue(dto.isIsRead());
        assertEquals(newTime, dto.getCreatedAt());
        assertEquals(300, dto.getTeamMemberId());
        assertEquals(400, dto.getTaskId());
    }
}
