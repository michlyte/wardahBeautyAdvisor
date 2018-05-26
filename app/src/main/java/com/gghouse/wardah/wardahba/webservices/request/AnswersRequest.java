package com.gghouse.wardah.wardahba.webservices.request;

import java.util.List;

/**
 * Created by michaelhalim on 5/8/17.
 */

public class AnswersRequest {
    private Long userId;
    private Long answerDate;
    private List<AnswerRequest> answers;

    public AnswersRequest(Long userId, Long answerDate, List<AnswerRequest> answers) {
        this.userId = userId;
        this.answerDate = answerDate;
        this.answers = answers;
    }
}
