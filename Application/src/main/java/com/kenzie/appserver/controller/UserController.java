package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.ExampleResponse;
import com.kenzie.appserver.controller.model.UserCreateRequest;
import com.kenzie.appserver.controller.model.UserResponse;
import com.kenzie.appserver.service.UserService;
import com.kenzie.appserver.service.model.Example;
import com.kenzie.appserver.service.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("id") String id) {

        User user  = userService.findById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        UserResponse exampleResponse = new UserResponse();
        exampleResponse.setId(example.getId());
        exampleResponse.setName(example.getName());
        return ResponseEntity.ok(exampleResponse);
    }

    @PostMapping
    public ResponseEntity<UserResponse> addNewUser(@RequestBody UserCreateRequest userCreateRequest) {
        User user = userService.addNewExample(userCreateRequest.getName());

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setName(user.getName());

        return ResponseEntity.ok(userResponse);
    }

    /******************************  HELPER METHODS  *************************************/

    //TODO from User Request to User Response

    //TODO from User Resposne to User Request
}
