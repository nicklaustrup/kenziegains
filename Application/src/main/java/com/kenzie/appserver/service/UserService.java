package com.kenzie.appserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.appserver.controller.model.UserCreateRequest;
import com.kenzie.appserver.controller.model.UserResponse;
import com.kenzie.appserver.repositories.UserRepository;
import com.kenzie.appserver.service.model.User;
import com.kenzie.capstone.service.client.ApiGatewayException;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.UserData;
import com.kenzie.capstone.service.model.UserRecord;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private UserRepository userRepository;
    private LambdaServiceClient lambdaServiceClient;

    public UserService(UserRepository userRepository, LambdaServiceClient lambdaServiceClient) {
        this.userRepository = userRepository;
        this.lambdaServiceClient = lambdaServiceClient;
    }

    public UserData findById(String username, String password) {

        // Example getting data from the lambda
        UserData dataFromLambda = lambdaServiceClient.getUserData(username);

        // Example getting data from the local repository
//        User dataFromDynamo = userRepository
//                .findById(username)
//                .map(user -> new User(user.getUserId(),
//                        user.getFirstName(),
//                        user.getLastName(),
//                        user.getUserType(),
//                        user.getMembership(),
//                        user.getStatus(),
//                        user.getUsername(),
//                        user.getPassword()))
//                .orElse(null);
        if (dataFromLambda !=null)
            if (!dataFromLambda.getPassword().equals(password)){
                throw new IllegalArgumentException("Invalid Password!");
            }

        return dataFromLambda;
    }
    public UserData updateUser(UserCreateRequest userCreateRequest) {

        // Example getting data from the lambda
        UserData dataFromLambda = lambdaServiceClient.getUserData(userCreateRequest.getUsername());


        // Example getting data from the local repository
//        User dataFromDynamo = userRepository
//                .findById(username)
//                .map(user -> new User(user.getUserId(),
//                        user.getFirstName(),
//                        user.getLastName(),
//                        user.getUserType(),
//                        user.getMembership(),
//                        user.getStatus(),
//                        user.getUsername(),
//                        user.getPassword()))
//                .orElse(null);

        UserRecord userRecord = new UserRecord();
        userRecord.setUserId(dataFromLambda.getUserId());
        userRecord.setFirstName(dataFromLambda.getFirstName());
        userRecord.setLastName(dataFromLambda.getLastName());
        userRecord.setUserType(dataFromLambda.getUserType());
        userRecord.setMembership(userCreateRequest.getMembership());
        userRecord.setStatus(dataFromLambda.getStatus());
        userRecord.setUsername(userCreateRequest.getUsername());
        userRecord.setPassword(userCreateRequest.getPassword());

        return lambdaServiceClient.updateUserData(userRecord);
    }
    public List<UserData> findAll() {
        return lambdaServiceClient.getAllUsersData();
    }

    public User addNewUser(UserCreateRequest user) {

        // Example sending data to the local repository
//        ExampleRecord exampleRecord = new ExampleRecord();
//        exampleRecord.setId(dataFromLambda.getId());
//        exampleRecord.setName(dataFromLambda.getData());
//        userRespository.save(exampleRecord);


        UserRecord userRecord = new UserRecord();
        userRecord.setUserId(UUID.randomUUID().toString());
        userRecord.setFirstName(user.getFirstName());
        userRecord.setLastName(user.getLastName());
        userRecord.setUserType(user.getUserType());
        userRecord.setMembership(user.getMembership());
        userRecord.setStatus("active");
        userRecord.setUsername(user.getUsername());
        userRecord.setPassword(user.getPassword());

//        userRepository.save(userRecord);
        // Example sending data to the lambda
        UserData dataFromLambda = lambdaServiceClient.setUserData(userRecord);


        User registeredUser = new User(dataFromLambda.getUserId(),
                dataFromLambda.getFirstName(),
                dataFromLambda.getLastName(),
                dataFromLambda.getUserType(),
                dataFromLambda.getMembership(),
                dataFromLambda.getStatus(),
                dataFromLambda.getUsername(),
                dataFromLambda.getPassword());

        return registeredUser;
    }
}
