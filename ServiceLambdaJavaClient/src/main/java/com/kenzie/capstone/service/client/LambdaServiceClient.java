package com.kenzie.capstone.service.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenzie.capstone.service.model.ExampleData;
import com.kenzie.capstone.service.model.InstructorLeadClassData;

import java.time.LocalDateTime;


public class LambdaServiceClient {

    private static final String GET_EXAMPLE_ENDPOINT = "example/{id}";
    private static final String SET_EXAMPLE_ENDPOINT = "example";
    private static final String GET_INSTRUCTORLEADCLASS_ENDPOINT = "instructorleadclass/{classid}";
    private static final String SET_INSTRUCTORLEADCLASS_ENDPOINT = "instructorleadclass";

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
    public InstructorLeadClassData setInstructorLeadClassData(String name, String description, String classType, String userId, int classCapacity, LocalDateTime dateTime, boolean status) {
        EndpointUtility endpointUtility = new EndpointUtility();

        //Serialization
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        InstructorLeadClassData instructorLeadClassData = new InstructorLeadClassData("temp_classId", name, description, classType, userId, classCapacity, dateTime, status);

        String response = endpointUtility.postEndpoint(SET_INSTRUCTORLEADCLASS_ENDPOINT, gson.toJson(instructorLeadClassData));
        InstructorLeadClassData instructorLeadClasData;
        try {
            instructorLeadClasData = mapper.readValue(response, InstructorLeadClassData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return instructorLeadClasData;
    }
}
