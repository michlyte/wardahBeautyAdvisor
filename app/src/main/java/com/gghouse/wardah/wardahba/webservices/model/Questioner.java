package com.gghouse.wardah.wardahba.webservices.model;

/**
 * Created by ecquaria-macmini on 5/26/17.
 */

public class Questioner {
    private Long questionnaireId;
    private Float value;

    public Questioner(Long questionerId, Float value) {
        this.questionnaireId = questionerId;
        this.value = value;
    }

    public Long getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(Long questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }
}
