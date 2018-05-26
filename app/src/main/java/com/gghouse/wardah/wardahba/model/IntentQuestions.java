package com.gghouse.wardah.wardahba.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by michaelhalim on 5/8/17.
 */

public class IntentQuestions implements Serializable {
    private List<Question> objects;

    public IntentQuestions(List<Question> objects) {
        this.objects = objects;
    }

    public List<Question> getObjects() {
        return objects;
    }

    public void setObjects(List<Question> objects) {
        this.objects = objects;
    }
}
