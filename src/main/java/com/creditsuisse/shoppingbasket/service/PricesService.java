package com.creditsuisse.shoppingbasket.service;

import java.math.BigDecimal;

/**
 * Created by vgalcenco on 5/30/17.
 */
public interface PricesService {
    BigDecimal getCurrentPriceForItem(String itemName);
}
