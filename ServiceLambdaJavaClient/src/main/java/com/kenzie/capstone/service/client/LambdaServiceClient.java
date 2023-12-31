package com.kenzie.capstone.service.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//import com.kenzie.capstone.service.model.ExampleData;


import com.kenzie.capstone.service.model.UserData;
import com.kenzie.capstone.service.model.UserRecord;
import com.kenzie.capstone.service.model.ClassAttendanceData;
import com.kenzie.capstone.service.model.InstructorLeadClassData;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class LambdaServiceClient {

//    private static final String GET_EXAMPLE_ENDPOINT = "example/{id}";
//    private static final String SET_EXAMPLE_ENDPOINT = "example";
    private static final String GET_USER_ENDPOINT = "user/{username}";
    private static final String GET_ALL_USERS_ENDPOINT = "user/all";
    private static final String SET_USER_ENDPOINT = "user";
    private static final String POST_UPDATE_USER_ENDPOINT = "user/update";
    private static final String GET_CLASS_ATTENDANCE_ENDPOINT = "classAttendance/{userId}/{classid}";
    private static final String GET_ALL_CLASS_ATTENDANCE_ENDPOINT = "classAttendance/all";
    private static final String SET_CLASS_ATTENDANCE_ENDPOINT = "classAttendance";
    private static final String PUT_CLASS_ATTENDANCE_ENDPOINT = "classAttendance/update";
    private static final String GET_INSTRUCTORLEADCLASS_ENDPOINT = "instructorleadclass/{classid}";
    private static final String SET_INSTRUCTORLEADCLASS_ENDPOINT = "instructorleadclass";
    private static final String GET_ALLINSTRUCTORLEADCLASS_ENDPOINT = "instructorleadclass/all";
    private static final String PUT_INSTRUCTORLEADCLASS_ENDPOINT = "instructorleadclass/update";

    private ObjectMapper mapper;

    public LambdaServiceClient() {
        this.mapper = new ObjectMapper();
    }

//    public ExampleData getExampleData(String id) {
//        EndpointUtility endpointUtility = new EndpointUtility();
//        String response = endpointUtility.getEndpoint(GET_EXAMPLE_ENDPOINT.replace("{id}", id));
//        ExampleData exampleData;
//        try {
//            exampleData = mapper.readValue(response, ExampleData.class);
//        } catch (Exception e) {
//            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
//        }
//        return exampleData;
//    }

//    public ExampleData setExampleData(String data) {
//        EndpointUtility endpointUtility = new EndpointUtility();
//        String response = endpointUtility.postEndpoint(SET_EXAMPLE_ENDPOINT, data);
//        ExampleData exampleData;
//        try {
//            exampleData = mapper.readValue(response, ExampleData.class);
//        } catch (Exception e) {
//            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
//        }
//        return exampleData;
//    }

    /***************        USERS         *********************/
    public UserData setUserData(UserRecord data) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String request;
        try {
            request = mapper.writeValueAsString(data);
        } catch (JsonProcessingException e){
            throw new ApiGatewayException("Unable to serialize request " + e);
        }

        String response = endpointUtility.postEndpoint(SET_USER_ENDPOINT, request);
        UserData userData;
        try {
            userData = mapper.readValue(response, UserData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return userData;
    }
    public UserData updateUserData(UserRecord data) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String request;
        try {
            request = mapper.writeValueAsString(data);
        } catch (JsonProcessingException e){
            throw new ApiGatewayException("Unable to serialize request " + e);
        }

        String response = endpointUtility.postEndpoint(POST_UPDATE_USER_ENDPOINT, request);
        UserData userData;
        try {
            userData = mapper.readValue(response, UserData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return userData;
    }

    public UserData getUserData(String username) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(GET_USER_ENDPOINT.replace("{username}", username));
        UserData userData;
        try {
            userData = mapper.readValue(response, UserData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return userData;
    }

    public List<UserData> getAllUsersData() {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(GET_ALL_USERS_ENDPOINT);
        List<UserData> userData;
        try {
            userData = mapper.readValue(response, new TypeReference<ArrayList<UserData>>() {});
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return userData;
    }
    /***************        CLASS ATTENDANCE         *********************/

    public ClassAttendanceData getClassAttendanceData(String userId, String classId) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint("classAttendance/"+userId+"/"+classId);
        ClassAttendanceData classAttendanceData;
        try {
            classAttendanceData = mapper.readValue(response, ClassAttendanceData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return classAttendanceData;
    }
    public ClassAttendanceData setClassAttendanceData(String userId, String classId, String attendanceStatus) {
        EndpointUtility endpointUtility = new EndpointUtility();
        //Serialization
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        ClassAttendanceData attendanceData = new ClassAttendanceData(userId, classId, attendanceStatus);


        String response = endpointUtility.postEndpoint(SET_CLASS_ATTENDANCE_ENDPOINT, gson.toJson(attendanceData));
        ClassAttendanceData classAttendanceData;
        try {
            classAttendanceData = mapper.readValue(response, ClassAttendanceData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return classAttendanceData;
    }

    public List<ClassAttendanceData> getAlLClassAttendanceData() {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(GET_ALL_CLASS_ATTENDANCE_ENDPOINT);
        List<ClassAttendanceData> classAttendanceData;
        try {
            classAttendanceData = mapper.readValue(response, new TypeReference<ArrayList<ClassAttendanceData>>() {});
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return classAttendanceData;
    }

    public ClassAttendanceData updateClassAttendance(String userId, String classId, String classAttendance) {
        EndpointUtility endpointUtility = new EndpointUtility();

        //Serialization
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        ClassAttendanceData classAttendanceData = new ClassAttendanceData(userId, classId, classAttendance);

        String response = endpointUtility.postEndpoint(PUT_CLASS_ATTENDANCE_ENDPOINT, gson.toJson(classAttendanceData));
        ClassAttendanceData attendanceData;
        try {
            attendanceData = mapper.readValue(response, ClassAttendanceData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return attendanceData;
    }
  /***************        INSTRUCTOR LEAD CLASS         *********************/
    public InstructorLeadClassData getInstructorLeadClassData(String classId) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(GET_INSTRUCTORLEADCLASS_ENDPOINT.replace("{classid}", classId));
        InstructorLeadClassData instructorLeadClassData;
        try {
            instructorLeadClassData = mapper.readValue(response, InstructorLeadClassData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return instructorLeadClassData;
    }
    public List<InstructorLeadClassData> getAllInstructorLeadClassData() {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(GET_ALLINSTRUCTORLEADCLASS_ENDPOINT);
        List<InstructorLeadClassData> instructorLeadClassDataList;
        try {
            instructorLeadClassDataList = mapper.readValue(response, mapper.getTypeFactory().constructCollectionType(List.class, InstructorLeadClassData.class));
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return instructorLeadClassDataList;
    }
    public InstructorLeadClassData setInstructorLeadClassData(String name, String description, String classType, String userId, int classCapacity, LocalDateTime dateTime, boolean status) {
        EndpointUtility endpointUtility = new EndpointUtility();

        //Serialization
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        InstructorLeadClassData instructorLeadClassData = new InstructorLeadClassData("temp_classId", name, description, classType, userId, classCapacity, dateTime.toString(), status);

        String response = endpointUtility.postEndpoint(SET_INSTRUCTORLEADCLASS_ENDPOINT, gson.toJson(instructorLeadClassData));
        InstructorLeadClassData instructorLeadClasData;
        try {
            instructorLeadClasData = mapper.readValue(response, InstructorLeadClassData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return instructorLeadClasData;
    }
    public InstructorLeadClassData updateInstructorLeadClassData(String classId, String name, String description, String classType, String userId, int classCapacity, LocalDateTime dateTime, boolean status) {
        EndpointUtility endpointUtility = new EndpointUtility();

        //Serialization
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        InstructorLeadClassData instructorLeadClassData = new InstructorLeadClassData(classId, name, description, classType, userId, classCapacity, dateTime.toString(), status);

        String response = endpointUtility.postEndpoint(PUT_INSTRUCTORLEADCLASS_ENDPOINT, gson.toJson(instructorLeadClassData));
        InstructorLeadClassData instructorLeadClasData;
        try {
            instructorLeadClasData = mapper.readValue(response, InstructorLeadClassData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return instructorLeadClasData;
    }
}
