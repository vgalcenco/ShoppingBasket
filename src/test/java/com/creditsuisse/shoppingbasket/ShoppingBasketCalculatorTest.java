package com.creditsuisse.shoppingbasket;

import com.creditsuisse.shoppingbasket.domain.PricedBasket;
import com.creditsuisse.shoppingbasket.domain.Discount;
import com.creditsuisse.shoppingbasket.domain.ShoppingBasket;
import com.creditsuisse.shoppingbasket.service.DiscountsService;
import com.creditsuisse.shoppingbasket.service.PricesService;
import com.creditsuisse.shoppingbasket.service.ShoppingBasketCalculator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by vgalcenco on 5/30/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class ShoppingBasketCalculatorTest {

    @Mock
    private PricesService pricesService;

    @Mock
    private DiscountsService discountsService;

    @InjectMocks
    private ShoppingBasketCalculator calculator;

    @Test
    public void calculateEmptyBasketPriceShouldReturnTotalPriceZero() throws Exception {
        PricedBasket bp = calculator.calculateBasketPrice(new ShoppingBasket());

        assertThat(bp.getTotalPrice(), equalTo(BigDecimal.ZERO));
    }

    @Test
    public void calculateBasketPriceWithOneItemShouldReturnItemsPrice() throws Exception {

        ShoppingBasket basket = new ShoppingBasket();
        basket.add("Apple");

        doReturn(new BigDecimal("0.35")).when(pricesService).getCurrentPriceForItem("Apple");
        doReturn(Optional.empty()).when(discountsService).getCurrentDiscount(anyString(), anyInt(), any());

        PricedBasket bp = calculator.calculateBasketPrice(basket);

        assertThat(bp.getTotalPrice(), equalTo(new BigDecimal("0.35")));

    }

    @Test
    public void calculateBasketPriceWithMultipleItemsShouldReturnTheSumOfAllItemPrices() throws Exception {

        ShoppingBasket basket = new ShoppingBasket();
        basket.add("Apple");
        basket.add("Banana");
        basket.add("Banana");
        basket.add("Melon");

        doReturn(new BigDecimal("0.35")).when(pricesService).getCurrentPriceForItem("Apple");
        doReturn(new BigDecimal("0.20")).when(pricesService).getCurrentPriceForItem("Banana");
        doReturn(new BigDecimal("0.50")).when(pricesService).getCurrentPriceForItem("Melon");

        doReturn(Optional.empty()).when(discountsService).getCurrentDiscount(anyString(), anyInt(), any());

        PricedBasket bp = calculator.calculateBasketPrice(basket);

        assertThat(bp.getTotalPrice(), equalTo(new BigDecimal("1.25")));

    }

    @Test
    public void calculateBasketShouldApplyDiscountsIfSuchAreActive() throws Exception {

        ShoppingBasket basket = new ShoppingBasket();
        basket.add("Apple");
        basket.add("Banana");
        basket.add("Melon");
        basket.add("Melon");
        basket.add("Lime");

        doReturn(new BigDecimal("0.35")).when(pricesService).getCurrentPriceForItem("Apple");
        doReturn(new BigDecimal("0.20")).when(pricesService).getCurrentPriceForItem("Banana");
        doReturn(new BigDecimal("0.50")).when(pricesService).getCurrentPriceForItem("Melon");
        doReturn(new BigDecimal("0.15")).when(pricesService).getCurrentPriceForItem("Lime");

        doReturn(Optional.empty()).when(discountsService).getCurrentDiscount("Apple", 1, new BigDecimal("0.35"));
        doReturn(Optional.empty()).when(discountsService).getCurrentDiscount("Banana", 1, new BigDecimal("0.20"));
        doReturn(Optional.of(new Discount(new BigDecimal("0.50"), "buy one get one free"))).when(discountsService).getCurrentDiscount("Melon", 2, new BigDecimal("0.50"));
        doReturn(Optional.empty()).when(discountsService).getCurrentDiscount("Lime", 1, new BigDecimal("0.15"));

        PricedBasket bp = calculator.calculateBasketPrice(basket);

        assertThat(bp.getTotalPrice(), equalTo(new BigDecimal("1.20")));
        assertThat(bp.getDiscounts().size(), equalTo(1));

    }

}