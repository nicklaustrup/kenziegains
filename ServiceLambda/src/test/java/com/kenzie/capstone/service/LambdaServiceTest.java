package com.kenzie.capstone.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.dao.ExampleDao;
import com.kenzie.capstone.service.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LambdaServiceTest {

    /** ------------------------------------------------------------------------
     *  expenseService.getExpenseById
     *  ------------------------------------------------------------------------ **/

    private ExampleDao exampleDao;
    private LambdaService lambdaService;

    @BeforeAll
    void setup() {
        this.exampleDao = mock(ExampleDao.class);
        this.lambdaService = new LambdaService(exampleDao);
    }

    @Test
    void setDataTest() {
        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> dataCaptor = ArgumentCaptor.forClass(String.class);

        // GIVEN
        String data = "somedata";

        // WHEN
        ExampleData response = this.lambdaService.setExampleData(data);

        // THEN
        verify(exampleDao, times(1)).setExampleData(idCaptor.capture(), dataCaptor.capture());

        assertNotNull(idCaptor.getValue(), "An ID is generated");
        assertEquals(data, dataCaptor.getValue(), "The data is saved");

        assertNotNull(response, "A response is returned");
        assertEquals(idCaptor.getValue(), response.getId(), "The response id should match");
        assertEquals(data, response.getData(), "The response data should match");
    }

    @Test
    void getDataTest() {
        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);

        // GIVEN
        String id = "fakeid";
        String data = "somedata";
        ExampleRecord record = new ExampleRecord();
        record.setId(id);
        record.setData(data);


        when(exampleDao.getExampleData(id)).thenReturn(Arrays.asList(record));

        // WHEN
        ExampleData response = this.lambdaService.getExampleData(id);

        // THEN
        verify(exampleDao, times(1)).getExampleData(idCaptor.capture());

        assertEquals(id, idCaptor.getValue(), "The correct id is used");

        assertNotNull(response, "A response is returned");
        assertEquals(id, response.getId(), "The response id should match");
        assertEquals(data, response.getData(), "The response data should match");
    }

    // Write additional tests here
    // InstructorLeadClass Class Tests
    @Test
    void getInstructorLeadClassData() {
        String classId = UUID.randomUUID().toString();
        InstructorLeadClassRecord record = new InstructorLeadClassRecord();
        record.setClassId(classId);
        record.setDateTime(LocalDateTime.now());
        List<InstructorLeadClassRecord> records = List.of(record);

        when(exampleDao.getInstructorLeadClassData(classId)).thenReturn(records);

        InstructorLeadClassData result = lambdaService.getInstructorLeadClassData(classId);

        assertNotNull(result);
        assertEquals(classId, result.getClassId());

        verify(exampleDao, times(1)).getInstructorLeadClassData(classId);
    }

    @Test
    void getInstructorLeadClassData_ReturnsNull() {
        String classId = UUID.randomUUID().toString();

        when(exampleDao.getInstructorLeadClassData(classId)).thenReturn(Collections.emptyList());

        InstructorLeadClassData result = lambdaService.getInstructorLeadClassData(classId);

        assertNull(result);
    }

    @Test
    void getAllInstructorLeadClassData() {
        InstructorLeadClassRecord record1 = new InstructorLeadClassRecord();
        record1.setClassId(UUID.randomUUID().toString());
        record1.setDateTime(LocalDateTime.now());
        InstructorLeadClassRecord record2 = new InstructorLeadClassRecord();
        record2.setClassId(UUID.randomUUID().toString());
        record2.setDateTime(LocalDateTime.now());
        List<InstructorLeadClassRecord> records = Arrays.asList(record1, record2);

        when(exampleDao.getAllInstructorLeadClassData()).thenReturn(records);

        List<InstructorLeadClassData> result = lambdaService.getAllInstructorLeadClassData();

        assertNotNull(result);
        assertEquals(records.size(), result.size());

    }

    @Test
    void getAllInstructorLeadClassData_ReturnsNull() {
        when(exampleDao.getAllInstructorLeadClassData()).thenReturn(Collections.emptyList());

        List<InstructorLeadClassData> response = lambdaService.getAllInstructorLeadClassData();

        assertNull(response);
    }

    @Test
    void setInstructorLeadClassData() {
        String classId = "classId";
        String name = "Class";
        String description = "description";
        String classType = "Type A";
        String userId = UUID.randomUUID().toString();
        int classCapacity = 20;
        String dateTime = LocalDateTime.now().toString();
        boolean status = true;


        InstructorLeadClassRecord record = new InstructorLeadClassRecord();
        record.setClassId(classId);
        record.setName(name);
        record.setDescription(description);
        record.setClassType(classType);
        record.setUserId(userId);
        record.setClassCapacity(classCapacity);
        record.setDateTime(LocalDateTime.parse(dateTime));
        record.setStatus(status);

        when(exampleDao.setInstructorLeadClassData(classId, name, description, classType, userId, classCapacity, dateTime, status)).thenReturn(record);

        InstructorLeadClassData result = lambdaService.setInstructorLeadClassData(name, description, classType, userId, classCapacity, dateTime, status);

        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(description, result.getDescription());
        assertEquals(classType, result.getClassType());
        assertEquals(userId, result.getUserId());
        assertEquals(classCapacity, result.getClassCapacity());
        assertEquals(dateTime, result.getDateTime());
        assertEquals(status, result.isStatus());

    }

    @Test
    void updateInstructorLeadClassData() {
        String classId = "classId";
        String name = "Class";
        String description = "description";
        String classType = "Type A";
        String userId = UUID.randomUUID().toString();
        int classCapacity = 30;
        String dateTime = LocalDateTime.now().toString();
        boolean status = false;

        InstructorLeadClassRecord record = new InstructorLeadClassRecord();
        record.setClassId(classId);
        record.setName(name);
        record.setDescription(description);
        record.setClassType(classType);
        record.setUserId(userId);
        record.setClassCapacity(classCapacity);
        record.setDateTime(LocalDateTime.parse(dateTime));
        record.setStatus(status);

        when(exampleDao.updateInstructorLeadClassData(classId, name, description, classType, userId, classCapacity, dateTime, status)).thenReturn(record);

        InstructorLeadClassData result = lambdaService.updateInstructorLeadClassData(classId, name, description, classType, userId, classCapacity, dateTime, status);

        assertNotNull(result);
        assertEquals(classId, result.getClassId());
        assertEquals(name, result.getName());
        assertEquals(description, result.getDescription());
        assertEquals(classType, result.getClassType());
        assertEquals(userId, result.getUserId());
        assertEquals(classCapacity, result.getClassCapacity());
        assertEquals(dateTime, result.getDateTime());
        assertEquals(status, result.isStatus());

        verify(exampleDao, times(1)).updateInstructorLeadClassData(classId, name, description, classType, userId, classCapacity, dateTime, status);
    }
    // User Class tests
    @Test
    void getUserData() {
        String username = "username";
        UserRecord userRecord = new UserRecord();
        userRecord.setUsername(username);
        List<UserRecord> records = List.of(userRecord);

        when(exampleDao.getUserData(username)).thenReturn(records);

        UserData result = lambdaService.getUserData(username);

        assertNotNull(result);
        assertEquals(username, result.getUsername());

    }

    @Test
    void getUsersData_ReturnsNull() {
        String username = "username";
        when(exampleDao.getUserData(username)).thenReturn(Collections.emptyList());

        UserData response = lambdaService.getUserData(username);

        assertNull(response);
    }
    @Test
    void getAllUsersData() {
        UserRecord userRecord1 = new UserRecord();
        userRecord1.setUsername("username");
        UserRecord userRecord2 = new UserRecord();
        userRecord2.setUsername("username2");
        List<UserRecord> records = Arrays.asList(userRecord1, userRecord2);

        when(exampleDao.getAllUsersData()).thenReturn(records);

        List<UserData> result = lambdaService.getAllUsersData();

        assertNotNull(result);
        assertEquals(records.size(), result.size());

        verify(exampleDao, times(1)).getAllUsersData();
    }

    @Test
    void setUserData() throws JsonProcessingException {
        String json = "{\"userId\":\"userId\",\"firstName\":\"abc\",\"lastName\":\"xyz\",\"userType\":\"admin\",\"membership\":\"premium\",\"status\":\"active\",\"username\":\"username\",\"password\":\"password\"}";
        UserData userData = new ObjectMapper().readValue(json, UserData.class);

        UserRecord userRecord = new UserRecord();
        userRecord.setUsername(userData.getUsername());

        when(exampleDao.setUserData(any(UserData.class))).thenReturn(userRecord);

        UserData result = lambdaService.setUserData(json);

        assertNotNull(result);
        assertEquals(userData.getUsername(), result.getUsername());

        verify(exampleDao, times(1)).setUserData(any(UserData.class));
    }
    @Test
    void setUserData_InvalidJson() {
        String invalidJson = "invalid json";

        Exception exception = assertThrows(RuntimeException.class, () -> lambdaService.updateUserData(invalidJson));

        verify(exampleDao, never()).setUserData(any(UserData.class));
    }

    @Test
    void updateUserData() throws JsonProcessingException {
        String json = "{\"userId\":\"userId\",\"firstName\":\"abc\",\"lastName\":\"xyz\",\"userType\":\"admin\",\"membership\":\"premium\",\"status\":\"active\",\"username\":\"username\",\"password\":\"password\"}";
        UserData userData = new ObjectMapper().readValue(json, UserData.class);

        UserRecord userRecord = new UserRecord();
        userRecord.setUsername(userData.getUsername());

        when(exampleDao.updateUserData(any(UserData.class))).thenReturn(userRecord);

        UserData result = lambdaService.updateUserData(json);

        assertNotNull(result);
        assertEquals(userData.getUsername(), result.getUsername());

        verify(exampleDao, times(1)).updateUserData(any(UserData.class));
    }
    @Test
    void updateUserData_InvalidJson() {
        String invalidJson = "invalid json";

        Exception exception = assertThrows(RuntimeException.class, () -> lambdaService.updateUserData(invalidJson));

        verify(exampleDao, never()).updateUserData(any(UserData.class));
    }

    // Class Attendance Class Tests
    @Test
    void getClassAttendanceData() {

        String userId = UUID.randomUUID().toString();
        String classId = UUID.randomUUID().toString();
        ClassAttendanceRecord record = new ClassAttendanceRecord();
        record.setUserId(userId);
        record.setClassId(classId);
        List<ClassAttendanceRecord> records = List.of(record);

        when(exampleDao.getAttendanceData(userId, classId)).thenReturn(records);

        ClassAttendanceData result = lambdaService.getClassAttendanceData(userId, classId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(classId, result.getClassId());

        verify(exampleDao, times(1)).getAttendanceData(userId, classId);
    }

    @Test
    void getClassAttendanceData_ReturnsNull() {
        String userId = UUID.randomUUID().toString();
        String classId = UUID.randomUUID().toString();
        when(exampleDao.getAttendanceData(userId, classId)).thenReturn(Collections.emptyList());

        ClassAttendanceData response = lambdaService.getClassAttendanceData(userId, classId);

        assertNull(response);
    }

    @Test
    void getAllClassAttendanceData() {
        ClassAttendanceRecord record1 = new ClassAttendanceRecord();
        record1.setUserId(UUID.randomUUID().toString());
        record1.setClassId(UUID.randomUUID().toString());
        record1.setAttendanceStatus("Present");

        ClassAttendanceRecord record2 = new ClassAttendanceRecord();
        record2.setUserId(UUID.randomUUID().toString());
        record2.setClassId(UUID.randomUUID().toString());
        record2.setAttendanceStatus("Absent");

        List<ClassAttendanceRecord> records = Arrays.asList(record1, record2);

        when(exampleDao.getAllClassAttendanceData()).thenReturn(records);

        List<ClassAttendanceData> result = lambdaService.getAllClassAttendanceData();

        assertNotNull(result);
        assertEquals(records.size(), result.size());

        ClassAttendanceData data1 = result.get(0);
        assertEquals(record1.getUserId(), data1.getUserId());
        assertEquals(record1.getClassId(), data1.getClassId());
        assertEquals(record1.getAttendanceStatus(), data1.getAttendanceStatus());

        ClassAttendanceData data2 = result.get(1);
        assertEquals(record2.getUserId(), data2.getUserId());
        assertEquals(record2.getClassId(), data2.getClassId());
        assertEquals(record2.getAttendanceStatus(), data2.getAttendanceStatus());

        verify(exampleDao, times(1)).getAllClassAttendanceData();
    }
    @Test
    void getAllClassAttendanceData_ReturnsNull() {

        when(exampleDao.getAllClassAttendanceData()).thenReturn(Collections.emptyList());

        List<ClassAttendanceData> response = lambdaService.getAllClassAttendanceData();

        assertNull(response);
    }

    @Test
    void setClassAttendanceData() {
        String userId = UUID.randomUUID().toString();
        String classId = UUID.randomUUID().toString();
        String attendanceStatus = "present";
        ClassAttendanceRecord record = new ClassAttendanceRecord();
        record.setUserId(userId);
        record.setClassId(classId);
        record.setAttendanceStatus(attendanceStatus);

        when(exampleDao.setAttendanceData(userId, classId, attendanceStatus)).thenReturn(record);

        ClassAttendanceData result = lambdaService.setClassAttendanceData(userId, classId, attendanceStatus);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(classId, result.getClassId());
        assertEquals(attendanceStatus, result.getAttendanceStatus());

        verify(exampleDao, times(1)).setAttendanceData(userId, classId, attendanceStatus);
    }
    @Test
    void updateClassAttendanceData() {
        String userId = UUID.randomUUID().toString();
        String classId = UUID.randomUUID().toString();
        String attendanceStatus = "absent";
        ClassAttendanceRecord record = new ClassAttendanceRecord();
        record.setUserId(userId);
        record.setClassId(classId);
        record.setAttendanceStatus(attendanceStatus);

        when(exampleDao.updateClassAttendanceData(userId, classId, attendanceStatus)).thenReturn(record);

        ClassAttendanceData result = lambdaService.updateClassAttendanceData(userId, classId, attendanceStatus);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(classId, result.getClassId());
        assertEquals(attendanceStatus, result.getAttendanceStatus());

        verify(exampleDao, times(1)).updateClassAttendanceData(userId, classId, attendanceStatus);
    }
}