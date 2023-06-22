package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.ExampleResponse;
import com.kenzie.appserver.controller.model.UserCreateRequest;
import com.kenzie.appserver.controller.model.UserResponse;
import com.kenzie.appserver.service.UserService;
import com.kenzie.appserver.service.model.Example;
import com.kenzie.appserver.service.model.User;
import com.kenzie.capstone.service.model.UserData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}_{password}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("username") String username, @PathVariable("password") String password) {

        UserData user  = userService.findById(username, password);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        UserResponse userResponse = new UserResponse();
        userResponse.setUserId(user.getUserId());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setUserType(user.getUserType());
        userResponse.setMembership(user.getMembership());
        userResponse.setStatus(user.getStatus());
        userResponse.setUsername(user.getUsername());
        userResponse.setPassword(user.getPassword());


        return ResponseEntity.ok(userResponse);
    }

    @PostMapping
    public ResponseEntity<UserResponse> addNewUser(@RequestBody UserCreateRequest userCreateRequest) {
        User user = userService.addNewUser(userCreateRequest);

        UserResponse userResponse = new UserResponse();
        userResponse.setUserId(user.getUserId());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setUserType(user.getUserType());
        userResponse.setMembership(user.getMembership());
        userResponse.setStatus(user.getStatus());
        userResponse.setUsername(user.getUsername());
        userResponse.setPassword(user.getPassword());

        return ResponseEntity.ok(userResponse);
    }

    /******************************  HELPER METHODS  *************************************/

    //TODO from User Request to User Response

    //TODO from User Resposne to User Request
}
