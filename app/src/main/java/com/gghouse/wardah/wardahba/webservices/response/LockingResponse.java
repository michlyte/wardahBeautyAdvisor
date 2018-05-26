package com.gghouse.wardah.wardahba.webservices.response;

import com.gghouse.wardah.wardahba.webservices.model.Lock;

/**
 * Created by ecquaria-macmini on 5/26/17.
 */

public class LockingResponse extends GenericResponse {
    private Lock data;

    public Lock getData() {
        return data;
    }

    public void setData(Lock data) {
        this.data = data;
    }
}
