package com.example.task_manager.DTO_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.example.task_manager.DTO.UpdateEmailRequestDTO;

public class UpdateEmailRequestDTOTest {
    @Test
    void testUpdateNameRequestDTOConstructorAndGetters() {
        String expectedNewEmail = "email@example.com";

        UpdateEmailRequestDTO updateEmailRequestDTO = new UpdateEmailRequestDTO(expectedNewEmail);

        assertEquals(expectedNewEmail, updateEmailRequestDTO.getNewEmail());
    }

    @Test
    void testUpdateNameRequestDTOSetters() {
        UpdateEmailRequestDTO updateEmailRequestDTO = new UpdateEmailRequestDTO();

        updateEmailRequestDTO.setNewEmail("new_email@example.com");

        assertEquals("new_email@example.com", updateEmailRequestDTO.getNewEmail());
    }
}
