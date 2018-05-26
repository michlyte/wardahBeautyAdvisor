package com.gghouse.wardah.wardahba.webservices.response;

import com.gghouse.wardah.wardahba.model.Questioner;

import java.util.List;

/**
 * Created by ecquaria-macmini on 5/26/17.
 */

public class QuestionerResponse extends GenericResponse {
    private List<Questioner> data;

    public List<Questioner> getData() {
        return data;
    }

    public void setData(List<Questioner> data) {
        this.data = data;
    }
}
