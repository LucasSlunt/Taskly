package com.example.task_manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.task_manager.entity.Team;
import com.example.task_manager.entity.TeamMember;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Integer> {

    @Query("SELECT t FROM Team t JOIN t.members tm WHERE tm.id = :teamMemberId")
    List<Team> findAllTeamsForMember(@Param("teamMemberId") int teamMemberId);
}
