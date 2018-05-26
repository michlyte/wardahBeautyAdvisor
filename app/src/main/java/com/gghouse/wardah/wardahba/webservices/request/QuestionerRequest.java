package com.gghouse.wardah.wardahba.webservices.request;

import com.gghouse.wardah.wardahba.webservices.model.Questioner;

import java.util.List;

/**
 * Created by ecquaria-macmini on 5/26/17.
 */

public class QuestionerRequest {
    private Long userId;
    private Long questionnaireDate;
    private List<Questioner> values;

    public QuestionerRequest(Long userId, Long questionnaireDate, List<Questioner> values) {
        this.userId = userId;
        this.questionnaireDate = questionnaireDate;
        this.values = values;
    }
}
