package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class ClassAttendanceCreateRequest {

    @NotEmpty
    @JsonProperty("userId")
    private String userId;
    @NotEmpty
    @JsonProperty("classId")
    private String classId;
    @NotEmpty
    @JsonProperty("classAttendance")
    private String classAttendance;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassAttendance() {
        return classAttendance;
    }

    public void setClassAttendance(String classAttendance) {
        this.classAttendance = classAttendance;
    }

}
