package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheClassAttendance;
import com.kenzie.appserver.repositories.ClassAttendanceRepository;
import com.kenzie.appserver.repositories.model.ClassAttendanceCompositeId;
import com.kenzie.appserver.repositories.model.ClassAttendanceRecord;
import com.kenzie.appserver.service.model.ClassAttendance;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.ClassAttendanceData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.*;

public class ClassAttendanceServiceTest {
    private ClassAttendanceRepository classAttendanceRepository;
    private ClassAttendanceService classAttendanceService;
    private LambdaServiceClient lambdaServiceClient;
    private CacheClassAttendance cacheClassAttendance;

    @BeforeEach
    void setup() {
        classAttendanceRepository = mock(ClassAttendanceRepository.class);
        lambdaServiceClient = mock(LambdaServiceClient.class);
        cacheClassAttendance = mock(CacheClassAttendance.class);
        classAttendanceService = new ClassAttendanceService(classAttendanceRepository, lambdaServiceClient, cacheClassAttendance);
    }
    /** ------------------------------------------------------------------------
     *  classAttendanceService.findById
     *  ------------------------------------------------------------------------ **/

    @Test
    void findById() {
        // GIVEN
        ClassAttendanceCompositeId compositeId = new ClassAttendanceCompositeId();
        compositeId.setUserId("userId");
        compositeId.setClassId("classId");

        ClassAttendanceRecord record = new ClassAttendanceRecord();
        record.setUserId("userId");
        record.setClassId("classId");
        record.setAttendanceStatus("present");

        // WHEN
        when(classAttendanceRepository.findById(compositeId)).thenReturn(Optional.of(record));
        ClassAttendance classAttendance = classAttendanceService.findById(compositeId);

        // THEN
        Assertions.assertNotNull(classAttendance, "The object is returned");
        Assertions.assertEquals(record.getClassId(), classAttendance.getClassId(), "The classId matches");
        Assertions.assertEquals(record.getUserId(), classAttendance.getUserId(), "The userId matches");
        Assertions.assertEquals(record.getAttendanceStatus(), classAttendance.getAttendanceStatus(), "The attendance status matches");

    }

    @Test
    void findById_invalid() {
        // GIVEN
        ClassAttendanceCompositeId compositeId = new ClassAttendanceCompositeId();
        compositeId.setUserId("userId");
        compositeId.setClassId("classId");

        // WHEN
        when(classAttendanceRepository.findById(compositeId)).thenReturn(Optional.empty());
        ClassAttendance classAttendance = classAttendanceService.findById(compositeId);

        // THEN
        Assertions.assertNull(classAttendance, "The example is null when not found");
    }
    @Test
    void addNewClassAttendance() {
        // GIVEN
        ClassAttendance classAttendance = new ClassAttendance("userId", "classId", "present");
        ClassAttendanceData classAttendanceData = new ClassAttendanceData("userId", "classId", "present");
        when(lambdaServiceClient.setClassAttendanceData("userId", "classId", "present")).thenReturn(classAttendanceData);

        // WHEN
        ClassAttendance result = classAttendanceService.addNewClassAttendance(classAttendance);

        // THEN
        Assertions.assertEquals(classAttendance, result);
        verify(lambdaServiceClient).setClassAttendanceData("userId", "classId", "present");
        verify(classAttendanceRepository).save(any(ClassAttendanceRecord.class));
    }

    @Test
    void findAll() {
        //GIVEN
        List<ClassAttendanceData> classAttendanceDataList = new ArrayList<>();
        classAttendanceDataList.add(new ClassAttendanceData("userId1", "classId1", "present"));
        classAttendanceDataList.add(new ClassAttendanceData("userId2", "classId2", "absent"));
        when(lambdaServiceClient.getAlLClassAttendanceData()).thenReturn(classAttendanceDataList);

        // WHEN
        List<ClassAttendanceRecord> result = classAttendanceService.findAll();

        // THEN
        Assertions.assertEquals(2, result.size());
        verify(lambdaServiceClient).getAlLClassAttendanceData();
    }
    @Test
    void update_WithExistingData() {
        // GIVEN
        ClassAttendance classAttendance = new ClassAttendance("userId", "classId", "present");
        when(lambdaServiceClient.getClassAttendanceData("userId", "classId"))
                .thenReturn(new ClassAttendanceData("userId", "classId", "absent"));

        // WHEN
        classAttendanceService.updateClassAttendance(classAttendance);

        // THEN
        verify(lambdaServiceClient).getClassAttendanceData("userId", "classId");
        verify(lambdaServiceClient).updateClassAttendance("userId", "classId", "present");
    }

    @Test
    void update_WithNonExistingData() {
        // GIVEN
        ClassAttendance classAttendance = new ClassAttendance("userId", "classId", "present");
        when(lambdaServiceClient.getClassAttendanceData("userId", "classId")).thenReturn(null);

        // WHEN
        classAttendanceService.updateClassAttendance(classAttendance);

        // THEN
        verify(lambdaServiceClient).getClassAttendanceData("userId", "classId");
        verify(lambdaServiceClient, never()).updateClassAttendance(anyString(), anyString(), anyString());
        verify(classAttendanceRepository).save(any(ClassAttendanceRecord.class));
    }
}
