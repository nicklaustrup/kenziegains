package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.ClassAttendanceCreateRequest;
import com.kenzie.appserver.controller.model.ClassAttendanceResponse;
import com.kenzie.appserver.controller.model.ExampleCreateRequest;
import com.kenzie.appserver.controller.model.ExampleResponse;
import com.kenzie.appserver.repositories.model.ClassAttendanceCompositeId;
import com.kenzie.appserver.service.ClassAttendanceService;
import com.kenzie.appserver.service.ExampleService;
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
  //  private ClassService classService;

    ClassAttendanceController(ClassAttendanceService classAttendanceService, UserService userService) {
        this.classAttendanceService = classAttendanceService;
        this.userService = userService;
    }

    @GetMapping("/{username}_{password}")
    public ResponseEntity<ClassAttendanceResponse> getUsersClasses(@PathVariable("id") String id) {

        ClassAttendanceCompositeId record = new ClassAttendanceCompositeId();
                record.setUserId(id);
                record.setClassId("xyz"); // WILL BE UPDATED ONCE CLASS IS SET

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
        ClassAttendance classAttendance = classAttendanceService.addNewClassAttendance(classAttendanceCreateRequest);

        ClassAttendanceResponse classAttendanceResponse = new ClassAttendanceResponse();
        classAttendanceResponse.setUserId(classAttendance.getUserId());
        classAttendanceResponse.setClassId(classAttendance.getClassId());
        classAttendanceResponse.setClassAttendance(classAttendance.getAttendanceStatus());

        return ResponseEntity.ok(classAttendanceResponse);
    }
}
