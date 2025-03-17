package com.example.task_manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.task_manager.DTO.CreateNotificationRequestDTO;
import com.example.task_manager.service.NotificationService;

@RestController
@RequestMapping("/notif")
public class NotificationController {
    @Autowired
    private NotificationService notifService;

    public NotificationController(NotificationService notifService) {
        this.notifService = notifService;
    }

}
