package com.gghouse.wardah.wardahba.model;

/**
 * Created by michaelhalim on 5/25/17.
 */

public class Questioner {
    private Long id;
    private String content;
    private Float rating;

    public Questioner(Long id, String content) {
        this.id = id;
        this.content = content;
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

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }
}
