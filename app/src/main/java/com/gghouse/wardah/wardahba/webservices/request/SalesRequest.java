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
    /**
     * new parameters
     */
    private Integer numberOfPiece;
    private Integer numberOfVisitor;
    private Integer numberOfBill;
    private Integer buyingPower;

    public SalesRequest(Long userId, Double salesAmount, Long salesDate, List<Product> products) {
        this.userId = userId;
        this.salesAmount = salesAmount;
        this.salesDate = salesDate;
        this.products = products;
    }

    /**
     * @param userId
     * @param salesAmount
     * @param salesDate
     * @param products
     * @param numberOfPiece
     * @param numberOfVisitor
     * @param numberOfBill
     * @param buyingPower
     */
    public SalesRequest(Long userId, Double salesAmount, Long salesDate, List<Product> products, Integer numberOfPiece, Integer numberOfVisitor, Integer numberOfBill, Integer buyingPower) {
        this.userId = userId;
        this.salesAmount = salesAmount;
        this.salesDate = salesDate;
        this.products = products;
        this.numberOfPiece = numberOfPiece;
        this.numberOfVisitor = numberOfVisitor;
        this.numberOfBill = numberOfBill;
        this.buyingPower = buyingPower;
    }
}
