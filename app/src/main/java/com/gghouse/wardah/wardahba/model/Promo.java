package com.gghouse.wardah.wardahba.model;

import java.util.Date;

/**
 * Created by michaelhalim on 2/15/17.
 */

public class Promo {
    private String imageUrl;
    private String title;
    private String description;
    private Date postedDate;

    public Promo(String imageUrl, String title, String description, Date postedDate) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
        this.postedDate = postedDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }
}
