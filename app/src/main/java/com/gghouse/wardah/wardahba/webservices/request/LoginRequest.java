package com.gghouse.wardah.wardahba.webservices.request;

/**
 * Created by michael on 3/10/2017.
 */

public class LoginRequest {
    private String username;
    private String password;
    private String imei;
    private String regId;

    public LoginRequest(String username, String password, String imei, String regId) {
        this.username = username;
        this.password = password;
        this.imei = imei;
        this.regId = regId;
    }
}
