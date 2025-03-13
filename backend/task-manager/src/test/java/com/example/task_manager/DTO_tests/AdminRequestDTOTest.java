package com.example.task_manager.DTO_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.example.task_manager.DTO.AdminRequestDTO;

public class AdminRequestDTOTest {
    @Test
    void testAdminRequestDTOConstructorAndGetters() {
        String expectedName = "Admin User";
        String expectedEmail = "admin@example.com";
        String expectedPassword = "securepassword";

        AdminRequestDTO adminRequestDTO = new AdminRequestDTO(expectedName, expectedEmail, expectedPassword);

        assertEquals(expectedName, adminRequestDTO.getName());
        assertEquals(expectedEmail, adminRequestDTO.getEmail());
        assertEquals(expectedPassword, adminRequestDTO.getPassword());
    }

    @Test
    void testAdminRequestDTOSetters() {
        AdminRequestDTO adminRequestDTO = new AdminRequestDTO();

        adminRequestDTO.setName("New Admin");
        adminRequestDTO.setEmail("newadmin@example.com");
        adminRequestDTO.setPassword("newpassword");

        assertEquals("New Admin", adminRequestDTO.getName());
        assertEquals("newadmin@example.com", adminRequestDTO.getEmail());
        assertEquals("newpassword", adminRequestDTO.getPassword());
    }
}
