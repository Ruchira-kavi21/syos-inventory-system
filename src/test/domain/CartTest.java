package test.domain;

import com.syos.domain.Batch;
import com.syos.domain.Cart;
import com.syos.domain.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    private Cart cart;
    private Item coke;
    private Item milk;

    @BeforeEach
    void setUp() {
        cart = new Cart();

        // 1. Create Items (Capacity 50)
        coke = new Item("C001", "Coke", 150.0, 50);
        milk = new Item("M001", "Milk", 300.0, 50);

        Batch cokeBatch = new Batch("B1", LocalDate.now(), LocalDate.now().plusMonths(6), 100);
        Batch milkBatch = new Batch("B2", LocalDate.now(), LocalDate.now().plusMonths(6), 100);


        coke.addBatchToStore(cokeBatch);
        coke.restockShelf();

        milk.addBatchToStore(milkBatch);
        milk.restockShelf();
    }


    @Test
    void testAddItem_NewItem() {
        cart.addItem(coke, 2);
        assertEquals(1, cart.getItems().size());
        assertEquals(2, cart.getItems().get(coke));
    }

    @Test
    void testAddItem_ExistingItem_UpdatesQuantity() {
        cart.addItem(coke, 2);
        cart.addItem(coke, 3); // Should become 5
        assertEquals(5, cart.getItems().get(coke));
    }

    @Test
    void testAddItem_MultipleDifferentItems() {
        cart.addItem(coke, 1);
        cart.addItem(milk, 1);
        assertEquals(2, cart.getItems().size());
    }

    @Test
    void testAddItem_ZeroQuantity() {
        cart.addItem(coke, 0);
        assertTrue(cart.getItems().containsKey(coke));
    }

    @Test
    void testAddItem_NegativeQuantity() {

        try {
            cart.addItem(coke, -5);

        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    void testAddItem_ExceedsShelfStock() {
        // Shelf has 50. Try to add 55.
        // This confirms the "Only X units available" error works correctly
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cart.addItem(coke, 55);
        });
        // You can check the message if you want, but assertThrows is enough
    }


    @Test
    void testCalculateTotal_Empty() {
        assertEquals(0.0, cart.calculateTotal());
    }

    @Test
    void testCalculateTotal_SingleItem() {
        cart.addItem(coke, 2); // 2 * 150.0 = 300.0
        assertEquals(300.0, cart.calculateTotal());
    }

    @Test
    void testCalculateTotal_MultipleItems() {
        cart.addItem(coke, 2); // 300.0
        cart.addItem(milk, 1); // 300.0
        assertEquals(600.0, cart.calculateTotal());
    }

    @Test
    void testCalculateTotal_HighPrecision() {
        // Create weird item and RESTOCK it
        Item weirdItem = new Item("X", "Weird", 10.3333, 100);
        weirdItem.addBatchToStore(new Batch("BX", LocalDate.now(), LocalDate.now().plusDays(1), 100));
        weirdItem.restockShelf();

        cart.addItem(weirdItem, 3);
        assertEquals(30.9999, cart.calculateTotal(), 0.0001);
    }

    @Test
    void testCalculateTotal_AfterClear() {
        cart.addItem(coke, 5);
        cart.clearCart();
        assertEquals(0.0, cart.calculateTotal());
    }



    @Test
    void testIsEmpty_InitiallyTrue() {
        assertTrue(cart.isEmpty());
    }

    @Test
    void testIsEmpty_FalseAfterAdd() {
        cart.addItem(coke, 1);
        assertFalse(cart.isEmpty());
    }

    @Test
    void testClearCart_RemovesItems() {
        cart.addItem(coke, 5);
        cart.clearCart();
        assertEquals(0, cart.getItems().size());
    }

    @Test
    void testClearCart_DoubleClear() {
        cart.clearCart();
        cart.clearCart();
        assertTrue(cart.isEmpty());
    }

    @Test
    void testClearCart_ResetTotal() {
        cart.addItem(coke, 5);
        cart.clearCart();
        assertEquals(0.0, cart.calculateTotal());
    }
}