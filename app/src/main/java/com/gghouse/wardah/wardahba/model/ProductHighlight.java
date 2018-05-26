package com.gghouse.wardah.wardahba.model;

import java.io.Serializable;

/**
 * Created by michaelhalim on 5/13/17.
 */

public class ProductHighlight implements Serializable {
    private String createdBy;
    private Long createdTime;
    private String updatedBy;
    private Long updatedTime;
    private String dateFormat;
    private String updatedFormat;
    private Long id;
    private FileLocation fileLocation;
    private String name;
    private Integer targetSales;
    private Boolean isDeleted;

    /*
     * Front-end
     */
    private Integer qty;

    public ProductHighlight(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Long updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getUpdatedFormat() {
        return updatedFormat;
    }

    public void setUpdatedFormat(String updatedFormat) {
        this.updatedFormat = updatedFormat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FileLocation getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(FileLocation fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTargetSales() {
        return targetSales;
    }

    public void setTargetSales(Integer targetSales) {
        this.targetSales = targetSales;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}
