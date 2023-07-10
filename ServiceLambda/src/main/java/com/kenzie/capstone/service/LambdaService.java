package com.kenzie.capstone.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.converter.LocalDateTimeConverter;
//import com.kenzie.capstone.service.caching.CachingDao;
import com.kenzie.capstone.service.model.*;
import com.kenzie.capstone.service.dao.ExampleDao;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class LambdaService {

//    private CachingDao exampleDao;
private ExampleDao exampleDao;
    private ObjectMapper mapper;

//    @Inject
//    public LambdaService(CachingDao exampleDao) {
//        this.exampleDao = exampleDao;
//        this.mapper = new ObjectMapper();
//    }
@Inject
public LambdaService(ExampleDao exampleDao) {
    this.exampleDao = exampleDao;
    this.mapper = new ObjectMapper();
}


//    public ExampleData getExampleData(String id) {
//        List<ExampleRecord> records = exampleDao.getExampleData(id);
//        if (records.size() > 0) {
//            return new ExampleData(records.get(0).getId(), records.get(0).getData());
//        }
//        return null;
//    }

//    public ExampleData setExampleData(String data) {
//        String id = UUID.randomUUID().toString();
//        ExampleRecord record = exampleDao.setExampleData(id, data);
//        return new ExampleData(id, data);
//    }


    /***************        INSTRUCTOR LEAD CLASS         *********************/
    public InstructorLeadClassData getInstructorLeadClassData(String classId) {
        List<InstructorLeadClassRecord> records = exampleDao.getInstructorLeadClassData(classId);
        if (records.size() > 0) {
            return new InstructorLeadClassData(records.get(0).getClassId(), records.get(0).getName(), records.get(0).getDescription(), records.get(0).getClassType(), records.get(0).getUserId(), records.get(0).getClassCapacity(), records.get(0).getDateTime().toString(), records.get(0).isStatus());
        }
        return null;
    }
    public List<InstructorLeadClassData> getAllInstructorLeadClassData() {
        List<InstructorLeadClassRecord> records = exampleDao.getAllInstructorLeadClassData();
        LocalDateTimeConverter converter = new LocalDateTimeConverter();
        if (records.size() > 0) {
            List<InstructorLeadClassData> instructorLeadClassDataList = new ArrayList<>();
            for(InstructorLeadClassRecord record : records) {
                InstructorLeadClassData instructorLeadClassData = new InstructorLeadClassData();
                instructorLeadClassData.setClassId(record.getClassId());
                instructorLeadClassData.setName(record.getName());
                instructorLeadClassData.setDescription(record.getDescription());
                instructorLeadClassData.setClassType(record.getClassType());
                instructorLeadClassData.setUserId(record.getUserId());
                instructorLeadClassData.setClassCapacity(record.getClassCapacity());
                instructorLeadClassData.setDateTime(converter.convert(record.getDateTime()));
                instructorLeadClassData.setStatus(record.isStatus());
                instructorLeadClassDataList.add(instructorLeadClassData);
            }
            return instructorLeadClassDataList;
        }
        return null;
    }

    public InstructorLeadClassData setInstructorLeadClassData(String name, String description, String classType, String userId, int classCapacity, String dateTime, boolean status) {
        String id = UUID.randomUUID().toString();
        InstructorLeadClassRecord record = exampleDao.setInstructorLeadClassData(id, name, description, classType, userId, classCapacity, dateTime, status);
        return new InstructorLeadClassData(id, name, description, classType, userId, classCapacity, dateTime.toString(), status);
    }

    public InstructorLeadClassData updateInstructorLeadClassData(String classId, String name, String description, String classType, String userId, int classCapacity, String dateTime, boolean status) {
        InstructorLeadClassRecord record = exampleDao.updateInstructorLeadClassData(classId, name, description, classType, userId, classCapacity, dateTime, status);
        return new InstructorLeadClassData(classId, name, description, classType, userId, classCapacity, dateTime.toString(), status);
    }

    /***************        USERS         *********************/
    public UserData getUserData(String username) {
        List<UserRecord> records = exampleDao.getUserData(username);
        if (records.size() > 0) {
            return new UserData(records.get(0).getUserId(), records.get(0).getFirstName(), records.get(0).getLastName(), records.get(0).getUserType(), records.get(0).getMembership(), records.get(0).getStatus(), records.get(0).getUsername(), records.get(0).getPassword());
        }
        return null;
    }
    public List<UserData> getAllUsersData() {
        List<UserRecord> records = exampleDao.getAllUsersData();

        return records.stream()
                .map(record -> new UserData(record.getUserId(),
                        record.getFirstName(),
                        record.getLastName(),
                        record.getUserType(),
                        record.getMembership(),
                        record.getStatus(),
                        record.getUsername(),
                        record.getPassword()))
                .collect(Collectors.toList());
    }

    public UserData setUserData(String data) {
        UserData userData;

        try{
            userData = mapper.readValue(data, UserData.class);
        } catch(JsonProcessingException e) {
            throw new RuntimeException("Unable to map deserialize JSON: " + e);
        }

        UserRecord record = exampleDao.setUserData(userData);
        return new UserData(record.getUserId(), record.getFirstName(), record.getLastName(), record.getUserType(), record.getMembership(), record.getStatus(), record.getUsername(), record.getPassword());
    }
    public UserData updateUserData(String data) {
        UserData userData;

        try{
            userData = mapper.readValue(data, UserData.class);
        } catch(JsonProcessingException e) {
            throw new RuntimeException("Unable to map deserialize JSON: " + e);
        }

        UserRecord record = exampleDao.updateUserData(userData);
        return new UserData(record.getUserId(), record.getFirstName(), record.getLastName(), record.getUserType(), record.getMembership(), record.getStatus(), record.getUsername(), record.getPassword());
    }

    /***************        CLASS ATTENDANCE CLASS         *********************/
    public ClassAttendanceData getClassAttendanceData(String userId, String classId) {
        List<ClassAttendanceRecord> records = exampleDao.getAttendanceData(userId, classId);
        if (records.size() > 0) {
            return new ClassAttendanceData(records.get(0).getUserId(), records.get(0).getClassId(), records.get(0).getAttendanceStatus());
        }
        return null;
    }

    public ClassAttendanceData setClassAttendanceData(String userId, String classId, String attendanceStatus) {
        ClassAttendanceRecord record = exampleDao.setAttendanceData(userId, classId, attendanceStatus);
        return new ClassAttendanceData(userId, classId, attendanceStatus);
    }

    public ClassAttendanceData updateClassAttendanceData(String userId, String classId, String attendanceStatus) {
        ClassAttendanceRecord record = exampleDao.updateClassAttendanceData(userId, classId, attendanceStatus);
        return new ClassAttendanceData(userId, classId, attendanceStatus);
    }
    public List<ClassAttendanceData> getAllClassAttendanceData() {
        List<ClassAttendanceRecord> records = exampleDao.getAllClassAttendanceData();

        if (records.size() > 0) {
            List<ClassAttendanceData> attendanceDataArrayList = new ArrayList<>();
            for(ClassAttendanceRecord record : records) {
                ClassAttendanceData classAttendanceData = new ClassAttendanceData();
                classAttendanceData.setClassId(record.getClassId());
                classAttendanceData.setUserId(record.getUserId());
                classAttendanceData.setAttendanceStatus(record.getAttendanceStatus());
                attendanceDataArrayList.add(classAttendanceData);
            }
            return attendanceDataArrayList;
        }
        return null;
    }
}
