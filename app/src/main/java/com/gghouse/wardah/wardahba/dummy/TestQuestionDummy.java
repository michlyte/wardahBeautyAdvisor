package com.gghouse.wardah.wardahba.dummy;

import com.gghouse.wardah.wardahba.model.Answer;
import com.gghouse.wardah.wardahba.model.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michaelhalim on 5/6/17.
 */

public class TestQuestionDummy {
    public static final List<Question> ITEMS = new ArrayList<Question>();

    private static final int COUNT = 5;

    static {
        for (int i = 0; i < COUNT; i++) {
            List<Answer> answerList = new ArrayList<Answer>();
            for (int j = 0; j < COUNT; j++) {
                answerList.add(new Answer(i + j, "Jawaban " + j));
            }
            ITEMS.add(new Question(i, "Pertanyaan ke " + i, answerList));
        }
    }
}
