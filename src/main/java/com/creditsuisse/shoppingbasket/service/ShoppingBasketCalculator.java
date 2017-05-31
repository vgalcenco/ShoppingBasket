package com.creditsuisse.shoppingbasket.service;

import com.creditsuisse.shoppingbasket.domain.PricedBasket;
import com.creditsuisse.shoppingbasket.domain.Discount;
import com.creditsuisse.shoppingbasket.domain.PricedItem;
import com.creditsuisse.shoppingbasket.domain.ShoppingBasket;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by vgalcenco on 5/30/17.
 */
public class ShoppingBasketCalculator {

    private PricesService pricesService;
    private DiscountsService discountsService;

    public ShoppingBasketCalculator(PricesService pricesService, DiscountsService discountsService) {
        this.pricesService = pricesService;
        this.discountsService = discountsService;
    }

    public PricedBasket calculateBasketPrice(ShoppingBasket shoppingBasket) {

        List<PricedItem> pricedItems = priceShoppingBasketItems(shoppingBasket);
        List<Discount> discounts = findApplicableDiscounts(pricedItems);

        BigDecimal totalPrice = pricedItems.stream().map(PricedItem::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalDiscounts = discounts.stream().map(Discount::getDiscountValue).reduce(BigDecimal.ZERO, BigDecimal::add);

        return new PricedBasket(totalPrice.subtract(totalDiscounts), pricedItems, discounts);
    }

    private List<Discount> findApplicableDiscounts(List<PricedItem> pricedItems) {
        return pricedItems.stream()
                .collect(Collectors.groupingBy(PricedItem::getItemName)).entrySet().stream()
                .map(entry -> discountsService.getCurrentDiscount(
                        entry.getKey(), //item name
                        entry.getValue().size(), // quantity
                        entry.getValue().get(0).getPrice()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private List<PricedItem> priceShoppingBasketItems(ShoppingBasket shoppingBasket) {
        return shoppingBasket.getAllItems()
                .parallelStream()
                .map(itemName -> new PricedItem(itemName, pricesService.getCurrentPriceForItem(itemName)))
                .collect(Collectors.toList());
    }

}
