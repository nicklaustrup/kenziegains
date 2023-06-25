package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.ClassAttendanceCreateRequest;
import com.kenzie.appserver.controller.model.ClassAttendanceResponse;
import com.kenzie.appserver.controller.model.ExampleCreateRequest;
import com.kenzie.appserver.controller.model.ExampleResponse;
import com.kenzie.appserver.repositories.model.ClassAttendanceCompositeId;
import com.kenzie.appserver.service.ClassAttendanceService;
import com.kenzie.appserver.service.ExampleService;
import com.kenzie.appserver.service.InstructorLeadClassService;
import com.kenzie.appserver.service.UserService;
import com.kenzie.appserver.service.model.ClassAttendance;
import com.kenzie.appserver.service.model.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{userId}_{classid}")
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
}
