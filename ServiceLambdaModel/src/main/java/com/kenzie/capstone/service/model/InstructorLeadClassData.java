package com.kenzie.capstone.service.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class InstructorLeadClassData {
    private String classId;
    private String name;
    private String description;
    private String classType;
    private String userId;
    private int classCapacity;
//    private LocalDateTime dateTime;
    private String dateTime;
    private boolean status;

//    public InstructorLeadClassData(String classId, String name, String description, String classType, String userId, int classCapacity, LocalDateTime dateTime, boolean status) {
public InstructorLeadClassData(String classId, String name, String description, String classType, String userId, int classCapacity, String dateTime, boolean status) {
        this.classId = classId;
        this.name = name;
        this.description = description;
        this.classType = classType;
        this.userId = userId;
        this.classCapacity = classCapacity;
        this.dateTime = dateTime;
        this.status = status;
    }

    public InstructorLeadClassData(){};

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getClassCapacity() {
        return classCapacity;
    }

    public void setClassCapacity(int classCapacity) {
        this.classCapacity = classCapacity;
    }

//    public LocalDateTime getDateTime() {
    public String getDateTime() {
        return dateTime;
    }

//    public void setDateTime(LocalDateTime dateTime) {
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstructorLeadClassData that = (InstructorLeadClassData) o;
        return classCapacity == that.classCapacity && status == that.status && classId.equals(that.classId) && name.equals(that.name) && description.equals(that.description) && classType.equals(that.classType) && userId.equals(that.userId) && dateTime.equals(that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classId, name, description, classType, userId, classCapacity, dateTime, status);
    }
}
