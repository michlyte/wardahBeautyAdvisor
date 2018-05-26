package com.gghouse.wardah.wardahba.model;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by michael on 5/4/2017.
 */

public class TestHistoryHeader extends Test {
    private Double average;
    private Float correctRate;
    private Integer sumTestTaken;

    public TestHistoryHeader() {
        super(0, "", 0, new Date().getTime());
        this.average = 0.00;
        this.correctRate = 0f;
        this.sumTestTaken = 0;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public Float getCorrectRate() {
        return correctRate;
    }

    public void setCorrectRate(Float correctRate) {
        this.correctRate = correctRate;
    }

    public Integer getSumTestTaken() {
        return sumTestTaken;
    }

    public void setSumTestTaken(Integer sumTestTaken) {
        this.sumTestTaken = sumTestTaken;
    }
}
