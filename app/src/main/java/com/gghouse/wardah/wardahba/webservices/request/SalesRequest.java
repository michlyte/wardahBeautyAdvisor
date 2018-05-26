package com.gghouse.wardah.wardahba.webservices.request;

import com.gghouse.wardah.wardahba.webservices.model.Product;

import java.util.List;

/**
 * Created by michael on 3/21/2017.
 */

public class SalesRequest {
    private Long userId;
    private Double salesAmount;
    private Long salesDate;
    private List<Product> products;

    public SalesRequest(Long userId, Double salesAmount, Long salesDate, List<Product> products) {
        this.userId = userId;
        this.salesAmount = salesAmount;
        this.salesDate = salesDate;
        this.products = products;
    }
}
