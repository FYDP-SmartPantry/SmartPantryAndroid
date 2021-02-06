package com.uwaterloo.smartpantry;
import com.uwaterloo.smartpantry.inventory.Item;
import com.uwaterloo.smartpantry.inventory.ItemFactory;

import org.junit.Test;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ItemFactoryUnitTest {
    @Test
    public void testFoodFactory() {
        Item food = ItemFactory.getItem("FOOD");
        // expected value is 212

        // use this method because float is not precise
        assertEquals(food.getName(), null);
    }

}
