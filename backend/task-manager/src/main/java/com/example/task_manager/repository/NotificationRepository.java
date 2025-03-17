package com.example.task_manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.task_manager.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    List<Notification> findByTeamMemberIdAndIsReadFalse(int teamMemberId);

    List<Notification> findByTeamMemberIdAndIsReadTrue(int teamMemberId);

}
