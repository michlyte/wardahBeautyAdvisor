package com.gghouse.wardah.wardahba.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by michaelhalim on 5/6/17.
 */

public class Question implements Serializable {
    private String createdBy;
    private Long createdTime;
    private String updatedBy;
    private Long updatedTime;
    private String dateFormat;
    private String updatedFormat;
    private Long id;
    private String content;
    private boolean isDeleted;
    private List<Answer> answers;

    public Question(long id, String content, List<Answer> answers) {
        this.id = id;
        this.content = content;
        this.answers = answers;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
