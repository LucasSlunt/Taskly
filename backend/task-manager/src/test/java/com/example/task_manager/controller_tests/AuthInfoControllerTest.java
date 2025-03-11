package com.example.task_manager.controller_tests;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.example.task_manager.DTO.AuthInfoDTO;
import com.example.task_manager.controller.AuthController;
import com.example.task_manager.service.AuthInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
 

@WebMvcTest(AuthController.class)
public class AuthInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthInfoService authInfoService;

    @InjectMocks
    private AuthController authController;

    private AuthInfoDTO mockUser;
    private AuthInfoDTO mockAdmin;

    @BeforeEach
    void setUp() {
        mockUser = new AuthInfoDTO(1, "John Doe", false);
        mockAdmin = new AuthInfoDTO(2, "Admin User", true);
    }

    // Test Successful Login
    @Test
    void testLogin_Success() throws Exception {
        when(authInfoService.authenticateUser(1, "correctpassword")).thenReturn(mockUser);

        mockMvc.perform(post("/auth-info/login")
                .param("teamMemberId", "1")
                .param("password", "correctpassword"))
                .andExpect(status().isOk()) // Expect HTTP 200
                .andExpect(jsonPath("$.accountId").value(1))
                .andExpect(jsonPath("$.userName").value("John Doe"))
                .andExpect(jsonPath("$.isAdmin").value(false));

        verify(authInfoService, times(1)).authenticateUser(1, "correctpassword");
    }

    //  Test Login as Admin
    @Test
    void testLogin_AdminSuccess() throws Exception {
        when(authInfoService.authenticateUser(2, "adminpassword")).thenReturn(mockAdmin);

        mockMvc.perform(post("/auth-info/login")
                .param("teamMemberId", "2")
                .param("password", "adminpassword"))
                .andExpect(status().isOk()) // Expect HTTP 200
                .andExpect(jsonPath("$.accountId").value(2))
                .andExpect(jsonPath("$.userName").value("Admin User"))
                .andExpect(jsonPath("$.isAdmin").value(true));

        verify(authInfoService, times(1)).authenticateUser(2, "adminpassword");
    }

    // Test Login with Incorrect Password
    @Test
    void testLogin_Failure_InvalidCredentials() throws Exception {
        when(authInfoService.authenticateUser(1, "wrongpassword"))
                .thenThrow(new RuntimeException("Invalid Credentials"));

        mockMvc.perform(post("/auth-info/login")
                .param("teamMemberId", "1")
                .param("password", "wrongpassword"))
                .andExpect(status().isUnauthorized()) // Expect HTTP 401
                .andExpect(content().string(""));

        verify(authInfoService, times(1)).authenticateUser(1, "wrongpassword");
    }

    // Test Login with Nonexistent User
    @Test
    void testLogin_Failure_NonExistentUser() throws Exception {
        when(authInfoService.authenticateUser(9999, "somepassword"))
                .thenThrow(new RuntimeException("Team Member not found"));

        mockMvc.perform(post("/auth-info/login")
                .param("teamMemberId", "9999")
                .param("password", "somepassword"))
                .andExpect(status().isUnauthorized()) // Expect HTTP 401
                .andExpect(content().string(""));

        verify(authInfoService, times(1)).authenticateUser(9999, "somepassword");
    }
}
