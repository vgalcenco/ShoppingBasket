package com.creditsuisse.shoppingbasket.domain;

import java.math.BigDecimal;

/**
 * Created by vgalcenco on 5/31/17.
 */
public class Discount {
    public BigDecimal discountValue;
    public String discountDescription;

    public Discount(BigDecimal discountValue, String discountDescription) {
        this.discountValue = discountValue;
        this.discountDescription = discountDescription;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public String getDiscountDescription() {
        return discountDescription;
    }
}
