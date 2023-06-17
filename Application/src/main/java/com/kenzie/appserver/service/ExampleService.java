package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.ExampleRepository;
import com.kenzie.appserver.repositories.model.ExampleRecord;
import com.kenzie.appserver.service.model.Example;

import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.ExampleData;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ExampleService {
    private ExampleRepository exampleRepository;
    private LambdaServiceClient lambdaServiceClient;

    public ExampleService(ExampleRepository exampleRepository, LambdaServiceClient lambdaServiceClient) {
        this.exampleRepository = exampleRepository;
        this.lambdaServiceClient = lambdaServiceClient;
    }

    public Example findById(String id) {

        // Example getting data from the lambda
        ExampleData dataFromLambda = lambdaServiceClient.getExampleData(id);

        Example fromLambda = new Example(dataFromLambda.getId(), dataFromLambda.getData());

        // Example getting data from the local repository
//        Example dataFromDynamo = exampleRepository
//                .findById(id)
//                .map(example -> new Example(example.getId(), example.getName()))
//                .orElse(null);

        return fromLambda;
    }

    public Example addNewExample(String name) {
        // Example sending data to the lambda
        ExampleData dataFromLambda = lambdaServiceClient.setExampleData(name);

//         Example sending data to the local repository
        ExampleRecord exampleRecord = new ExampleRecord();
        exampleRecord.setId(dataFromLambda.getId());
        exampleRecord.setName(dataFromLambda.getData());
//        userRespository.save(exampleRecord);

//        ExampleRecord exampleRecord = new ExampleRecord();
//        exampleRecord.setId(UUID.randomUUID().toString());
//        exampleRecord.setName(name);
        exampleRepository.save(exampleRecord);

        Example example = new Example(exampleRecord.getId(), name);
        return example;
    }
}
