package com.gghouse.wardah.wardahba.webservices.response;

import com.gghouse.wardah.wardahba.model.Notif;
import com.gghouse.wardah.wardahba.model.Pagination;

import java.util.List;

/**
 * Created by michael on 3/13/2017.
 */

public class NotificationsResponse extends GenericResponse {
    private List<Notif> data;
    private Pagination pagination;

    public List<Notif> getData() {
        return data;
    }

    public void setData(List<Notif> data) {
        this.data = data;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
