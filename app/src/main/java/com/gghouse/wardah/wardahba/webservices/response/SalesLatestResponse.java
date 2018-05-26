package com.gghouse.wardah.wardahba.webservices.response;

import com.gghouse.wardah.wardahba.model.Sales;

import java.util.List;

/**
 * Created by michael on 3/13/2017.
 */

public class SalesLatestResponse extends GenericResponse {
    private List<Sales> data;

    public List<Sales> getData() {
        return data;
    }

    public void setData(List<Sales> data) {
        this.data = data;
    }
}
