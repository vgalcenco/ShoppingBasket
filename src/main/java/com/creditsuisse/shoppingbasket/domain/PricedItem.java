package com.creditsuisse.shoppingbasket.domain;

import java.math.BigDecimal;

/**
 * Created by vgalcenco on 5/30/17.
 */
public class PricedItem {

    private String itemName;

    private BigDecimal price;

    public PricedItem(String itemName, BigDecimal price) {
        this.itemName = itemName;
        this.price = price;
    }

    public String getItemName() {
        return itemName;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
