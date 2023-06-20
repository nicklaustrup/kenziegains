package com.kenzie.appserver.service.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class InstructorLeadClass {
    private final String classId;
    private final String name;
    private final String description;
    private final String classtype;
    private final String userId;
    private final String instructorsName;
    private final Integer classCapacity;
    private final LocalDate date;
    private final LocalTime time;
    private final Boolean status;

    public InstructorLeadClass(String classId, String name, String description, String classtype,
                               String userId, String instructorsName, Integer classCapacity,
                               LocalDate date, LocalTime time, Boolean status) {
        this.classId = classId;
        this.name = name;
        this.description = description;
        this.classtype = classtype;
        this.userId = userId;
        this.instructorsName = instructorsName;
        this.classCapacity = classCapacity;
        this.date = date;
        this.time = time;
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

    public String getClasstype() {
        return classtype;
    }

    public String getUserId() {
        return userId;
    }

    public String getInstructorsName() {
        return instructorsName;
    }

    public Integer getClassCapacity() {
        return classCapacity;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public Boolean getStatus() {
        return status;
    }
}
