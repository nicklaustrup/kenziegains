package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.UserCreateRequest;
import com.kenzie.appserver.controller.model.UserResponse;
import com.kenzie.appserver.repositories.UserRepository;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.appserver.service.model.User;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.UserData;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private UserRepository userRepository;
    private LambdaServiceClient lambdaServiceClient;

    public UserService(UserRepository userRepository, LambdaServiceClient lambdaServiceClient) {
        this.userRepository = userRepository;
        this.lambdaServiceClient = lambdaServiceClient;
    }

    public User findById(String username, String password) {

        // Example getting data from the lambda
//        UserData dataFromLambda = lambdaServiceClient.getUserData(id);


        // Example getting data from the local repository
        User dataFromDynamo = userRepository
                .findById(username)
                .map(user -> new User(user.getUserId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getUserType(),
                        user.getMembership(),
                        user.getStatus(),
                        user.getUsername(),
                        user.getPassword()))
                .orElse(null);

        if (!dataFromDynamo.getPassword().equals(password)){
            throw new IllegalArgumentException("Invalid Password!");
        }

        return dataFromDynamo;
    }

    public User addNewUser(UserCreateRequest user) {

        // Example sending data to the local repository
//        ExampleRecord exampleRecord = new ExampleRecord();
//        exampleRecord.setId(dataFromLambda.getId());
//        exampleRecord.setName(dataFromLambda.getData());
//        userRespository.save(exampleRecord);


        //TODO replace this with a lambda call
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
        UserResponse dataFromLambda = lambdaServiceClient.setUserData(userRecord);


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