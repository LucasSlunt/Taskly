package com.example.task_manager.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.task_manager.entity.IsAssigned;
import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.TeamMember;

@Repository
public interface IsAssignedRepository extends JpaRepository<IsAssigned, Integer> {

    Optional<IsAssigned> findByTeamMemberAndTask(TeamMember teamMember, Task task);

    boolean existsByTeamMember_AccountIdAndTask_TaskId(int teamMemberId, int taskId);

    Collection<IsAssigned> findByTeamMember_AccountId(int teamMemberId);

    Collection<IsAssigned> findByTask(Task task);
}