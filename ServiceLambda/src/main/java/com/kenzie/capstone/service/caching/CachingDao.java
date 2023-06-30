package com.kenzie.capstone.service.caching;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.kenzie.capstone.service.dao.CachingDaoInterface;
import com.kenzie.capstone.service.dao.NonCachingDao;
import com.kenzie.capstone.service.model.ClassAttendanceRecord;
import com.kenzie.capstone.service.model.ExampleRecord;
import com.kenzie.capstone.service.model.InstructorLeadClassRecord;

import javax.inject.Inject;
import java.io.IOException;
import java.sql.Ref;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class CachingDao implements CachingDaoInterface {
    private static final int CACHING_READ_TTL = 60*60;
    private static final String CACHING_KEY = "CachingKey::%s";

    private final CacheClient cacheClient;
    private final NonCachingDao nonCachingDao;

    @Inject

    public CachingDao(CacheClient cacheClient, NonCachingDao nonCachingDao) {
        this.cacheClient = cacheClient;
        this.nonCachingDao = nonCachingDao;
    }

    @Override
    public ClassAttendanceRecord addRecord(ClassAttendanceRecord record){
        cacheClient.invalidate(String.format(CACHING_KEY, record.getUserId()));
        return  nonCachingDao.addRecord(record);
    }

    @Override
    public List<ClassAttendanceRecord> getUserId(String userId){
        Json json = new Json();
        return cacheClient.getValue(String.format(CACHING_KEY, userId))
                .map(value -> this.fromJson(value))
                .orElseGet(() -> {
                    List<ClassAttendanceRecord> daoValue = nonCachingDao.getexampleId(userId);
                    this.addToCache(daoValue, userId);
                    return daoValue;
                });
    }

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
        return gson.fromJson(json, new TYpeToken<ArrayList<ClassAttendanceRecord>>() { }.gettype());
    }

    @Override
    public boolean setClassAttendance(String attendanceStatus){
        return attendanceStatus;
    }

    private void addToCache(List<ClassAttendanceRecord> records, String userId){
        cacheClient.setValue(
                String.format(CACHING_KEY, userId),
                CACHING_READ_TTL,
                gson.toJson(records)
        );
    }
}