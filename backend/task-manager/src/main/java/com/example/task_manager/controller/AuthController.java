package com.example.task_manager.controller;

import com.example.task_manager.DTO.AuthInfoDTO;
import com.example.task_manager.service.AuthInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth-info")
public class AuthController {

    private final AuthInfoService authInfoService;

    public AuthController(AuthInfoService authInfoService) {
        this.authInfoService = authInfoService;
    }

    /*
     * Endpoint for authenticating a user.
     * Expects a JSON request body with `teamMemberId` and `password`.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthInfoDTO> login(@RequestParam int teamMemberId, @RequestParam String password) {
        try {
            AuthInfoDTO authInfo = authInfoService.authenticateUser(teamMemberId, password);
            return ResponseEntity.ok(authInfo);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(null); // 401 Unauthorized
        }
    }

    /*
     * Endpoint for figuring out is a user is an admin
     * Takes `teamMemberId` as a path variable
     */
    @GetMapping("/{teamMemberId}/is-admin")
    public ResponseEntity<Boolean> isAdmin(@PathVariable int teamMemberId) {
        try {
            boolean isAdmin = authInfoService.isAdmin(teamMemberId);
            return ResponseEntity.ok(isAdmin);
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        }
    }
}