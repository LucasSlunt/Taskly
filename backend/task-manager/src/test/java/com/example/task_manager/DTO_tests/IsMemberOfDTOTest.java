package com.example.task_manager.DTO_tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.task_manager.DTO.IsMemberOfDTO;

public class IsMemberOfDTOTest {

    @Test
    void testDTOConstructorAndGetters() {
        IsMemberOfDTO dto = new IsMemberOfDTO(1, 1001, 201);

        assertEquals(1, dto.getIsMemberOfId());
        assertEquals(1001, dto.getTeamMemberId());
        assertEquals(201, dto.getTeamId());
    }

    @Test
    void testDTOSetters() {
        IsMemberOfDTO dto = new IsMemberOfDTO(1, 1001, 201);

        dto.setIsMemberOfId(2);
        dto.setTeamMemberId(2002);
        dto.setTeamId(202);

        assertEquals(2, dto.getIsMemberOfId());
        assertEquals(2002, dto.getTeamMemberId());
        assertEquals(202, dto.getTeamId());
    }
}
