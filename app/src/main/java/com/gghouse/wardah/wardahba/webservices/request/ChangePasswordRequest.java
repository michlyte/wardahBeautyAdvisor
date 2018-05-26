package com.gghouse.wardah.wardahba.webservices.request;

/**
 * Created by michaelhalim on 6/9/17.
 */

public class ChangePasswordRequest {
    private String oldPassword;
    private String password;

    public ChangePasswordRequest(String oldPassword, String password) {
        this.oldPassword = oldPassword;
        this.password = password;
    }
}
