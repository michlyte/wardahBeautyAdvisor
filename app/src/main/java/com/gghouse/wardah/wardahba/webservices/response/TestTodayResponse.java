package com.gghouse.wardah.wardahba.webservices.response;

import com.gghouse.wardah.wardahba.model.Test;

/**
 * Created by michaelhalim on 5/6/17.
 */

public class TestTodayResponse extends GenericResponse {
    private Test data;

    public Test getData() {
        return data;
    }

    public void setData(Test data) {
        this.data = data;
    }
}
