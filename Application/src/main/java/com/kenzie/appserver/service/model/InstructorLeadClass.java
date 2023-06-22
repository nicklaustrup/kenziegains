package com.kenzie.appserver.service.model;

import java.time.LocalDateTime;

public class InstructorLeadClass {
    private final String classId;
    private final String name;
    private final String description;
    private final String classType;
    private final String userId;
    private final int classCapacity;
    private final LocalDateTime dateTime;
    private final boolean status;

    public InstructorLeadClass(String classId, String name, String description, String classType, String userId, int classCapacity, LocalDateTime dateTime, boolean status) {
        this.classId = classId;
        this.name = name;
        this.description = description;
        this.classType = classType;
        this.userId = userId;
        this.classCapacity = classCapacity;
        this.dateTime = dateTime;
        this.status = status;
    }

    public String getClassId() {
        return classId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getClassType() {
        return classType;
    }

    public String getUserId() {
        return userId;
    }

    public int getClassCapacity() {
        return classCapacity;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public boolean isStatus() {
        return status;
    }
}
