package com.gghouse.wardah.wardahba.webservices.model;

/**
 * Created by michael on 3/21/2017.
 */

public class Product {
    private Long productId;
    private Integer quantity;

    public Product(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
