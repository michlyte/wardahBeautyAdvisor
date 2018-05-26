package com.gghouse.wardah.wardahba.model;

import com.google.gson.Gson;

/**
 * Created by michael on 3/10/2017.
 */

public class User {
    private Long id;
    private Long lastQuestionId;
    private String fullName;
    private String email;
    private String mobileNumber;
    private Long dateOfBirth;
    private Location location;
    private String userType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLastQuestionId() {
        return lastQuestionId;
    }

    public void setLastQuestionId(Long lastQuestionId) {
        this.lastQuestionId = lastQuestionId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Long getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
