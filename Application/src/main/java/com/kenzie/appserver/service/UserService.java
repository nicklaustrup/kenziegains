package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.UserRespository;
import com.kenzie.appserver.repositories.model.ExampleRecord;
import com.kenzie.appserver.service.model.Example;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.ExampleData;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRespository userRespository;
    private LambdaServiceClient lambdaServiceClient;

    public UserService(UserRespository userRespository, LambdaServiceClient lambdaServiceClient) {
        this.userRespository = userRespository;
        this.lambdaServiceClient = lambdaServiceClient;
    }

    public Example findById(String id) {

        // Example getting data from the lambda
        ExampleData dataFromLambda = lambdaServiceClient.getExampleData(id);

        // Example getting data from the local repository
        Example dataFromDynamo = userRespository
                .findById(id)
                .map(example -> new Example(example.getId(), example.getName()))
                .orElse(null);

        return dataFromDynamo;
    }

    public Example addNewExample(String name) {
        // Example sending data to the lambda
        ExampleData dataFromLambda = lambdaServiceClient.setExampleData(name);

        // Example sending data to the local repository
        ExampleRecord exampleRecord = new ExampleRecord();
        exampleRecord.setId(dataFromLambda.getId());
        exampleRecord.setName(dataFromLambda.getData());
        userRespository.save(exampleRecord);

        Example example = new Example(dataFromLambda.getId(), name);
        return example;
    }
}
