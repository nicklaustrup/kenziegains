package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.model.InstructorLeadClassRecord;
import com.kenzie.appserver.repositories.InstructorLeadClassRepository;
import com.kenzie.appserver.service.model.InstructorLeadClass;

import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.InstructorLeadClassData;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class InstructorLeadClassService {
    private InstructorLeadClassRepository instructorLeadClassRepository;
    private LambdaServiceClient lambdaServiceClient;

    public InstructorLeadClassService(InstructorLeadClassRepository instructorLeadClassRepository, LambdaServiceClient lambdaServiceClient) {
        this.instructorLeadClassRepository = instructorLeadClassRepository;
        this.lambdaServiceClient = lambdaServiceClient;
    }

    public InstructorLeadClass findById(String classId) {

        // Example getting data from the lambda
        InstructorLeadClassData dataFromLambda = lambdaServiceClient.getInstructorLeadClassData(classId);

        // Example getting data from the local repository
        InstructorLeadClass dataFromDynamo = instructorLeadClassRepository
                .findById(classId)
                .map(instructorLeadClass -> new InstructorLeadClass(instructorLeadClass.getClassId(), instructorLeadClass.getName(), instructorLeadClass.getDescription(), instructorLeadClass.getClassType(), instructorLeadClass.getUserId(), instructorLeadClass.getClassCapacity(), instructorLeadClass.getDateTime(), instructorLeadClass.isStatus()))
                .orElse(null);

        return dataFromDynamo;
    }

    public InstructorLeadClass addNewInstructorLeadClass(String name, String description, String classType, String userId, int classCapacity, LocalDateTime dateTime, boolean status) {

        // Example sending data to the lambda
        InstructorLeadClassData dataFromLambda = lambdaServiceClient.setInstructorLeadClassData(name, description, classType, userId, classCapacity, dateTime, status);

        // Example sending data to the local repository
        InstructorLeadClassRecord instructorLeadClassRecord = new InstructorLeadClassRecord();
        instructorLeadClassRecord.setClassId(dataFromLambda.getClassId());
        instructorLeadClassRecord.setName(dataFromLambda.getName());
        instructorLeadClassRecord.setDescription(dataFromLambda.getDescription());
        instructorLeadClassRecord.setClassType(dataFromLambda.getClassType());
        instructorLeadClassRecord.setUserId(dataFromLambda.getUserId());
        instructorLeadClassRecord.setClassCapacity(dataFromLambda.getClassCapacity());
        instructorLeadClassRecord.setDateTime(dataFromLambda.getDateTime());
        instructorLeadClassRecord.setStatus(dataFromLambda.isStatus());
        instructorLeadClassRepository.save(instructorLeadClassRecord);

        InstructorLeadClass instructorLeadClass = new InstructorLeadClass(dataFromLambda.getClassId(), name, description, classType, userId, classCapacity, dateTime, status);
        return instructorLeadClass;
    }

    public void updateInstructorLeadClass(InstructorLeadClass instructorLeadClass) {

        //TODO implement but with Lambda similar to the above
        if (instructorLeadClassRepository.existsById(instructorLeadClass.getClassId())) {
            InstructorLeadClassRecord instructorLeadClassRecord = new InstructorLeadClassRecord();
            instructorLeadClassRecord.setClassId(instructorLeadClass.getClassId());
            instructorLeadClassRecord.setName(instructorLeadClass.getName());
            instructorLeadClassRecord.setDescription(instructorLeadClass.getDescription());
            instructorLeadClassRecord.setClassType(instructorLeadClass.getClassType());
            instructorLeadClassRecord.setUserId(instructorLeadClass.getUserId());
            instructorLeadClassRecord.setClassCapacity(instructorLeadClass.getClassCapacity());
            instructorLeadClassRecord.setDateTime(instructorLeadClass.getDateTime());
            instructorLeadClassRecord.setStatus(instructorLeadClass.isStatus());
            instructorLeadClassRepository.save(instructorLeadClassRecord);
        }
    }

    public List<InstructorLeadClassRecord> findAll() {
        //TODO implement but with Lambda similar to the above
        List<InstructorLeadClassRecord> instructorLeadClassRecordsFromBackend = (List<InstructorLeadClassRecord>) instructorLeadClassRepository
                .findAll();
        return instructorLeadClassRecordsFromBackend;
    }
}
