package com.creditsuisse.shoppingbasket.service;

import com.creditsuisse.shoppingbasket.domain.Discount;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Created by vgalcenco on 5/31/17.
 */
public interface DiscountsService {

     Optional<Discount> getCurrentDiscount(String itemName, int quantity, BigDecimal price);

}
