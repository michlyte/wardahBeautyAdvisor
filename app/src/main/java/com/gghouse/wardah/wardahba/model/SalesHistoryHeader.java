package com.gghouse.wardah.wardahba.model;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by michael on 3/20/2017.
 */

public class SalesHistoryHeader extends Sales {
    private Double average;
    private Double countByMonth;
    private Double count;
    private Double highestPerMonth;

    public SalesHistoryHeader() {
        super(new Date().getTime(), 0L, 0.00, 0L, false);
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public Double getCountByMonth() {
        return countByMonth;
    }

    public void setCountByMonth(Double countByMonth) {
        this.countByMonth = countByMonth;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public Double getHighestPerMonth() {
        return highestPerMonth;
    }

    public void setHighestPerMonth(Double highestPerMonth) {
        this.highestPerMonth = highestPerMonth;
    }
}
