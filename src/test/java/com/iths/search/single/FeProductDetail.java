package com.iths.search.single;

import org.apache.solr.client.solrj.beans.Field;

import java.math.BigDecimal;

/**
 * @author sen.huang
 * @date 2019/5/14.
 */
public class FeProductDetail {

    @Field("id")
    private String id;

    @Field("order_id")
    private String orderId;

    @Field("product_name")
    private String productName;

    @Field("product_price")
    private Double productPrice;

    @Field("description")
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
