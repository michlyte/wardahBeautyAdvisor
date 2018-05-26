package com.gghouse.wardah.wardahba.model;

import java.io.Serializable;

/**
 * Created by michael on 5/27/2017.
 */

public class Profile implements Serializable {
    private Long createdTime;
    private Long updatedTime;
    private Long id;
    private Long nip;
    private Location location;
    private Long lastQuestionId;
    private String fullName;
    private String email;
    private String mobileNumber;
    private Boolean isDeleted;
    private String position;
    private String dateOfBirthFormat;
    private Long firstDateWork;

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public Long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Long updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNip() {
        return nip;
    }

    public void setNip(Long nip) {
        this.nip = nip;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Long getLastQuestionId() {
        return lastQuestionId;
    }

    public void setLastQuestionId(Long lastQuestionId) {
        this.lastQuestionId = lastQuestionId;
    }

    public String getFullName() {
        if (fullName == null || fullName.isEmpty()) {
            return "-";
        } else {
            return fullName;
        }
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        if (email == null || email.isEmpty()) {
            return "-";
        } else {
            return email;
        }
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        if (mobileNumber == null || mobileNumber.isEmpty()) {
            return "-";
        } else {
            return mobileNumber;
        }
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public String getPosition() {
        if (position == null || position.isEmpty()) {
            return "-";
        } else {
            return position;
        }
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDateOfBirthFormat() {
        return dateOfBirthFormat;
    }

    public void setDateOfBirthFormat(String dateOfBirthFormat) {
        this.dateOfBirthFormat = dateOfBirthFormat;
    }

    public Long getFirstDateWork() {
        return firstDateWork;
    }

    public void setFirstDateWork(Long firstDateWork) {
        this.firstDateWork = firstDateWork;
    }
}
