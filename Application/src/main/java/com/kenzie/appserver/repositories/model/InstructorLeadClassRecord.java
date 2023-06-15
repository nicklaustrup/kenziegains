package com.kenzie.appserver.repositories.model;

import java.util.Objects;

public class InstructorLeadClassRecord {
    String classId;
    String name;
    String description;
    String classtype;
    String userId;
    String instructorsName;
    Integer classCapacity;
//    LocalDate date;
//    LocalTime time;
    Boolean status;

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

    public String getClasstype() {
        return classtype;
    }

    public void setClasstype(String classtype) {
        this.classtype = classtype;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getInstructorsName() {
        return instructorsName;
    }

    public void setInstructorsName(String instructorsName) {
        this.instructorsName = instructorsName;
    }

    public Integer getClassCapacity() {
        return classCapacity;
    }

    public void setClassCapacity(Integer classCapacity) {
        this.classCapacity = classCapacity;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstructorLeadClassRecord that = (InstructorLeadClassRecord) o;
        return classId.equals(that.classId) && name.equals(that.name) && description.equals(that.description) && classtype.equals(that.classtype) && userId.equals(that.userId) && instructorsName.equals(that.instructorsName) && classCapacity.equals(that.classCapacity) && status.equals(that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classId, name, description, classtype, userId, instructorsName, classCapacity, status);
    }
}
