package com.gghouse.wardah.wardahba.webservices.response;

import com.gghouse.wardah.wardahba.model.Pagination;
import com.gghouse.wardah.wardahba.model.Sales;

import java.util.List;

/**
 * Created by michael on 3/20/2017.
 */

public class SalesListResponse extends GenericResponse {
    private List<Sales> data;
    private Pagination pagination;

    public List<Sales> getData() {
        return data;
    }

    public void setData(List<Sales> data) {
        this.data = data;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
