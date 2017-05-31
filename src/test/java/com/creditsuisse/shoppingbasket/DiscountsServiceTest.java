package com.creditsuisse.shoppingbasket;

import com.creditsuisse.shoppingbasket.domain.Discount;
import com.creditsuisse.shoppingbasket.service.DiscountServiceImpl;
import com.creditsuisse.shoppingbasket.service.DiscountsService;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by vgalcenco on 5/31/17.
 */
public class DiscountsServiceTest {

    @Test
    public void testNoDiscountOfferedForItemsThatHaveNoDiscountRegistered() throws Exception {
        DiscountsService discountsService = new DiscountServiceImpl();

        Optional<Discount> discount = discountsService.getCurrentDiscount("Apple", 1, BigDecimal.valueOf(0.35));

        assertThat(discount.isPresent(), is(equalTo(false)));
    }

    @Test
    public void testDiscountOfferedForItemsThatHaveBuyOneGetOneDiscountOnOffer() throws Exception {
        DiscountServiceImpl discountsService = new DiscountServiceImpl();
        discountsService.startDiscount("Melon", DiscountServiceImpl.TWO_FOR_ONE);

        Optional<Discount> discountOpt = discountsService.getCurrentDiscount("Melon", 2, BigDecimal.valueOf(0.5));
        assertThat(discountOpt.isPresent(), is(equalTo(true)));

        Discount discount = discountOpt.get();
        assertThat(discount.getDiscountValue(), is(equalTo(BigDecimal.valueOf(0.5))));
        assertThat(discount.getDiscountDescription(), is(equalTo("buy one get one free")));
    }

    @Test
    public void testBuyOneGetOneDiscountNotOfferedForItemsThatDontMeetTheRequiredMinimum() throws Exception {
        DiscountServiceImpl discountsService = new DiscountServiceImpl();
        discountsService.startDiscount("Melon", DiscountServiceImpl.TWO_FOR_ONE);

        Optional<Discount> discountOpt = discountsService.getCurrentDiscount("Melon", 1, BigDecimal.valueOf(0.5));
        assertThat(discountOpt.isPresent(), is(equalTo(false)));
    }

    @Test
    public void testBuyOneGetOneDiscountOfferedAppliedForEachMultipleOfTwo() throws Exception {
        DiscountServiceImpl discountsService = new DiscountServiceImpl();
        discountsService.startDiscount("Melon", DiscountServiceImpl.TWO_FOR_ONE);

        Optional<Discount> discountOpt = discountsService.getCurrentDiscount("Melon", 5, BigDecimal.valueOf(0.5));
        assertThat(discountOpt.isPresent(), is(equalTo(true)));

        Discount discount = discountOpt.get();
        assertThat(discount.getDiscountValue(), is(equalTo(BigDecimal.valueOf(1.0))));
        assertThat(discount.getDiscountDescription(), is(equalTo("buy one get one free")));
    }

    @Test
    public void testDiscountOfferedForItemsThatHaveThreeForTwoDiscountOnOffer() throws Exception {
        DiscountServiceImpl discountsService = new DiscountServiceImpl();
        discountsService.startDiscount("Lime", DiscountServiceImpl.THREE_FOR_TWO);

        Optional<Discount> discountOpt = discountsService.getCurrentDiscount("Lime", 3, BigDecimal.valueOf(0.15));
        assertThat(discountOpt.isPresent(), is(equalTo(true)));

        Discount discount = discountOpt.get();
        assertThat(discount.getDiscountValue(), is(equalTo(BigDecimal.valueOf(0.15).setScale(2))));
        assertThat(discount.getDiscountDescription(), is(equalTo("three for the price of two")));
    }


    @Test
    public void testThreeForOneDiscountNotOfferedForItemsThatDontMeetTheRequiredMinimum() throws Exception {
        DiscountServiceImpl discountsService = new DiscountServiceImpl();
        discountsService.startDiscount("Lime", DiscountServiceImpl.THREE_FOR_TWO);

        Optional<Discount> discountOpt = discountsService.getCurrentDiscount("Lime", 2, BigDecimal.valueOf(0.15));
        assertThat(discountOpt.isPresent(), is(equalTo(false)));
    }

    @Test
    public void testThreeForTwoDiscountOfferedOnlyForMultiplesOfThree() throws Exception {
        DiscountServiceImpl discountsService = new DiscountServiceImpl();
        discountsService.startDiscount("Lime", DiscountServiceImpl.THREE_FOR_TWO);

        Optional<Discount> discountOpt = discountsService.getCurrentDiscount("Lime", 10, BigDecimal.valueOf(0.15));
        assertThat(discountOpt.isPresent(), is(equalTo(true)));

        Discount discount = discountOpt.get();
        assertThat(discount.getDiscountValue(), is(equalTo(BigDecimal.valueOf(0.45).setScale(2))));
        assertThat(discount.getDiscountDescription(), is(equalTo("three for the price of two")));
    }

}