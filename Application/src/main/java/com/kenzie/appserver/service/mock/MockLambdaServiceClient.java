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
        
        // Admin user
        users.add(new UserData(
                "test-user-id",
                "Test",
                "User",
                "administrator",
                "gold",
                "active",
                "admin",
                "password"));
                
        // Add some mock instructor users
        users.add(new UserData(
                "instructor-1",
                "John",
                "Smith",
                "instructor",
                "gold",
                "active",
                "jsmith",
                "password"));
                
        users.add(new UserData(
                "instructor-2",
                "Sarah",
                "Johnson",
                "instructor",
                "gold",
                "active",
                "sjohnson",
                "password"));
                
        // Add some mock regular users
        users.add(new UserData(
                "member-1",
                "Emily",
                "Davis",
                "gymMember",
                "gold",
                "active",
                "edavis",
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
    
    @Override
    public InstructorLeadClassData getInstructorLeadClassData(String classId) {
        // Return a mock class if the ID matches one of our predefined IDs
        if ("class-1".equals(classId)) {
            return createMockYogaClass();
        } else if ("class-2".equals(classId)) {
            return createMockHIITClass();
        }
        return null;
    }
    
    @Override
    public List<InstructorLeadClassData> getAllInstructorLeadClassData() {
        List<InstructorLeadClassData> classes = new ArrayList<>();
        
        // Add mock classes
        classes.add(createMockYogaClass());
        classes.add(createMockHIITClass());
        classes.add(createMockSpinningClass());
        classes.add(createMockBoxingClass());
        
        return classes;
    }
    
    // Helper methods to create mock classes
    private InstructorLeadClassData createMockYogaClass() {
        return new InstructorLeadClassData(
                "class-1",
                "Morning Yoga",
                "Start your day with rejuvenating yoga poses for all levels",
                "Yoga",
                "instructor-1",
                20,
                LocalDateTime.now().plusDays(1).withHour(8).withMinute(0).toString(),
                true
        );
    }
    
    private InstructorLeadClassData createMockHIITClass() {
        return new InstructorLeadClassData(
                "class-2",
                "HIIT Challenge",
                "High-intensity interval training for maximum fat burning",
                "Cardio",
                "instructor-2",
                15,
                LocalDateTime.now().plusDays(1).withHour(17).withMinute(30).toString(),
                true
        );
    }
    
    private InstructorLeadClassData createMockSpinningClass() {
        return new InstructorLeadClassData(
                "class-3",
                "Spinning Session",
                "Intense indoor cycling workout with pumping music",
                "Spinning",
                "instructor-1",
                25,
                LocalDateTime.now().plusDays(2).withHour(12).withMinute(15).toString(),
                true
        );
    }
    
    private InstructorLeadClassData createMockBoxingClass() {
        return new InstructorLeadClassData(
                "class-4",
                "Boxing Fundamentals",
                "Learn basic boxing techniques and combinations",
                "Boxing",
                "instructor-2",
                12,
                LocalDateTime.now().plusDays(2).withHour(19).withMinute(0).toString(),
                true
        );
    }
    
    // You can also implement the ClassAttendance methods if needed
    @Override
    public List<ClassAttendanceData> getAlLClassAttendanceData() {
        List<ClassAttendanceData> attendances = new ArrayList<>();
        
        // Add some mock attendances
        attendances.add(new ClassAttendanceData("member-1", "class-1", "Attending"));
        attendances.add(new ClassAttendanceData("member-1", "class-2", "Attending"));
        
        return attendances;
    }
}