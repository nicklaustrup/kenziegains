package com.kenzie.appserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.appserver.config.CacheUser;
import com.kenzie.appserver.controller.model.UserCreateRequest;
import com.kenzie.appserver.controller.model.UserResponse;
import com.kenzie.appserver.controller.model.UserUpdateRequest;
import com.kenzie.appserver.repositories.UserRepository;
import com.kenzie.appserver.service.model.InstructorLeadClass;
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
    private CacheUser cache;

    public UserService(UserRepository userRepository, LambdaServiceClient lambdaServiceClient, CacheUser cache) {
        this.userRepository = userRepository;
        this.lambdaServiceClient = lambdaServiceClient;
        this.cache = cache;
    }

    public UserData findById(String username, String password) {
        User cachedUser = cache.get(username);
        // Check if User is cached and return it if true
        if (cachedUser != null) {
            return new UserData(cachedUser.getUserId(), cachedUser.getFirstName(), cachedUser.getLastName(), cachedUser.getUserType(), cachedUser.getMembership(), cachedUser.getStatus(), cachedUser.getUsername(), cachedUser.getPassword());
        }

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
        if (dataFromLambda !=null) {
            if (!dataFromLambda.getPassword().equals(password)) {
                throw new IllegalArgumentException("Invalid Password!");
            }
            cache.add(dataFromLambda.getUsername(), new User(dataFromLambda.getUserId(), dataFromLambda.getFirstName(), dataFromLambda.getLastName(), dataFromLambda.getUserType(), dataFromLambda.getMembership(), dataFromLambda.getStatus(), dataFromLambda.getUsername(), dataFromLambda.getPassword()));
        }
        return dataFromLambda;
    }
    public UserData updateUser(UserUpdateRequest userUpdateRequest) {

        // Example getting data from the lambda
        User cachedUser = cache.get(userUpdateRequest.getUsername());
        UserData dataFromLambda = new UserData();
        if (cachedUser != null) {
            dataFromLambda.setFirstName(cachedUser.getFirstName());
            dataFromLambda.setLastName(cachedUser.getLastName());
            dataFromLambda.setMembership(cachedUser.getMembership());
            dataFromLambda.setUserType(cachedUser.getUserType());
            dataFromLambda.setUserId(cachedUser.getUserId());
            dataFromLambda.setStatus(cachedUser.getStatus());
            dataFromLambda.setUsername(cachedUser.getUsername());
            dataFromLambda.setPassword(cachedUser.getPassword());
            cache.evict(userUpdateRequest.getUsername());
        } else {
            dataFromLambda = lambdaServiceClient.getUserData(userUpdateRequest.getUsername());
        }


        UserRecord userRecord = new UserRecord();
        userRecord.setUserId(dataFromLambda.getUserId());
        userRecord.setFirstName(dataFromLambda.getFirstName());
        userRecord.setLastName(dataFromLambda.getLastName());
        userRecord.setUserType(dataFromLambda.getUserType());
        if (userUpdateRequest.getMembership() != null){
            userRecord.setMembership(userUpdateRequest.getMembership());
        } else {
            userRecord.setMembership(dataFromLambda.getMembership());
        }
        if (userUpdateRequest.getPassword() != null){
            userRecord.setStatus(userUpdateRequest.getStatus());
        } else {
            userRecord.setStatus(dataFromLambda.getStatus());
        }
        if (userUpdateRequest.getPassword() != null){
            userRecord.setPassword(userUpdateRequest.getPassword());
        } else {
            userRecord.setPassword(dataFromLambda.getPassword());
        }
        userRecord.setUsername(dataFromLambda.getUsername());

        UserData lambdaUser = lambdaServiceClient.updateUserData(userRecord);

        cache.add(dataFromLambda.getUsername(), new User(lambdaUser.getUserId(), lambdaUser.getFirstName(), lambdaUser.getLastName(), lambdaUser.getUserType(), lambdaUser.getMembership(), lambdaUser.getStatus(), lambdaUser.getUsername(), lambdaUser.getPassword()));
        return lambdaUser;
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

        cache.add(registeredUser.getUsername(), registeredUser);

        return registeredUser;
    }
}
