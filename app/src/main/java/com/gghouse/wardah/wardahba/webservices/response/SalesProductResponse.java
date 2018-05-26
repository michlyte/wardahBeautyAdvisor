package com.gghouse.wardah.wardahba.webservices.response;

import com.gghouse.wardah.wardahba.model.ProductHighlight;

import java.util.List;

/**
 * Created by michael on 3/20/2017.
 */

public class SalesProductResponse extends GenericResponse {
    private List<ProductHighlight> data;

    public List<ProductHighlight> getData() {
        return data;
    }

    public void setData(List<ProductHighlight> data) {
        this.data = data;
    }
}
