package com.kenzie.appserver.service;

import com.kenzie.appserver.converter.LocalDateTimeConverter;
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

        // Getting data from the lambda
        InstructorLeadClassData dataFromLambda = lambdaServiceClient.getInstructorLeadClassData(classId);
        LocalDateTimeConverter converter = new LocalDateTimeConverter();
        InstructorLeadClass instructorLeadClass = null;
        if (dataFromLambda!= null)
            instructorLeadClass = new InstructorLeadClass(dataFromLambda.getClassId(), dataFromLambda.getName(), dataFromLambda.getDescription(), dataFromLambda.getClassType(), dataFromLambda.getUserId(), dataFromLambda.getClassCapacity(), converter.unconvert(dataFromLambda.getDateTime()), dataFromLambda.isStatus());

        // Getting data from the local repository
//        InstructorLeadClass dataFromDynamo = instructorLeadClassRepository
//                .findById(classId)
//                .map(instructorLeadClass -> new InstructorLeadClass(instructorLeadClass.getClassId(), instructorLeadClass.getName(), instructorLeadClass.getDescription(), instructorLeadClass.getClassType(), instructorLeadClass.getUserId(), instructorLeadClass.getClassCapacity(), instructorLeadClass.getDateTime(), instructorLeadClass.isStatus()))
//                .orElse(null);
//        return dataFromDynamo;
        return instructorLeadClass;
    }

    public List<InstructorLeadClassRecord> findAll() {
        // Getting data from the lambda
//        List<InstructorLeadClassData> dataFromLambda = lambdaServiceClient.getAllInstructorLeadClassData();
//        List<InstructorLeadClassRecord> instructorLeadClassRecordList = new ArrayList<>();
//        LocalDateTimeConverter converter = new LocalDateTimeConverter();
//        for(InstructorLeadClassData instructorLeadClassData : dataFromLambda) {
//            InstructorLeadClassRecord instructorLeadClassRecord = new InstructorLeadClassRecord();
//            instructorLeadClassRecord.setClassId(instructorLeadClassData.getClassId());
//            instructorLeadClassRecord.setName(instructorLeadClassData.getName());
//            instructorLeadClassRecord.setDescription(instructorLeadClassData.getDescription());
//            instructorLeadClassRecord.setClassType(instructorLeadClassData.getClassType());
//            instructorLeadClassRecord.setClassCapacity(instructorLeadClassData.getClassCapacity());
//            instructorLeadClassRecord.setDateTime(converter.unconvert(instructorLeadClassData.getDateTime()));
//            instructorLeadClassRecord.setStatus(instructorLeadClassData.isStatus());
//            instructorLeadClassRecordList.add(instructorLeadClassRecord);
//        }
        // Getting data from the local repository
        List<InstructorLeadClassRecord> instructorLeadClassRecordList = (List<InstructorLeadClassRecord>) instructorLeadClassRepository
                .findAll();
        return instructorLeadClassRecordList;
    }
    public InstructorLeadClass addNewInstructorLeadClass(String name, String description, String classType, String userId, int classCapacity, LocalDateTime dateTime, boolean status) {

        // Sending data to the lambda
        InstructorLeadClassData dataFromLambda = lambdaServiceClient.setInstructorLeadClassData(name, description, classType, userId, classCapacity, dateTime, status);

        // Sending data to the local repository
        LocalDateTimeConverter converter = new LocalDateTimeConverter();
        InstructorLeadClassRecord instructorLeadClassRecord = new InstructorLeadClassRecord();
        instructorLeadClassRecord.setClassId(dataFromLambda.getClassId());
        instructorLeadClassRecord.setName(dataFromLambda.getName());
        instructorLeadClassRecord.setDescription(dataFromLambda.getDescription());
        instructorLeadClassRecord.setClassType(dataFromLambda.getClassType());
        instructorLeadClassRecord.setUserId(dataFromLambda.getUserId());
        instructorLeadClassRecord.setClassCapacity(dataFromLambda.getClassCapacity());
        instructorLeadClassRecord.setDateTime(converter.unconvert(dataFromLambda.getDateTime()));
        instructorLeadClassRecord.setStatus(dataFromLambda.isStatus());
        instructorLeadClassRepository.save(instructorLeadClassRecord);

        InstructorLeadClass instructorLeadClass = new InstructorLeadClass(dataFromLambda.getClassId(), name, description, classType, userId, classCapacity, dateTime, status);
        return instructorLeadClass;
    }

    public void updateInstructorLeadClass(InstructorLeadClass instructorLeadClass) {
        if (lambdaServiceClient.getInstructorLeadClassData(instructorLeadClass.getClassId()) != null) {
            InstructorLeadClassRecord instructorLeadClassRecord = new InstructorLeadClassRecord();
            instructorLeadClassRecord.setClassId(instructorLeadClass.getClassId());
            instructorLeadClassRecord.setName(instructorLeadClass.getName());
            instructorLeadClassRecord.setDescription(instructorLeadClass.getDescription());
            instructorLeadClassRecord.setClassType(instructorLeadClass.getClassType());
            instructorLeadClassRecord.setUserId(instructorLeadClass.getUserId());
            instructorLeadClassRecord.setClassCapacity(instructorLeadClass.getClassCapacity());
            instructorLeadClassRecord.setDateTime(instructorLeadClass.getDateTime());
            instructorLeadClassRecord.setStatus(instructorLeadClass.isStatus());
//            lambdaServiceClient.putAllInstructorLeadClassData(instructorLeadClassRecord);
        }

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
}
