package com.creditsuisse.shoppingbasket.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by vgalcenco on 5/30/17.
 */
public class ShoppingBasket {

    private List<String> items = new ArrayList<String>();

    public void add(String apple) {
        items.add(apple);
    }

    public List<String> getAllItems() {
        return Collections.unmodifiableList(items);
    }
}
