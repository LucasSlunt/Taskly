package com.example.task_manager.controller;

import com.example.task_manager.DTO.IsMemberOfDTO;
import com.example.task_manager.service.IsMemberOfService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/memberships")
public class IsMemberOfController {

    private final IsMemberOfService isMemberOfService;

    public IsMemberOfController(IsMemberOfService isMemberOfService) {
        this.isMemberOfService = isMemberOfService;
    }

    // Add Member to a Team
    @PostMapping("/{teamMemberId}/team/{teamId}")
    public ResponseEntity<?> addMemberToTeam(@PathVariable int teamMemberId, @PathVariable int teamId) {
        try {
            return ResponseEntity.ok(isMemberOfService.addMemberToTeam(teamMemberId, teamId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //mass assign members to a team
    @PostMapping("/team/{teamId}/mass-assign")
    public ResponseEntity<?> massAssignToTeam(@PathVariable int teamId, @RequestBody List<Integer> teamMemberIds) {
        try {
            List<IsMemberOfDTO> isMemberOfDTOs = isMemberOfService.massAssignToTeam(teamId, teamMemberIds);
            return ResponseEntity.ok(isMemberOfDTOs);
        }
        catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Remove Member from Team
    @DeleteMapping("/{teamMemberId}/team/{teamId}")
    public ResponseEntity<?> removeMemberFromTeam(@PathVariable int teamMemberId, @PathVariable int teamId) {
        try {
            return ResponseEntity.ok(isMemberOfService.removeMemberFromTeam(teamMemberId, teamId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Check if Member of Team
    @GetMapping("/{teamMemberId}/team/{teamId}")
    public ResponseEntity<?> isMemberOfTeam(@PathVariable int teamMemberId, @PathVariable int teamId) {
        return ResponseEntity.ok(isMemberOfService.isMemberOfTeam(teamMemberId, teamId));
    }
}