package com.uwaterloo.smartpantry;
import com.uwaterloo.smartpantry.inventory.Item;

import org.junit.Test;

import static org.junit.Assert.*;

public class ItemFactoryUnitTest {
    @Test
    public void testFoodFactory() {
        Item food = ItemFactory.getItem("FOOD");
        // expected value is 212

        // use this method because float is not precise
        assertEquals(food.getName(), null);
    }

}
