package com.kenzie.capstone.service.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.model.ExampleData;
import com.kenzie.capstone.service.model.UserData;
import com.kenzie.capstone.service.model.UserRecord;
import com.kenzie.capstone.service.model.ClassAttendanceData;
import com.kenzie.capstone.service.model.ClassAttendanceRecord;


public class LambdaServiceClient {

    private static final String GET_EXAMPLE_ENDPOINT = "example/{id}";
    private static final String SET_EXAMPLE_ENDPOINT = "example";
    private static final String GET_USER_ENDPOINT = "user/{username}";
    private static final String SET_USER_ENDPOINT = "user";
    private static final String SET_CLASS_ATTENDANCE_ENDPOINT = "classAttendance";

    private ObjectMapper mapper;

    public LambdaServiceClient() {
        this.mapper = new ObjectMapper();
    }

    public ExampleData getExampleData(String id) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(GET_EXAMPLE_ENDPOINT.replace("{id}", id));
        ExampleData exampleData;
        try {
            exampleData = mapper.readValue(response, ExampleData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return exampleData;
    }

    public ExampleData setExampleData(String data) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.postEndpoint(SET_EXAMPLE_ENDPOINT, data);
        ExampleData exampleData;
        try {
            exampleData = mapper.readValue(response, ExampleData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return exampleData;
    }

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
    /***************        CLASS ATTENDANCE         *********************/
    public ClassAttendanceData setClassAttendanceData(ClassAttendanceRecord data) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String request;
        try {
            request = mapper.writeValueAsString(data);
        } catch (JsonProcessingException e){
            throw new ApiGatewayException("Unable to serialize request " + e);
        }

        String response = endpointUtility.postEndpoint(SET_CLASS_ATTENDANCE_ENDPOINT, request);
        ClassAttendanceData classAttendanceData;
        try {
            classAttendanceData = mapper.readValue(response, ClassAttendanceData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return classAttendanceData;
    }

}
