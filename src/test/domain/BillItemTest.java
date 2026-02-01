package test.domain;

import com.syos.domain.BillItem;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BillItemTest {


    @Test
    void testGetQuantity_Normal() {
        BillItem bi = new BillItem("Milk", 5, 10.0, 50.0);
        assertEquals(5, bi.getQuantity());
    }

    @Test
    void testGetQuantity_Zero() {
        BillItem bi = new BillItem("Milk", 0, 10.0, 0.0);
        assertEquals(0, bi.getQuantity());
    }

    @Test
    void testGetQuantity_Large() {
        BillItem bi = new BillItem("Milk", 1000, 10.0, 10000.0);
        assertEquals(1000, bi.getQuantity());
    }

    @Test
    void testGetQuantity_Negative() {
        BillItem bi = new BillItem("Milk", -5, 10.0, -50.0);
        assertEquals(-5, bi.getQuantity());
    }


    @Test
    void testGetItemName_Normal() {
        BillItem bi = new BillItem("Coke", 1, 1.0, 1.0);
        assertEquals("Coke", bi.getItemName());
    }

    @Test
    void testGetItemName_EmptyString() {
        BillItem bi = new BillItem("", 1, 1.0, 1.0);
        assertEquals("", bi.getItemName());
    }

    @Test
    void testGetItemName_Null() {
        BillItem bi = new BillItem(null, 1, 1.0, 1.0);
        assertNull(bi.getItemName());
    }

    @Test
    void testGetUnitPrice_Integer() {
        BillItem bi = new BillItem("X", 1, 100.0, 100.0);
        assertEquals(100.0, bi.getUnitPrice());
    }

    @Test
    void testGetUnitPrice_Decimal() {
        BillItem bi = new BillItem("X", 1, 10.55, 10.55);
        assertEquals(10.55, bi.getUnitPrice());
    }

    @Test
    void testGetUnitPrice_Zero() {
        BillItem bi = new BillItem("X", 1, 0.0, 0.0);
        assertEquals(0.0, bi.getUnitPrice());
    }
}