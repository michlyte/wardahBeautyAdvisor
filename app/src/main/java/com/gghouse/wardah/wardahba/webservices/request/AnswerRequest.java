package com.gghouse.wardah.wardahba.webservices.request;

/**
 * Created by michaelhalim on 5/9/17.
 */

public class AnswerRequest {
    private Long questionId;
    private Long answerId;

    public AnswerRequest(Long questionId, Long answerId) {
        this.questionId = questionId;
        this.answerId = answerId;
    }
}
