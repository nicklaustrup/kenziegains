package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.kenzie.appserver.converter.LocalDateTimeConverter;

import java.time.LocalDateTime;
import java.util.Objects;

@DynamoDBTable(tableName = "InstructorLeadClass")
public class InstructorLeadClassRecord {
    private String classId;
    private String name;
    private String description;
    private String classType;
    private String userId;
    private int classCapacity;
    private LocalDateTime dateTime;
    private boolean status;

    @DynamoDBHashKey(attributeName = "classId")
    public String getClassId() {
        return classId;
    }

    @DynamoDBAttribute(attributeName = "name")
    public String getName() {
        return name;
    }

    @DynamoDBAttribute(attributeName = "description")
    public String getDescription() {
        return description;
    }

    @DynamoDBAttribute(attributeName = "classType")
    public String getClassType() {
        return classType;
    }

    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return userId;
    }

    @DynamoDBAttribute(attributeName = "classCapacity")
    public int getClassCapacity() {
        return classCapacity;
    }

    @DynamoDBAttribute(attributeName = "dateTime")
    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @DynamoDBAttribute(attributeName = "status")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.BOOL)
    public boolean isStatus() {
        return status;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setClassCapacity(int classCapacity) {
        this.classCapacity = classCapacity;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstructorLeadClassRecord that = (InstructorLeadClassRecord) o;
        return classCapacity == that.classCapacity && status == that.status && classId.equals(that.classId) && name.equals(that.name) && description.equals(that.description) && classType.equals(that.classType) && userId.equals(that.userId) && dateTime.equals(that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classId, name, description, classType, userId, classCapacity, dateTime, status);
    }
}
