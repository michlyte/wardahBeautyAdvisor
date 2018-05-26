package com.gghouse.wardah.wardahba.webservices.response;

import com.gghouse.wardah.wardahba.model.Answer;
import com.gghouse.wardah.wardahba.model.Question;

import java.util.Collections;
import java.util.List;

/**
 * Created by michaelhalim on 5/6/17.
 */

public class TestQuestionsResponse extends GenericResponse {
    private List<Question> data;

    public List<Question> getData() {
        // Shuffle answers for each question
        for (int i = 0; i < data.size(); i++) {
            List<Answer> answers = data.get(i).getAnswers();
            Collections.shuffle(answers);
            data.get(i).setAnswers(answers);
        }
        return data;
    }

    public void setData(List<Question> data) {
        this.data = data;
    }
}
