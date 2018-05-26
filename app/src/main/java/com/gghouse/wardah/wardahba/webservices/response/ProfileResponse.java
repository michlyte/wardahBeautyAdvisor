package com.gghouse.wardah.wardahba.webservices.response;

import com.gghouse.wardah.wardahba.model.Profile;

/**
 * Created by michael on 5/27/2017.
 */

public class ProfileResponse extends GenericResponse {
    private Profile data;

    public Profile getData() {
        return data;
    }

    public void setData(Profile data) {
        this.data = data;
    }
}
