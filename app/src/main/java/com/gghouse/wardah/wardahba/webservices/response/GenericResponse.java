package com.gghouse.wardah.wardahba.webservices.response;

/**
 * Created by michael on 3/13/2017.
 */

public class GenericResponse {
    private String status;
    private Integer code;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
