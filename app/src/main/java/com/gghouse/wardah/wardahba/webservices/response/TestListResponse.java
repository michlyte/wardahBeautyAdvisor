package com.gghouse.wardah.wardahba.webservices.response;

import com.gghouse.wardah.wardahba.model.Pagination;
import com.gghouse.wardah.wardahba.model.Test;

import java.util.List;

/**
 * Created by michaelhalim on 5/6/17.
 */

public class TestListResponse extends GenericResponse {
    private List<Test> data;
    private Pagination pagination;

    public List<Test> getData() {
        return data;
    }

    public void setData(List<Test> data) {
        this.data = data;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
