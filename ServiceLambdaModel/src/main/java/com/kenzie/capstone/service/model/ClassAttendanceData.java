package com.kenzie.capstone.service.model;

import java.util.Objects;

public class ClassAttendanceData {
    private String userId;
    private String classId;
    private String attendanceStatus;

    public ClassAttendanceData(String userId, String classId, String attendanceStatus) {
        this.userId = userId;
        this.classId = classId;
        this.attendanceStatus = attendanceStatus;
    }

    public ClassAttendanceData() {}

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

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClassAttendanceData that = (ClassAttendanceData) o;
        return Objects.equals(userId, that.userId) && Objects.equals(classId, that.classId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, classId);
    }
}
