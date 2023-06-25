package com.kenzie.appserver.service.model;

public class ClassAttendance {
    private final String userId;
    private final String classId;
    private final String attendanceStatus;

    public ClassAttendance(String userId, String classId, String attendanceStatus) {
        this.userId = userId;
        this.classId = classId;
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
