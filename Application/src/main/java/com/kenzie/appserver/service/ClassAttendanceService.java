package com.kenzie.appserver.service;


import com.kenzie.appserver.repositories.ClassAttendanceRepository;
import com.kenzie.appserver.repositories.model.ClassAttendanceCompositeId;
import com.kenzie.appserver.repositories.model.ClassAttendanceRecord;
import com.kenzie.appserver.repositories.model.InstructorLeadClassRecord;
import com.kenzie.appserver.service.model.ClassAttendance;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.ClassAttendanceData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassAttendanceService {
    private ClassAttendanceRepository classAttendanceRepository;
    private LambdaServiceClient lambdaServiceClient;

    public ClassAttendanceService(ClassAttendanceRepository classAttendanceRepository, LambdaServiceClient lambdaServiceClient) {
        this.classAttendanceRepository = classAttendanceRepository;
        this.lambdaServiceClient = lambdaServiceClient;
    }

    public ClassAttendance findById(ClassAttendanceCompositeId compositeId) {

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
        List<ClassAttendanceRecord> rsvpFromBackend = (List<ClassAttendanceRecord>) classAttendanceRepository
                .findAll();

        return rsvpFromBackend;
    }
}
