package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.*;
import com.kenzie.appserver.repositories.model.ClassAttendanceCompositeId;
import com.kenzie.appserver.repositories.model.ClassAttendanceRecord;
import com.kenzie.appserver.repositories.model.InstructorLeadClassRecord;
import com.kenzie.appserver.service.ClassAttendanceService;
//import com.kenzie.appserver.service.ExampleService;
import com.kenzie.appserver.service.InstructorLeadClassService;
import com.kenzie.appserver.service.UserService;
import com.kenzie.appserver.service.model.ClassAttendance;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/classAttendance")
public class ClassAttendanceController {

    private ClassAttendanceService classAttendanceService;
    private UserService userService;
    private InstructorLeadClassService instructorLeadClassService;

    ClassAttendanceController(ClassAttendanceService classAttendanceService, UserService userService, InstructorLeadClassService instructorLeadClassService ) {
        this.classAttendanceService = classAttendanceService;
        this.userService = userService;
        this.instructorLeadClassService = instructorLeadClassService;
    }

    @GetMapping("/{userId}/{classid}")
    public ResponseEntity<ClassAttendanceResponse> getUsersClasses(@PathVariable("userId") String userId,
                                                                   @PathVariable("classid") String classId) {

        ClassAttendanceCompositeId record = new ClassAttendanceCompositeId();
                record.setUserId(userId);
                record.setClassId(classId);

        ClassAttendance classAttendance = classAttendanceService.findById(record);

        if (classAttendance == null) {
            return ResponseEntity.notFound().build();
        }

        ClassAttendanceResponse classAttendanceResponse = new ClassAttendanceResponse();
        classAttendanceResponse.setUserId(classAttendance.getUserId());
        classAttendanceResponse.setClassId(classAttendance.getClassId());
        classAttendanceResponse.setClassAttendance(classAttendance.getAttendanceStatus());
        return ResponseEntity.ok(classAttendanceResponse);
    }

    @PostMapping
    public ResponseEntity<ClassAttendanceResponse> addNewClassAttendance(@RequestBody ClassAttendanceCreateRequest classAttendanceCreateRequest) {
        ClassAttendance classAttendance = new ClassAttendance(classAttendanceCreateRequest.getUserId(),
                classAttendanceCreateRequest.getClassId(),
                classAttendanceCreateRequest.getClassAttendance());

        classAttendanceService.addNewClassAttendance(classAttendance);

        ClassAttendanceResponse classAttendanceResponse = new ClassAttendanceResponse();
        classAttendanceResponse.setUserId(classAttendance.getUserId());
        classAttendanceResponse.setClassId(classAttendance.getClassId());
        classAttendanceResponse.setClassAttendance(classAttendance.getAttendanceStatus());

        return ResponseEntity.ok(classAttendanceResponse);
    }
    @GetMapping("/all")
    public ResponseEntity<List<ClassAttendanceResponse>> getAllClassAttendance() {

        List<ClassAttendanceRecord> classAttendanceDataList  = classAttendanceService.findAll();

        if (classAttendanceDataList == null) {
            return ResponseEntity.notFound().build();
        }

        List<ClassAttendanceResponse> classAttendanceResponses = new ArrayList<>();

        for (ClassAttendanceRecord classAttendanceRecord: classAttendanceDataList) {
            ClassAttendanceResponse classAttendanceResponse = new ClassAttendanceResponse();
            classAttendanceResponse.setClassId(classAttendanceRecord.getClassId());
            classAttendanceResponse.setUserId(classAttendanceRecord.getUserId());
            classAttendanceResponse.setClassAttendance(classAttendanceRecord.getAttendanceStatus());

            classAttendanceResponses.add(classAttendanceResponse);
        }

        return ResponseEntity.ok(classAttendanceResponses);
    }

    @PutMapping
    public ResponseEntity<ClassAttendanceResponse> updateClassAttendance(@RequestBody ClassAttendanceUpdateRequest classAttendanceUpdateRequest) {

        ClassAttendance updatedClassAttendance = new ClassAttendance(classAttendanceUpdateRequest.getUserId(),
                classAttendanceUpdateRequest.getClassId(),  classAttendanceUpdateRequest.getClassAttendance());

        classAttendanceService.updateClassAttendance(updatedClassAttendance);

        ClassAttendanceResponse classAttendanceResponse = new ClassAttendanceResponse();
        classAttendanceResponse.setUserId(updatedClassAttendance.getUserId());
        classAttendanceResponse.setClassId(updatedClassAttendance.getClassId());
        classAttendanceResponse.setClassAttendance(updatedClassAttendance.getAttendanceStatus());
        return ResponseEntity.ok(classAttendanceResponse);
    }
}
