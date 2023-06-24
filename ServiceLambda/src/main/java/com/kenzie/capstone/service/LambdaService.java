package com.kenzie.capstone.service;

import com.kenzie.capstone.service.model.ExampleData;
import com.kenzie.capstone.service.dao.ExampleDao;
import com.kenzie.capstone.service.model.ExampleRecord;
import com.kenzie.capstone.service.model.InstructorLeadClassData;
import com.kenzie.capstone.service.model.InstructorLeadClassRecord;

import javax.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class LambdaService {
    //TODO Build this out

    private ExampleDao exampleDao;

    @Inject
    public LambdaService(ExampleDao exampleDao) {
        this.exampleDao = exampleDao;
    }

    public ExampleData getExampleData(String id) {
        List<ExampleRecord> records = exampleDao.getExampleData(id);
        if (records.size() > 0) {
            return new ExampleData(records.get(0).getId(), records.get(0).getData());
        }
        return null;
    }

    public ExampleData setExampleData(String data) {
        String id = UUID.randomUUID().toString();
        ExampleRecord record = exampleDao.setExampleData(id, data);
        return new ExampleData(id, data);
    }

    /***************        INSTRUCTOR LEAD CLASS         *********************/
    public InstructorLeadClassData getInstructorLeadClassData(String classId) {
        List<InstructorLeadClassRecord> records = exampleDao.getInstructorLeadClassData(classId);
        if (records.size() > 0) {
            return new InstructorLeadClassData(records.get(0).getClassId(), records.get(0).getName(), records.get(0).getDescription(), records.get(0).getClassType(), records.get(0).getUserId(), records.get(0).getClassCapacity(), records.get(0).getDateTime().toString(), records.get(0).isStatus());
        }
        return null;
    }

    public InstructorLeadClassData setInstructorLeadClassData(String name, String description, String classType, String userId, int classCapacity, String dateTime, boolean status) {
        String id = UUID.randomUUID().toString();
        InstructorLeadClassRecord record = exampleDao.setInstructorLeadClassData(id, name, description, classType, userId, classCapacity, dateTime, status);
        return new InstructorLeadClassData(id, name, description, classType, userId, classCapacity, dateTime.toString(), status);
    }
}
