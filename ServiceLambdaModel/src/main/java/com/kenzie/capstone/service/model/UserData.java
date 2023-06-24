package com.kenzie.capstone.service.model;

import java.util.Objects;

public class UserData {
    private String userId;

    private String firstName;

    private String lastName;

    private String userType;

    private String membership;

    private String status;

    private String username;

    private String password;

    public UserData(String userId, String firstName, String lastName, String userType, String membership, String status, String username, String password) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userType = userType;
        this.membership = membership;
        this.status = status;
        this.username = username;
        this.password = password;
    }

    public UserData(){}
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserData)) return false;
        UserData userData = (UserData) o;
        return Objects.equals(getUserId(), userData.getUserId()) && Objects.equals(getFirstName(), userData.getFirstName()) && Objects.equals(getLastName(), userData.getLastName()) && Objects.equals(getUserType(), userData.getUserType()) && Objects.equals(getMembership(), userData.getMembership()) && Objects.equals(getStatus(), userData.getStatus()) && Objects.equals(getUsername(), userData.getUsername()) && Objects.equals(getPassword(), userData.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getFirstName(), getLastName(), getUserType(), getMembership(), getStatus(), getUsername(), getPassword());
    }
}
