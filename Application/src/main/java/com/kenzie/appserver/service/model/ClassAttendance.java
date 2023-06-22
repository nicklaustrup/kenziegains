package com.kenzie.appserver.service.model;

public class ClassAttendance {
    private final String classId;
    private final String userId;
    private final String attendanceStatus;

    public ClassAttendance(String classId, String userId, String attendanceStatus) {
        this.classId = classId;
        this.userId = userId;
        this.attendanceStatus = attendanceStatus;
    }

    public String getClassId() {
        return classId;
    }

    public String getUserId() {
        return userId;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }
}
