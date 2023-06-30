package com.kenzie.capstone.service.caching;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.kenzie.capstone.service.converter.LocalDateTimeConverter;
//import com.kenzie.capstone.service.dao.CachingDaoInterface;
import com.kenzie.capstone.service.dao.ExampleDao;
//import com.kenzie.capstone.service.dao.NonCachingDao;
import com.kenzie.capstone.service.model.*;

import javax.inject.Inject;
import java.io.IOException;
import java.sql.Ref;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CachingDao{
    private static final int CACHING_READ_TTL = 60*60;
    private static final String CACHING_KEY = "CachingKey::%s";
    private static final String ATTENDANCE_CACHING_KEY = "Key::classId::%s::userID::%s";

    private final CacheClient cacheClient;
    private final ExampleDao nonCachingDao;

    @Inject
    public CachingDao(CacheClient cacheClient, ExampleDao nonCachingDao) {
        this.cacheClient = cacheClient;
        this.nonCachingDao = nonCachingDao;
    }


//    public ClassAttendanceRecord addRecord(ClassAttendanceRecord record){
//        cacheClient.deleteValue(String.format(CACHING_KEY, record.getUserId()));
//        return  nonCachingDao(record);
//    }
//
//
//    public List<ClassAttendanceRecord> getUserId(String userId){
//        Json json = new Json();
//        return cacheClient.getValue(String.format(CACHING_KEY, userId))
//                .map(value -> this.fromJson(value))
//                .orElseGet(() -> {
//                    List<ClassAttendanceRecord> daoValue = nonCachingDao.getexampleId(userId);
//                    this.addToCache(daoValue, userId);
//                    return daoValue;
//                });
//    }

    GsonBuilder builder = new GsonBuilder().registerTypeAdapter(
            ZonedDateTime.class,
            new TypeAdapter<ZonedDateTime>() {
                @Override
                public void write(JsonWriter out, ZonedDateTime value) throws IOException{
                    out.value(value.toString());
                }
                @Override
                public ZonedDateTime read(JsonReader in) throws IOException{
                return ZonedDateTime.parse((in.nextString()));
                }
            }
    ).enableComplexMapKeySerialization();
    Gson gson = builder.create();
    private List<ClassAttendanceRecord> fromJson(String json){
        return gson.fromJson(json, new TypeToken<ArrayList<ClassAttendanceRecord>>() { }.getType());
    }


//    public boolean setClassAttendance(String attendanceStatus){
//        return attendanceStatus;
//    }

    private void addToCache(List<ClassAttendanceRecord> records, String userId){
        cacheClient.setValue(
                String.format(CACHING_KEY, userId),
                CACHING_READ_TTL,
                gson.toJson(records)
        );
    }

    public ExampleData storeExampleData(ExampleData exampleData) {
        return nonCachingDao.storeExampleData(exampleData);
    }

    public List<ExampleRecord> getExampleData(String id) {
        return nonCachingDao.getExampleData(id);
    }

    public ExampleRecord setExampleData(String id, String data) {
        return nonCachingDao.setExampleData(id,data);
    }

    //Set additional methods for each of the classes/tables
    public List<InstructorLeadClassRecord> getInstructorLeadClassData(String classId) {
        return nonCachingDao.getInstructorLeadClassData(classId);
    }

    public InstructorLeadClassRecord setInstructorLeadClassData(String classId, String name, String description, String classType,
                                                                String userId, int classCapacity, String dateTime, boolean status) {
        return nonCachingDao.setInstructorLeadClassData(classId,name,description,classType,userId,classCapacity,dateTime, status);
    }


    //User Tables
    public List<UserRecord> getUserData(String username) {
        return nonCachingDao.getUserData(username);
    }

    public UserRecord setUserData(UserData data) {
        return nonCachingDao.setUserData(data);
    }

    // Class Attendance Tables

    public List<ClassAttendanceRecord> getAttendanceData(String userId, String classId) {
        String cacheValue = cacheClient.getValue(String.format(ATTENDANCE_CACHING_KEY, classId,userId));
        if (cacheValue == null){
            List<ClassAttendanceRecord> records = nonCachingDao.getAttendanceData(userId,classId);
            cacheClient.setValue(String.format(ATTENDANCE_CACHING_KEY, classId,userId), CACHING_READ_TTL, gson.toJson(records));
            return records;
        }else {
        return fromJson(cacheValue);
        }
    }

    public ClassAttendanceRecord setAttendanceData(String userId, String classId, String attendanceStatus) {
        return nonCachingDao.setAttendanceData(userId,classId,attendanceStatus);
    }

}