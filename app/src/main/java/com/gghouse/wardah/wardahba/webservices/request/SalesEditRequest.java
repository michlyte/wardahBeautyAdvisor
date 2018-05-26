package com.gghouse.wardah.wardahba.webservices.request;

import com.gghouse.wardah.wardahba.webservices.model.Product;

import java.util.List;

/**
 * Created by michaelhalim on 7/25/17.
 */

public class SalesEditRequest {
    private Long salesId;
    private Long userId;
    private Double salesAmount;
    private List<Product> products;

    public SalesEditRequest(Long salesId, Long userId, Double salesAmount, List<Product> products) {
        this.salesId = salesId;
        this.userId = userId;
        this.salesAmount = salesAmount;
        this.products = products;
    }
}
