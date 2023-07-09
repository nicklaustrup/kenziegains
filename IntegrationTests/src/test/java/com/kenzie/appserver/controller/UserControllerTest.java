package com.kenzie.appserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.UserCreateRequest;
import com.kenzie.appserver.service.UserService;
import com.kenzie.appserver.service.model.User;

import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@IntegrationTest
class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    UserService userService;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void getById_Exists() throws Exception {

        String username = mockNeat.strings().valStr();
        String password = mockNeat.strings().valStr();
        String firstName = mockNeat.strings().valStr();
        String lastName = mockNeat.strings().valStr();
        String userType = mockNeat.strings().valStr();
        String membership = mockNeat.strings().valStr();

        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setFirstName(firstName);
        userCreateRequest.setLastName(lastName);
        userCreateRequest.setUserType(userType);
        userCreateRequest.setMembership(membership);
        userCreateRequest.setUsername(username);
        userCreateRequest.setPassword(password);


        User persistedUserClass = userService.addNewUser(userCreateRequest);

        mvc.perform(get("/user/{username}_{password}",persistedUserClass.getUsername(),persistedUserClass.getPassword())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("username")
                        .value(is(username)))
                .andExpect(jsonPath("firstName")
                        .value(is(firstName)))
                .andExpect(jsonPath("userType")
                        .value(is(userType)))
                .andExpect(status().isOk());
    }
    @Test
    public void getAll() throws Exception {

        String username = mockNeat.strings().valStr();
        String password = mockNeat.strings().valStr();
        String firstName = mockNeat.strings().valStr();
        String lastName = mockNeat.strings().valStr();
        String userType = mockNeat.strings().valStr();
        String membership = mockNeat.strings().valStr();


        String username2 = mockNeat.strings().valStr();
        String password2 = mockNeat.strings().valStr();
        String firstName2 = mockNeat.strings().valStr();
        String lastName2 = mockNeat.strings().valStr();
        String userType2 = mockNeat.strings().valStr();
        String membership2 = mockNeat.strings().valStr();

        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setFirstName(firstName);
        userCreateRequest.setLastName(lastName);
        userCreateRequest.setUserType(userType);
        userCreateRequest.setMembership(membership);
        userCreateRequest.setUsername(username);
        userCreateRequest.setPassword(password);

        UserCreateRequest userCreateRequest2 = new UserCreateRequest();
        userCreateRequest2.setFirstName(firstName2);
        userCreateRequest2.setLastName(lastName2);
        userCreateRequest2.setUserType(userType2);
        userCreateRequest2.setMembership(membership2);
        userCreateRequest2.setUsername(username2);
        userCreateRequest2.setPassword(password2);

        userService.addNewUser(userCreateRequest);
        userService.addNewUser(userCreateRequest2);

        MvcResult result =  mvc.perform(get("/user/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<String> usernameList = JsonPath.read(jsonResponse, "$[*].username");

        Assertions.assertTrue(usernameList.contains(username));
        Assertions.assertTrue(usernameList.contains(username2));
    }

    @Test
    public void createUserClass_CreateSuccessful() throws Exception {

        String username = mockNeat.strings().valStr();
        String password = mockNeat.strings().valStr();
        String firstName = mockNeat.strings().valStr();
        String lastName = mockNeat.strings().valStr();
        String userType = mockNeat.strings().valStr();
        String membership = mockNeat.strings().valStr();

        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setFirstName(firstName);
        userCreateRequest.setLastName(lastName);
        userCreateRequest.setUserType(userType);
        userCreateRequest.setMembership(membership);
        userCreateRequest.setUsername(username);
        userCreateRequest.setPassword(password);

        mapper.registerModule(new JavaTimeModule());

        mvc.perform(post("/user")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userCreateRequest)))
                .andExpect(jsonPath("firstName")
                        .value(is(firstName)))
                .andExpect(jsonPath("lastName")
                        .value(is(lastName)))
                .andExpect(jsonPath("userType")
                        .value(is(userType)))
                .andExpect(jsonPath("membership")
                        .value(is(membership)))
                .andExpect(jsonPath("username")
                        .value(is(username)))
                .andExpect(status().isOk());
    }

//    @Test
//    public void updateUser_IsSuccessful() throws Exception {
//
//        String username = "user" + System.currentTimeMillis();
//        String password = mockNeat.strings().valStr();
//        String firstName = mockNeat.strings().valStr();
//        String lastName = mockNeat.strings().valStr();
//        String userType = mockNeat.strings().valStr();
//        String membership = mockNeat.strings().valStr();
//        String updatedMembership = "testMembership";
//
//        UserCreateRequest userCreateRequest = new UserCreateRequest();
//        userCreateRequest.setFirstName(firstName);
//        userCreateRequest.setLastName(lastName);
//        userCreateRequest.setUserType(userType);
//        userCreateRequest.setMembership(membership);
//        userCreateRequest.setUsername(username);
//        userCreateRequest.setPassword(password);
//
//        userService.addNewUser(userCreateRequest);
//
//        UserCreateRequest updatedUserCreateRequest = new UserCreateRequest();
//        updatedUserCreateRequest.setFirstName(userCreateRequest.getFirstName());
//        updatedUserCreateRequest.setLastName(userCreateRequest.getLastName());
//        updatedUserCreateRequest.setUserType(userCreateRequest.getUserType());
//        updatedUserCreateRequest.setMembership(updatedMembership);
//        updatedUserCreateRequest.setUsername("user" + System.currentTimeMillis());
//        updatedUserCreateRequest.setPassword(userCreateRequest.getPassword());
//
//
//        mapper.registerModule(new JavaTimeModule());
//
//        mvc.perform(post("/user")
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(mapper.writeValueAsString(userCreateRequest)))
//                .andExpect(status().isOk());
//
//
//        mvc.perform(put("/user/update")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(updatedUserCreateRequest)))
//                        .andExpect(status().isOk())
//                        .andExpect(jsonPath("firstName")
//                                .value(is(firstName)))
//                        .andExpect(jsonPath("lastName")
//                                .value(is(lastName)))
//                        .andExpect(jsonPath("userType")
//                                .value(is(userType)))
//                        .andExpect(jsonPath("membership")
//                                .value(is(updatedMembership)))
//                        .andExpect(jsonPath("username")
//                                .value(is(username)));
//    }

}