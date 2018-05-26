package com.gghouse.wardah.wardahba.model;

import java.io.Serializable;

/**
 * Created by michael on 3/13/2017.
 */

public class Sales implements Serializable {
    private Long createdTime;
    private Long id;
    private Double salesAmount;
    private Long userId;
    private boolean editable;

    public Sales(Long createdTime, Long id, Double salesAmount, Long userId, boolean editable) {
        this.createdTime = createdTime;
        this.id = id;
        this.salesAmount = salesAmount;
        this.userId = userId;
        this.editable = editable;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(Double salesAmount) {
        this.salesAmount = salesAmount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }
}
