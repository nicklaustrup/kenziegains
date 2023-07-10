package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheInstructorLeadClass;
import com.kenzie.appserver.repositories.InstructorLeadClassRepository;
import com.kenzie.appserver.repositories.model.InstructorLeadClassRecord;
import com.kenzie.appserver.service.model.InstructorLeadClass;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.InstructorLeadClassData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class InstructorLeadClassServiceTest {
    private InstructorLeadClassRepository instructorLeadClassRepository;
    private InstructorLeadClassService instructorLeadClassService;
    private LambdaServiceClient lambdaServiceClient;
    private CacheInstructorLeadClass cacheInstructorLeadClass;

    @BeforeEach
    void setup() {
        instructorLeadClassRepository = mock(InstructorLeadClassRepository.class);
        lambdaServiceClient = mock(LambdaServiceClient.class);
        cacheInstructorLeadClass = mock(CacheInstructorLeadClass.class);
        instructorLeadClassService = new InstructorLeadClassService(instructorLeadClassRepository, lambdaServiceClient, cacheInstructorLeadClass);
    }
    /** ------------------------------------------------------------------------
     *  instructorLeadClassService.findById
     *  ------------------------------------------------------------------------ **/

    @Test
    void findById() {
        // GIVEN
        String classId = "classId";
        InstructorLeadClassData instructorLeadClassData = new InstructorLeadClassData
                (classId, "name", "description", "type",
                        "userId", 10, LocalDateTime.now().toString(), true);
        when(lambdaServiceClient.getInstructorLeadClassData(classId)).thenReturn(instructorLeadClassData);

        // WHEN
        InstructorLeadClass result = instructorLeadClassService.findById(classId);

        // THEN
        assertEquals(classId, result.getClassId());
        assertEquals("name", result.getName());
        assertEquals("description", result.getDescription());
        assertEquals("type", result.getClassType());
        assertEquals("userId", result.getUserId());
        assertEquals(10, result.getClassCapacity());

        verify(lambdaServiceClient).getInstructorLeadClassData(classId);
        verify(instructorLeadClassRepository, never()).findById(anyString());
    }

    @Test
    void findById_invalid() {
        // GIVEN
        String classId = "classId";
        when(lambdaServiceClient.getInstructorLeadClassData(classId)).thenReturn(null);

        // WHEN
        InstructorLeadClass result = instructorLeadClassService.findById(classId);

        // THEN
        assertNull(result);
        verify(lambdaServiceClient).getInstructorLeadClassData(classId);
    }
    @Test
    void findAll() {
        // GIVEN
        List<InstructorLeadClassData> instructorLeadClassDataList = new ArrayList<>();
        instructorLeadClassDataList.add(new InstructorLeadClassData("classId1", "name1", "description1",
                "type1", "userId1", 10, LocalDateTime.now().toString(), true));
        instructorLeadClassDataList.add(new InstructorLeadClassData("classId2", "name2", "description2",
                "type2", "userId2", 20, LocalDateTime.now().toString(), false));
        when(lambdaServiceClient.getAllInstructorLeadClassData()).thenReturn(instructorLeadClassDataList);

        // WHEN
        List<InstructorLeadClassRecord> result = instructorLeadClassService.findAll();

        // THEN
        assertEquals(2, result.size());
        verify(lambdaServiceClient).getAllInstructorLeadClassData();
        verify(instructorLeadClassRepository, never()).findAll();
    }
    @Test
    void addNewInstructorLeadClass() {
        // GIVEN
        String classId = "classId";
        String name = "name";
        String description = "description";
        String classType = "type";
        String userId = "userId";
        int classCapacity = 10;
        LocalDateTime dateTime = LocalDateTime.now();
        boolean status = true;
        InstructorLeadClassData instructorLeadClassData = new InstructorLeadClassData(classId, name, description, classType, userId, classCapacity, dateTime.toString(), status);
        when(lambdaServiceClient.setInstructorLeadClassData(name, description, classType, userId, classCapacity, dateTime, status)).thenReturn(instructorLeadClassData);

        // WHEN
        InstructorLeadClass result = instructorLeadClassService.addNewInstructorLeadClass(name, description, classType, userId, classCapacity, dateTime, status);

        // THEN
        assertEquals(classId, result.getClassId());
        assertEquals(name, result.getName());
        assertEquals(description, result.getDescription());
        assertEquals(classType, result.getClassType());
        assertEquals(userId, result.getUserId());
        assertEquals(classCapacity, result.getClassCapacity());
        assertEquals(dateTime, result.getDateTime());
        assertEquals(status, result.isStatus());
        verify(lambdaServiceClient).setInstructorLeadClassData(name, description, classType, userId, classCapacity, dateTime, status);
    }

    @Test
    void update_WithExistingData() {
        // GIVEN
        InstructorLeadClass instructorLeadClass = new InstructorLeadClass("classId", "name", "description",
                "type", "userId", 10, LocalDateTime.now(), true);
        when(lambdaServiceClient.getInstructorLeadClassData("classId")).
                thenReturn(new InstructorLeadClassData("classId", "oldName", "oldDescription",
                        "oldType", "oldUserId", 20, LocalDateTime.now().minusDays(1).toString(), false));

        // WHEN
        instructorLeadClassService.updateInstructorLeadClass(instructorLeadClass);

        // THEN
        verify(lambdaServiceClient).getInstructorLeadClassData("classId");
        verify(lambdaServiceClient).updateInstructorLeadClassData("classId", "name", "description",
                "type", "userId", 10, instructorLeadClass.getDateTime(), true);
        verify(instructorLeadClassRepository, never()).save(any(InstructorLeadClassRecord.class));
    }

    @Test
    void update_WithNonExistingData() {
        // GIVEN
        InstructorLeadClass instructorLeadClass = new InstructorLeadClass("classId", "name", "description",
                "type", "userId", 10, LocalDateTime.now(), true);
        when(lambdaServiceClient.getInstructorLeadClassData("classId")).thenReturn(null);

        // WHEN
        instructorLeadClassService.updateInstructorLeadClass(instructorLeadClass);

        // THEN
        verify(lambdaServiceClient).getInstructorLeadClassData("classId");
        verify(lambdaServiceClient, never()).updateInstructorLeadClassData(anyString(), anyString(), anyString(), anyString(), anyString(), anyInt(), any(LocalDateTime.class), anyBoolean());
    }
}
