package com.kenzie.appserver.service.mock;

import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.UserData;
import com.kenzie.capstone.service.model.UserRecord;
import com.kenzie.capstone.service.model.ClassAttendanceData;
import com.kenzie.capstone.service.model.InstructorLeadClassData;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MockLambdaServiceClient extends LambdaServiceClient {

    @Override
    public UserData getUserData(String username) {
        // Create a mock user for admin
        if (username.equals("admin")) {
            return new UserData(
                    "test-user-id",
                    "Test",
                    "User",
                    "administrator",
                    "gold",
                    "active",
                    "admin",
                    "password");
        }
        return null;
    }

    @Override
    public List<UserData> getAllUsersData() {
        List<UserData> users = new ArrayList<>();
        users.add(new UserData(
                "test-user-id",
                "Test",
                "User",
                "administrator",
                "gold",
                "active",
                "admin",
                "password"));
        return users;
    }

    @Override
    public UserData setUserData(UserRecord data) {
        // Create a user with the provided data
        return new UserData(
                UUID.randomUUID().toString(), // Generate a random ID
                data.getFirstName(),
                data.getLastName(),
                data.getUserType(),
                data.getMembership(),
                "active", // Default status
                data.getUsername(),
                data.getPassword());
    }
}