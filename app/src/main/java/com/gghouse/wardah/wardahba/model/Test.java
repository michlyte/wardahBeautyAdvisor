package com.gghouse.wardah.wardahba.model;

import java.util.Date;

/**
 * Created by michaelhalim on 2/16/17.
 */

public class Test {
    private Integer score;
    private String title;
    private Integer totalSoal;
    private Long createdTime;

    public Test(Integer score, String title, Integer totalSoal, Long createdTime) {
        this.score = score;
        this.title = title;
        this.totalSoal = totalSoal;
        this.createdTime = createdTime;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTotalSoal() {
        return totalSoal;
    }

    public void setTotalSoal(Integer totalSoal) {
        this.totalSoal = totalSoal;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }
}
