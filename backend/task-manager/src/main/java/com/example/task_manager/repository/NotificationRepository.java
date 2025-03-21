package com.example.task_manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.task_manager.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    @Query("SELECT n FROM Notification n WHERE n.teamMember.accountId = :teamMemberId AND n.isRead = false")
    List<Notification> findByTeamMemberIdAndIsReadFalse(@Param("teamMemberId") int teamMemberId);

    @Query("SELECT n FROM Notification n WHERE n.teamMember.accountId = :teamMemberId AND n.isRead = true")
    List<Notification> findByTeamMemberIdAndIsReadTrue(@Param("teamMemberId") int teamMemberId);
}
