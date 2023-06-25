package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import org.springframework.data.annotation.Id;

import java.util.Objects;

@DynamoDBTable(tableName = "ClassAttendance")
public class ClassAttendanceRecord {
    @Id
    private ClassAttendanceCompositeId compositeId;
    private String attendanceStatus;

    @DynamoDBHashKey(attributeName = "userId")
    public String getUserId() {
        return compositeId != null ? compositeId.getUserId() : null;
    }

    @DynamoDBRangeKey(attributeName = "classId")
    public String getClassId() {
        return compositeId != null ? compositeId.getClassId() : null;
    }

    @DynamoDBAttribute(attributeName = "attendanceStatus")
    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setUserId(String userId) {
        if (compositeId == null){
            compositeId = new ClassAttendanceCompositeId();
        }
        compositeId.setUserId(userId);
    }

    public void setClassId(String classId) {
        if (compositeId == null){
            compositeId = new ClassAttendanceCompositeId();
        }
        compositeId.setClassId(classId);
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
        ClassAttendanceRecord that = (ClassAttendanceRecord) o;
        return Objects.equals(compositeId, that.compositeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(compositeId);
    }
}
