package com.creditsuisse.shoppingbasket.service;

import com.creditsuisse.shoppingbasket.domain.Discount;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by vgalcenco on 5/31/17.
 */
public class DiscountServiceImpl implements DiscountsService {

    private Map<String, DiscountRule> activeDiscounts = new HashMap<>();

    public Optional<Discount> getCurrentDiscount(String itemName, int quantity, BigDecimal itemPrice) {
        if (activeDiscounts.containsKey(itemName)) {
            Discount discount = activeDiscounts.get(itemName).apply(quantity, itemPrice);
            return (BigDecimal.ZERO.compareTo(discount.discountValue) == 0) ? Optional.empty() : Optional.of(discount);
        } else {
            return Optional.empty();
        }
    }

    public void startDiscount(String itemName, DiscountRule rule) {
        activeDiscounts.put(itemName, rule);
    }

    interface DiscountRule {
        Discount apply(int quantity, BigDecimal itemPrice);
    }

    public static DiscountRule TWO_FOR_ONE = (quantity, itemPrice) -> {
        BigDecimal discount = itemPrice.multiply(BigDecimal.valueOf(quantity).divideToIntegralValue(new BigDecimal(2)));
        return new Discount(discount, "buy one get one free");
    };

    public static DiscountRule THREE_FOR_TWO = (quantity, itemPrice) -> {
        BigDecimal discount = itemPrice.multiply(BigDecimal.valueOf(quantity).divideToIntegralValue(new BigDecimal(3)));
        return new Discount(discount, "three for the price of two");
    };

}
