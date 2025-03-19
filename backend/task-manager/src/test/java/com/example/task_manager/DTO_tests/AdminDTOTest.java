package com.example.task_manager.DTO_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.example.task_manager.DTO.AdminDTO;
import com.example.task_manager.enums.RoleType;

public class AdminDTOTest {
    @Test
    void testDTOConstructorAndGetters() {
        AdminDTO adminDTO = new AdminDTO(1, "John Doe", "john.doe@example.com", RoleType.ADMIN);

        assertEquals(1, adminDTO.getAccountId());
        assertEquals("John Doe", adminDTO.getUserName());
        assertEquals("john.doe@example.com", adminDTO.getUserEmail());
    }

    @Test
    void testDTOSetters() {
        AdminDTO adminDTO = new AdminDTO(1, "John Doe", "john.doe@example.com", RoleType.ADMIN);

        adminDTO.setAccountId(2);
        adminDTO.setUserName("Jane Doe");
        adminDTO.setUserEmail("jane.doe@example.com");

        assertEquals(2, adminDTO.getAccountId());
        assertEquals("Jane Doe", adminDTO.getUserName());
        assertEquals("jane.doe@example.com", adminDTO.getUserEmail());
    }
}
