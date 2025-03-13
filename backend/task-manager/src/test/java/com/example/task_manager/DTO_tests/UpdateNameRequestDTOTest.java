package com.example.task_manager.DTO_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.example.task_manager.DTO.UpdateNameRequestDTO;

public class UpdateNameRequestDTOTest {
    @Test
    void testUpdateNameRequestDTOConstructorAndGetters() {
        String expectedNewName = "Updated Admin Name";

        UpdateNameRequestDTO updateNameRequestDTO = new UpdateNameRequestDTO(expectedNewName);

        assertEquals(expectedNewName, updateNameRequestDTO.getNewName());
    }

    @Test
    void testUpdateNameRequestDTOSetters() {
        UpdateNameRequestDTO updateNameRequestDTO = new UpdateNameRequestDTO();

        updateNameRequestDTO.setNewName("New Admin Name");

        assertEquals("New Admin Name", updateNameRequestDTO.getNewName());
    }
}
