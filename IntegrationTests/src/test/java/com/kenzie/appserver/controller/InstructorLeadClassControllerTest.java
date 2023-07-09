package com.kenzie.appserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.InstructorLeadClassCreateRequest;
import com.kenzie.appserver.controller.model.InstructorLeadClassUpdateRequest;

import com.kenzie.appserver.service.InstructorLeadClassService;

import com.kenzie.appserver.service.model.InstructorLeadClass;

import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@IntegrationTest
class InstructorLeadClassControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    InstructorLeadClassService instructorLeadClassService;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void getById_Exists() throws Exception {

        String name = mockNeat.strings().valStr();
        String description = mockNeat.strings().valStr();
        String classType = mockNeat.strings().valStr();
        String userId = mockNeat.strings().valStr();
        int classCapacity = mockNeat.ints().get();
        LocalDateTime dateTime = LocalDateTime.now();
        boolean status = true;

        InstructorLeadClass persistedInstructorLeadClass = instructorLeadClassService.addNewInstructorLeadClass(name,
                description, classType, userId, classCapacity, dateTime, status);

        mvc.perform(get("/instructorleadclass/{classid}",persistedInstructorLeadClass.getClassId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("userId")
                        .value(is(userId)))
                .andExpect(jsonPath("name")
                        .value(is(name)))
                .andExpect(jsonPath("classType")
                        .value(is(classType)))
                .andExpect(status().isOk());
    }
    @Test
    public void getAll() throws Exception {

        String name = mockNeat.strings().valStr();
        String description = mockNeat.strings().valStr();
        String classType = mockNeat.strings().valStr();
        String userId = mockNeat.strings().valStr();
        int classCapacity = mockNeat.ints().get();
        LocalDateTime dateTime = LocalDateTime.now();
        boolean status = true;

        String name2 = mockNeat.strings().valStr();
        String description2 = mockNeat.strings().valStr();
        String classType2 = mockNeat.strings().valStr();
        String userId2 = mockNeat.strings().valStr();
        int classCapacity2 = mockNeat.ints().get();
        LocalDateTime dateTime2 = LocalDateTime.now();
        boolean status2 = true;

        instructorLeadClassService.addNewInstructorLeadClass(name, description, classType, userId, classCapacity, dateTime, status);
        instructorLeadClassService.addNewInstructorLeadClass(name2, description2, classType2, userId2, classCapacity2, dateTime2, status2);

        MvcResult result =  mvc.perform(get("/instructorleadclass/all")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<String> userIds = JsonPath.read(jsonResponse, "$[*].userId");

        List<String> expectedUserIds = Arrays.asList(userId, userId2);
        Assertions.assertTrue(userIds.contains(userId));
        Assertions.assertTrue(userIds.contains(userId2));
    }

    @Test
    public void createInstructorClass_CreateSuccessful() throws Exception {

        String name = "name";
        String description = "description";
        String classType = "type";
        String userId = "userId";
        int classCapacity = 10;
        LocalDateTime dateTime = LocalDateTime.now();
        boolean status = true;

        InstructorLeadClassCreateRequest instructorLeadClassCreateRequest = new InstructorLeadClassCreateRequest();
        instructorLeadClassCreateRequest.setUserId(userId);
        instructorLeadClassCreateRequest.setName(name);
        instructorLeadClassCreateRequest.setDescription(description);
        instructorLeadClassCreateRequest.setClassType(classType);
        instructorLeadClassCreateRequest.setClassCapacity(classCapacity);
        instructorLeadClassCreateRequest.setDateTime(dateTime);
        instructorLeadClassCreateRequest.setStatus(status);

        mapper.registerModule(new JavaTimeModule());

        mvc.perform(post("/instructorleadclass")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(instructorLeadClassCreateRequest)))
                .andExpect(jsonPath("userId")
                        .value(is(userId)))
                .andExpect(jsonPath("description")
                        .value(is(description)))
                .andExpect(jsonPath("classType")
                        .value(is(classType)))
                .andExpect(jsonPath("classCapacity")
                        .value(is(classCapacity)))
                .andExpect(jsonPath("status")
                        .value(is(status)))
                .andExpect(status().isOk());
    }

    @Test
    public void updateInstructorLeadClass_IsSuccessful() throws Exception {
        String name = "name";
        String description = "description";
        String classType = "type";
        String userId = "userId";
        int classCapacity = 10;
        LocalDateTime dateTime = LocalDateTime.now();
        boolean status = true;
        String updatedClassType = "changed";

        InstructorLeadClassCreateRequest instructorLeadClassCreateRequest = new InstructorLeadClassCreateRequest();
        instructorLeadClassCreateRequest.setUserId(userId);
        instructorLeadClassCreateRequest.setName(name);
        instructorLeadClassCreateRequest.setDescription(description);
        instructorLeadClassCreateRequest.setClassType(classType);
        instructorLeadClassCreateRequest.setClassCapacity(classCapacity);
        instructorLeadClassCreateRequest.setDateTime(dateTime);
        instructorLeadClassCreateRequest.setStatus(status);


        mapper.registerModule(new JavaTimeModule());

        MvcResult result = mvc.perform(post("/instructorleadclass")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(instructorLeadClassCreateRequest)))
                    .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        String classId = JsonPath.read(jsonResponse, "classId");

        InstructorLeadClassUpdateRequest instructorLeadClassUpdateRequest = new InstructorLeadClassUpdateRequest();
        instructorLeadClassUpdateRequest.setClassId(classId);
        instructorLeadClassUpdateRequest.setUserId(userId);
        instructorLeadClassUpdateRequest.setName(name);
        instructorLeadClassUpdateRequest.setDescription(description);
        instructorLeadClassUpdateRequest.setClassType(updatedClassType);
        instructorLeadClassUpdateRequest.setClassCapacity(classCapacity);
        instructorLeadClassUpdateRequest.setDateTime(dateTime);
        instructorLeadClassUpdateRequest.setStatus(status);

        mvc.perform(put("/instructorleadclass/update")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(instructorLeadClassUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("userId")
                        .value(is(userId)))
                .andExpect(jsonPath("name")
                        .value(is(name)))
                .andExpect(jsonPath("classType")
                        .value(is(updatedClassType)));
    }

}