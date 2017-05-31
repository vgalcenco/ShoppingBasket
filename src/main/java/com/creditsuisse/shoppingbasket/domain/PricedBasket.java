package com.creditsuisse.shoppingbasket.domain;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by vgalcenco on 5/30/17.
 */
public class PricedBasket {

    private BigDecimal totalPrice;
    private List<PricedItem> pricedItems;
    private List<Discount> discounts;

    public PricedBasket(BigDecimal totalPrice, List<PricedItem> pricedItems, List<Discount> discounts) {
        this.totalPrice = totalPrice;
        this.pricedItems = pricedItems;
        this.discounts = discounts;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public List<PricedItem> getPricedItems() {
        return pricedItems;
    }

    public List<Discount> getDiscounts() {
        return discounts;
    }
}
