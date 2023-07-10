package com.kenzie.appserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.ClassAttendanceCreateRequest;
import com.kenzie.appserver.controller.model.ClassAttendanceUpdateRequest;
//import com.kenzie.appserver.controller.model.ExampleCreateRequest;
import com.kenzie.appserver.repositories.model.ClassAttendanceRecord;
import com.kenzie.appserver.service.ClassAttendanceService;
import com.kenzie.appserver.service.model.ClassAttendance;
//import com.kenzie.appserver.service.model.Example;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@IntegrationTest
class ClassAttendanceControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    ClassAttendanceService classAttendanceService;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void getById_Exists() throws Exception {

        String userId = mockNeat.strings().valStr();
        String classId = mockNeat.strings().valStr();
        String attendanceStatus = mockNeat.strings().valStr();

        ClassAttendance classAttendance = new ClassAttendance(userId, classId,attendanceStatus);

        ClassAttendance persistedClassAttendance = classAttendanceService.addNewClassAttendance(classAttendance);

        mvc.perform(get("/classAttendance/{userId}/{classid}", persistedClassAttendance.getUserId(),persistedClassAttendance.getClassId(),
                        persistedClassAttendance.getAttendanceStatus())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("userId")
                        .value(is(userId)))
                .andExpect(jsonPath("classId")
                        .value(is(classId)))
                .andExpect(jsonPath("classAttendance")
                        .value(is(attendanceStatus)))
                .andExpect(status().isOk());
    }
    @Test
    public void getAll() throws Exception {

        String userId = "testUser1";
        String classId = mockNeat.strings().valStr();
        String attendanceStatus = mockNeat.strings().valStr();

        String userId2 = "testUser2";
        String classId2 = mockNeat.strings().valStr();
        String attendanceStatus2 = mockNeat.strings().valStr();

        ClassAttendance classAttendance = new ClassAttendance(userId, classId,attendanceStatus);
        classAttendanceService.addNewClassAttendance(classAttendance);

        ClassAttendance classAttendance2 = new ClassAttendance(userId2, classId2,attendanceStatus2);
        classAttendanceService.addNewClassAttendance(classAttendance2);

        MvcResult result =  mvc.perform(get("/classAttendance/all")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<String> userIds = JsonPath.read(jsonResponse, "$[*].userId");

        Assertions.assertTrue(userIds.contains(userId));
        Assertions.assertTrue(userIds.contains(userId2));
    }

    @Test
    public void createClassAttendance_CreateSuccessful() throws Exception {

        String userId = UUID.randomUUID().toString();
        String classId = UUID.randomUUID().toString();
        String attendanceStatus = "attending";

        ClassAttendanceCreateRequest classAttendanceCreateRequest = new ClassAttendanceCreateRequest();
        classAttendanceCreateRequest.setUserId(userId);
        classAttendanceCreateRequest.setClassId(classId);
        classAttendanceCreateRequest.setClassAttendance(attendanceStatus);

        mapper.registerModule(new JavaTimeModule());

        mvc.perform(post("/classAttendance")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(classAttendanceCreateRequest)))
                .andExpect(jsonPath("userId")
                        .value(is(userId)))
                .andExpect(jsonPath("classId")
                        .value(is(classId)))
                .andExpect(jsonPath("classAttendance")
                        .value(is(attendanceStatus)))
                .andExpect(status().isOk());
    }

    @Test
    public void updateClassAttendance_IsSuccessful() throws Exception {
        String userId = UUID.randomUUID().toString();
        String classId = UUID.randomUUID().toString();
        String attendanceStatus = "attending";
        String updatedAttendanceStatus = "changed";

        ClassAttendanceCreateRequest classAttendanceCreateRequest = new ClassAttendanceCreateRequest();
        classAttendanceCreateRequest.setUserId(userId);
        classAttendanceCreateRequest.setClassId(classId);
        classAttendanceCreateRequest.setClassAttendance(attendanceStatus);

        ClassAttendanceUpdateRequest classAttendanceUpdateRequest = new ClassAttendanceUpdateRequest();
        classAttendanceUpdateRequest.setUserId(classAttendanceCreateRequest.getUserId());
        classAttendanceUpdateRequest.setClassId(classAttendanceCreateRequest.getClassId());
        classAttendanceUpdateRequest.setClassAttendance(updatedAttendanceStatus);

        mapper.registerModule(new JavaTimeModule());

        MvcResult result = mvc.perform(post("/classAttendance")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(classAttendanceCreateRequest)))
                .andReturn();

        mvc.perform(put("/classAttendance")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(classAttendanceUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("userId")
                        .value(is(userId)))
                .andExpect(jsonPath("classId")
                        .value(is(classId)))
                .andExpect(jsonPath("classAttendance")
                        .value(is(updatedAttendanceStatus)));
    }

}