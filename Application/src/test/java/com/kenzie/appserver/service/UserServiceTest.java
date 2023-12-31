package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheUser;
import com.kenzie.appserver.controller.model.UserCreateRequest;
import com.kenzie.appserver.controller.model.UserUpdateRequest;
import com.kenzie.appserver.repositories.UserRepository;

import com.kenzie.appserver.service.model.User;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.UserData;
import com.kenzie.capstone.service.model.UserRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    private UserRepository userRepository;
    private UserService userService;
    private LambdaServiceClient lambdaServiceClient;
    private CacheUser cacheUser;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        lambdaServiceClient = mock(LambdaServiceClient.class);
        cacheUser = mock(CacheUser.class);
        userService = new UserService(userRepository, lambdaServiceClient, cacheUser);
    }
    /** ------------------------------------------------------------------------
     *  instructorLeadClassService.findById
     *  ------------------------------------------------------------------------ **/

    @Test
    void findById() {
        // GIVEN
        String username = "testUser";
        String password = "password";
        UserData userData = new UserData("userId", "abc", "xyz", "userType",
                "membership", "active", username, password);
        when(lambdaServiceClient.getUserData(username)).thenReturn(userData);

        // WHEN
        UserData result = userService.findById(username, password);

        // THEN
        assertEquals(userData, result);
        verify(lambdaServiceClient).getUserData(username);
        verify(userRepository, never()).findById(anyString());
    }

    @Test
    void findById_invalid() {
        // GIVEN
        String username = "testUser";
        String password = "password";
        UserData userData = new UserData("userId", "abc", "xyz", "userType",
                "membership", "active", username, "wrong");
        when(lambdaServiceClient.getUserData(username)).thenReturn(userData);

        // WHEN

        // THEN
        assertThrows(IllegalArgumentException.class, () -> userService.findById(username, password));
        verify(lambdaServiceClient).getUserData(username);
        verify(userRepository, never()).findById(anyString());
    }
    @Test
    void findAll() {
        // GIVEN
        List<UserData> userDataList = Arrays.asList(
                new UserData("userId1", "abc", "xyz", "userType1",
                        "membership1", "active", "username1", "password1"),
                new UserData("userId2", "aaa", "xxx", "userType2",
                        "membership2", "active", "username2", "password2")
        );
        when(lambdaServiceClient.getAllUsersData()).thenReturn(userDataList);


        // WHEN
        List<UserData> result = userService.findAll();

        // THEN
        assertEquals(2, result.size());
        verify(lambdaServiceClient).getAllUsersData();
        verify(userRepository, never()).findAll();
    }
    @Test
    void addNewUser() {
        // GIVEN
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setFirstName("abc");
        userCreateRequest.setLastName("xyz");
        userCreateRequest.setUserType("userType");
        userCreateRequest.setMembership("membership");
        userCreateRequest.setUsername("username");
        userCreateRequest.setPassword("password");

        when(lambdaServiceClient.setUserData(any(UserRecord.class))).
                thenReturn(new UserData("userId", "abc", "xyz", "userType",
                        "membership", "active", "username", "password"));

        // WHEN
        User result = userService.addNewUser(userCreateRequest);

        // THEN
        assertNotNull(result);
        assertEquals("userId", result.getUserId());
        assertEquals("abc", result.getFirstName());
        assertEquals("xyz", result.getLastName());
        assertEquals("userType", result.getUserType());
        assertEquals("membership", result.getMembership());
        assertEquals("active", result.getStatus());
        assertEquals("username", result.getUsername());
        assertEquals("password", result.getPassword());
        verify(lambdaServiceClient).setUserData(any(UserRecord.class));
    }

    @Test
    void updateUser_WithExistingData() {

        // GIVEN
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setUserType("userType");
        userUpdateRequest.setMembership("membership");
        userUpdateRequest.setUsername("username");
        userUpdateRequest.setPassword("password");

        UserData mockUserData = new UserData();
        mockUserData.setUserId("123");
        mockUserData.setFirstName("abc");
        mockUserData.setLastName("xyz");
        mockUserData.setUserType("Regular");
        mockUserData.setStatus("Active");

        // WHEN
        when(lambdaServiceClient.getUserData(userUpdateRequest.getUsername())).thenReturn(mockUserData);
        when(lambdaServiceClient.updateUserData(any(UserRecord.class))).thenReturn(mockUserData);
        UserData updatedUserData = userService.updateUser(userUpdateRequest);

        // THEN
        assertEquals(mockUserData, updatedUserData);
    }
    @Test
    void updateUser_WithNonExistingData() {

        // GIVEN
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setUserType("userType");
        userUpdateRequest.setMembership("membership");
        userUpdateRequest.setUsername("username");
        userUpdateRequest.setPassword("password");

        UserData mockUserData = new UserData();
        mockUserData.setUserId("123");
        mockUserData.setFirstName("abc");
        mockUserData.setLastName("xyz");
        mockUserData.setUserType("Regular");
        mockUserData.setStatus("Active");

        // WHEN
        when(cacheUser.get("username")).thenReturn(null);
        when(lambdaServiceClient.getUserData(userUpdateRequest.getUsername())).thenReturn(mockUserData);

        // THEN
        assertThrows(NullPointerException.class, () -> userService.updateUser(userUpdateRequest));
        verify(cacheUser, never()).evict(any());
    }
        @Test
        void updateUser_CacheUserExists() {

            // GIVEN
            UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
            userUpdateRequest.setUsername("username");

            User cachedUser = new User("123", "abc", "xyz",
                    "userType", "membership", "Active", "username", "password");
            when(cacheUser.get("username")).thenReturn(cachedUser);

            UserData mockUserData = new UserData();
            mockUserData.setUserId("123");
            mockUserData.setFirstName("abc");
            mockUserData.setLastName("xyz");
            mockUserData.setUserType("userType");
            mockUserData.setStatus("Active");
            mockUserData.setUsername("username");
            when(lambdaServiceClient.getUserData("username")).thenReturn(mockUserData);
            when(lambdaServiceClient.updateUserData(any(UserRecord.class))).thenReturn(mockUserData);

            // WHEN
            UserData updatedUserData = userService.updateUser(userUpdateRequest);

            // THEN
            assertNotNull(updatedUserData);
            verify(cacheUser, times(1)).evict("username");
    }
}
