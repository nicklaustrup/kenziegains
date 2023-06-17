package com.kenzie.capstone.service.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.appserver.controller.model.UserResponse;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.capstone.service.model.ExampleData;
import com.kenzie.capstone.service.model.UserData;
import com.kenzie.appserver.controller.model.UserCreateRequest;


public class LambdaServiceClient {

    private static final String GET_EXAMPLE_ENDPOINT = "example/{id}";
    private static final String SET_EXAMPLE_ENDPOINT = "example";
    private static final String GET_USER_ENDPOINT = "user/{username}";
    private static final String SET_USER_ENDPOINT = "user";

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
    public UserResponse setUserData(UserRecord data) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String request;
        try {
            request = mapper.writeValueAsString(data);
        } catch (JsonProcessingException e){
            throw new ApiGatewayException("Unable to serialize request " + e);
        }

        String response = endpointUtility.postEndpoint(SET_USER_ENDPOINT, request);
        UserResponse userData;
        try {
            userData = mapper.readValue(response, UserResponse.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return userData;
    }

    public UserData getUserData(String id) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(GET_EXAMPLE_ENDPOINT.replace("{id}", id));
        UserData userData;
        try {
            userData = mapper.readValue(response, UserData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return userData;
    }
}
