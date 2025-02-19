package com.example.task_manager.DTO_tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.task_manager.DTO.IsAssignedDTO;

public class IsAssignedDTOTest {

    @Test
    void testDTOConstructorAndGetters() {
        IsAssignedDTO dto = new IsAssignedDTO(1, 101, 1001, 201);

        assertEquals(1, dto.getIsAssignedId());
        assertEquals(101, dto.getTaskId());
        assertEquals(1001, dto.getTeamMemberId());
        assertEquals(201, dto.getTeamId());
    }

    @Test
    void testDTOSetters() {
        IsAssignedDTO dto = new IsAssignedDTO(1, 101, 1001, 201);

        dto.setIsAssignedId(2);
        dto.setTaskId(102);
        dto.setTeamMemberId(2002);
        dto.setTeamId(202);

        assertEquals(2, dto.getIsAssignedId());
        assertEquals(102, dto.getTaskId());
        assertEquals(2002, dto.getTeamMemberId());
        assertEquals(202, dto.getTeamId());
    }
}

