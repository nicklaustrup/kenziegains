package com.kenzie.appserver.service;


import com.kenzie.appserver.config.CacheClassAttendance;
import com.kenzie.appserver.converter.LocalDateTimeConverter;
import com.kenzie.appserver.repositories.ClassAttendanceRepository;
import com.kenzie.appserver.repositories.model.ClassAttendanceCompositeId;
import com.kenzie.appserver.repositories.model.ClassAttendanceRecord;
import com.kenzie.appserver.repositories.model.InstructorLeadClassRecord;
import com.kenzie.appserver.service.model.ClassAttendance;
import com.kenzie.appserver.service.model.InstructorLeadClass;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.ClassAttendanceData;
import com.kenzie.capstone.service.model.InstructorLeadClassData;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClassAttendanceService {
    private ClassAttendanceRepository classAttendanceRepository;
    private LambdaServiceClient lambdaServiceClient;
    private CacheClassAttendance cache;

    public ClassAttendanceService(ClassAttendanceRepository classAttendanceRepository, LambdaServiceClient lambdaServiceClient, CacheClassAttendance cache) {
        this.classAttendanceRepository = classAttendanceRepository;
        this.lambdaServiceClient = lambdaServiceClient;
        this.cache = cache;
    }

    public ClassAttendance findById(ClassAttendanceCompositeId compositeId) {
        ClassAttendance cachedclassAttendance = cache.get(compositeId.getClassId() + "/" + compositeId.getUserId());
        // Check if InstructorLeadClass is cached and return it if true
        if (cachedclassAttendance != null) {
            return cachedclassAttendance;
        }

        // Example getting data from the lambda
        ClassAttendanceData dataFromLambda = lambdaServiceClient.getClassAttendanceData(compositeId.getUserId(), compositeId.getUserId());

        // Example getting data from the local repository
        ClassAttendance dataFromDynamo = classAttendanceRepository
                .findById(compositeId)
                .map(classAttendance -> new ClassAttendance(
                        classAttendance.getUserId(),
                        classAttendance.getClassId(),
                        classAttendance.getAttendanceStatus()))
                .orElse(null);

        if (dataFromLambda!= null) {
            // if InstructorLeadClass found, cache it
            cache.add(dataFromLambda.getClassId() + "/" + dataFromLambda.getUserId(), dataFromDynamo);
        }
        return dataFromDynamo;
    }

    public ClassAttendance addNewClassAttendance(ClassAttendance classAttendance) {
        // sending data to the lambda
        ClassAttendanceData dataFromLambda = lambdaServiceClient.setClassAttendanceData(classAttendance.getUserId(),
                classAttendance.getClassId(), classAttendance.getAttendanceStatus());

         // sending data to the local repository
        ClassAttendanceRecord classAttendanceRecordLocal = new ClassAttendanceRecord();
        classAttendanceRecordLocal.setUserId(dataFromLambda.getUserId());
        classAttendanceRecordLocal.setClassId(dataFromLambda.getClassId());
        classAttendanceRecordLocal.setAttendanceStatus(dataFromLambda.getAttendanceStatus());
        classAttendanceRepository.save(classAttendanceRecordLocal);
        return classAttendance;
    }

    public List<ClassAttendanceRecord> findAll() {
        List<ClassAttendanceData> dataFromLambda = lambdaServiceClient.getAlLClassAttendanceData();
        List<ClassAttendanceRecord> classAttendanceRecords = new ArrayList<>();

        for(ClassAttendanceData classAttendanceData : dataFromLambda) {
            ClassAttendanceRecord classAttendanceRecord = new ClassAttendanceRecord();
            classAttendanceRecord.setClassId(classAttendanceData.getClassId());
            classAttendanceRecord.setUserId(classAttendanceData.getUserId());
            classAttendanceRecord.setAttendanceStatus(classAttendanceData.getAttendanceStatus());
            classAttendanceRecords.add(classAttendanceRecord);
        }
        return classAttendanceRecords;
    }

    public void updateClassAttendance(ClassAttendance classAttendance) {
        String userId = classAttendance.getUserId();
        String classId = classAttendance.getClassId();
        if (lambdaServiceClient.getClassAttendanceData(userId,classId) != null) {
            lambdaServiceClient.updateClassAttendance(userId,classId,classAttendance.getAttendanceStatus());
            cache.evict(classAttendance.getClassId() + "/" + classAttendance.getUserId());
        }
        ClassAttendanceRecord classAttendanceRecordLocal = new ClassAttendanceRecord();
        classAttendanceRecordLocal.setUserId(classAttendance.getUserId());
        classAttendanceRecordLocal.setClassId(classAttendance.getClassId());
        classAttendanceRecordLocal.setAttendanceStatus(classAttendance.getAttendanceStatus());
        classAttendanceRepository.save(classAttendanceRecordLocal);
    }
}
