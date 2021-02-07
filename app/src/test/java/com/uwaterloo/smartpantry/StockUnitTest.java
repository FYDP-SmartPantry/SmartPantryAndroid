package com.uwaterloo.smartpantry;
import com.uwaterloo.smartpantry.inventory.Stock;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNotEquals;

import org.junit.After;
import org.junit.Before;

public class StockUnitTest {
    @Test
    public void StockCompareTest() {
        Stock A = new Stock("Sample", 2);
        Stock B = new Stock("Sample", 3);
        Stock C = new Stock("do", 2);
        Stock D = new Stock("Sample", 2);
        assertFalse(A.equals(B));
        assertFalse(A.equals(C));
        assertTrue(A.equals(D));
    }
}
