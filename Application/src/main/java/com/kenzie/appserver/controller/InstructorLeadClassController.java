package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.InstructorLeadClassCreateRequest;
import com.kenzie.appserver.controller.model.InstructorLeadClassResponse;
import com.kenzie.appserver.controller.model.InstructorLeadClassUpdateRequest;
import com.kenzie.appserver.service.InstructorLeadClassService;

import com.kenzie.appserver.service.model.InstructorLeadClass;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/instructorleadclass")
public class InstructorLeadClassController {
    private InstructorLeadClassService instructorLeadClassService;

    public InstructorLeadClassController(InstructorLeadClassService instructorLeadClassService) {
        this.instructorLeadClassService = instructorLeadClassService;
    }

    @GetMapping("/{classid}")
    public ResponseEntity<InstructorLeadClassResponse> get(@PathVariable("classid") String classId) {

        InstructorLeadClass instructorLeadClass = instructorLeadClassService.findById(classId);
        if (instructorLeadClass == null) {
            return ResponseEntity.notFound().build();
        }

        InstructorLeadClassResponse instructorLeadClassResponse = new InstructorLeadClassResponse();
        instructorLeadClassResponse.setClassId(instructorLeadClass.getClassId());
        instructorLeadClassResponse.setName(instructorLeadClass.getName());
        instructorLeadClassResponse.setDescription(instructorLeadClass.getDescription());
        instructorLeadClassResponse.setClassType(instructorLeadClass.getClassType());
        instructorLeadClassResponse.setUserId(instructorLeadClass.getUserId());
        instructorLeadClassResponse.setClassCapacity(instructorLeadClass.getClassCapacity());
        instructorLeadClassResponse.setDateTime(instructorLeadClass.getDateTime());
        instructorLeadClassResponse.setStatus(instructorLeadClass.isStatus());
        return ResponseEntity.ok(instructorLeadClassResponse);
    }

    @PostMapping
    public ResponseEntity<InstructorLeadClassResponse> addNewInstructorLeadClass(@RequestBody InstructorLeadClassCreateRequest instructorLeadClassRequest) {
        InstructorLeadClass instructorLeadClass = instructorLeadClassService.addNewInstructorLeadClass(instructorLeadClassRequest.getName(), instructorLeadClassRequest.getDescription(), instructorLeadClassRequest.getClassType(), instructorLeadClassRequest.getUserId(), instructorLeadClassRequest.getClassCapacity(), instructorLeadClassRequest.getDateTime(), instructorLeadClassRequest.isStatus());

        InstructorLeadClassResponse instructorLeadClassResponse = new InstructorLeadClassResponse();
        instructorLeadClassResponse.setClassId(instructorLeadClass.getClassId());
        instructorLeadClassResponse.setName(instructorLeadClass.getName());
        instructorLeadClassResponse.setDescription(instructorLeadClass.getDescription());
        instructorLeadClassResponse.setClassType(instructorLeadClass.getClassType());
        instructorLeadClassResponse.setUserId(instructorLeadClass.getUserId());
        instructorLeadClassResponse.setClassCapacity(instructorLeadClass.getClassCapacity());
        instructorLeadClassResponse.setDateTime(instructorLeadClass.getDateTime());
        instructorLeadClassResponse.setStatus(instructorLeadClass.isStatus());
        return ResponseEntity.ok(instructorLeadClassResponse);
    }

    @PutMapping
    public ResponseEntity<InstructorLeadClassResponse> updateInstructorLeadClass(@RequestBody InstructorLeadClassUpdateRequest instructorLeadClassUpdateRequest) {

        InstructorLeadClass instructorLeadClass = new InstructorLeadClass(instructorLeadClassUpdateRequest.getClassId(), instructorLeadClassUpdateRequest.getName(), instructorLeadClassUpdateRequest.getDescription(), instructorLeadClassUpdateRequest.getClassType(), instructorLeadClassUpdateRequest.getUserId(), instructorLeadClassUpdateRequest.getClassCapacity(), instructorLeadClassUpdateRequest.getDateTime(), instructorLeadClassUpdateRequest.isStatus());
        instructorLeadClassService.updateInstructorLeadClass(instructorLeadClass);

        InstructorLeadClassResponse instructorLeadClassResponse = new InstructorLeadClassResponse();
        instructorLeadClassResponse.setClassId(instructorLeadClass.getClassId());
        instructorLeadClassResponse.setName(instructorLeadClass.getName());
        instructorLeadClassResponse.setDescription(instructorLeadClass.getDescription());
        instructorLeadClassResponse.setClassType(instructorLeadClass.getClassType());
        instructorLeadClassResponse.setUserId(instructorLeadClass.getUserId());
        instructorLeadClassResponse.setClassCapacity(instructorLeadClass.getClassCapacity());
        instructorLeadClassResponse.setDateTime(instructorLeadClass.getDateTime());
        instructorLeadClassResponse.setStatus(instructorLeadClass.isStatus());
        return ResponseEntity.ok(instructorLeadClassResponse);
    }
}
