package com.gghouse.wardah.wardahba.dummy;

import com.gghouse.wardah.wardahba.model.Questioner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michaelhalim on 5/25/17.
 */

public class QuestionerDummy {
    public static final List<Questioner> ITEMS = new ArrayList<Questioner>();

    private static final int COUNT = 5;

    static {
        for (int i = 0; i < COUNT; i++) {
            ITEMS.add(new Questioner((long) i, "Soal: " + i));
        }
    }
}
