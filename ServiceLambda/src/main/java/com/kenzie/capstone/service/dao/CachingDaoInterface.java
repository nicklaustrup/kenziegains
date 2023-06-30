package com.kenzie.capstone.service.dao;

import com.kenzie.capstone.service.model.ClassAttendanceRecord;
import com.kenzie.capstone.service.model.InstructorLeadClassRecord;
import com.kenzie.capstone.service.model.UserRecord;
import jdk.jshell.Snippet;

import java.util.List;

public interface CachingDaoInterface {
    ClassAttendanceRecord setUserId(String userID);
    List<ClassAttendanceRecord> getUserId();
    boolean setAttendanceStatus(String attendanceStatus);
}
