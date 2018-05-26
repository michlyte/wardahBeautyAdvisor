package com.gghouse.wardah.wardahba.webservices.response;

import com.gghouse.wardah.wardahba.model.User;

/**
 * Created by michael on 3/10/2017.
 */

public class LoginResponse extends GenericResponse {
    private User data;

    public User getData() {
        return data;
    }
}
