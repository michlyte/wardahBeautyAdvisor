package com.gghouse.wardah.wardahba.model;

import java.io.Serializable;

/**
 * Created by michael on 3/13/2017.
 */

public class Notif implements Serializable {
    private Long id;
    private String description;
    private String type;
    private Long periodStart;
    private Long periodEnd;
    private FileLocation fileLocation;
    private String status;
    private Long notificationTime;

    // Dummy variables
    private Integer drawable;

    public Notif(Long id, String description, String type, Integer drawable, Long periodStart) {
        this.id = id;
        this.description = description;
        this.type = type;
        this.drawable = drawable;
        this.periodStart = periodStart;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(Long periodStart) {
        this.periodStart = periodStart;
    }

    public Long getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(Long periodEnd) {
        this.periodEnd = periodEnd;
    }

    public FileLocation getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(FileLocation fileLocation) {
        this.fileLocation = fileLocation;
    }

    public Integer getDrawable() {
        return drawable;
    }

    public void setDrawable(Integer drawable) {
        this.drawable = drawable;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(Long notificationTime) {
        this.notificationTime = notificationTime;
    }
}
