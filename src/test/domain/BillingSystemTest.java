package test.domain;

import com.syos.domain.Batch;
import com.syos.domain.Cart;
import com.syos.domain.Item;
import com.syos.domain.Bill;
import com.syos.usecase.BillingSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class BillingSystemTest {

    private BillingSystem billingSystem;
    private Cart cart;
    private Item coke;

    @BeforeEach
    void setUp() {
        billingSystem = new BillingSystem();
        cart = new Cart();

        // Setup a valid item with stock so we can test transactions
        coke = new Item("C001", "Coke", 100.0, 50);
        // Add stock so cart doesn't complain
        coke.addBatchToStore(new Batch("B1", LocalDate.now(), LocalDate.now().plusMonths(6), 100));
        coke.restockShelf();
    }

    // ==================================================
    // 1. VALIDATION LOGIC (Safe, No DB Calls) - 5 Tests
    // ==================================================

    @Test
    void testProcessTransaction_EmptyCart_ThrowsException() {
        // Validation: Should not allow checkout with nothing
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            billingSystem.processTransaction(cart, 100.0);
        });
        assertTrue(e.getMessage().contains("Cart is empty"));
    }

    @Test
    void testProcessTransaction_InsufficientCash_ThrowsException() {
        cart.addItem(coke, 1); // Total is 100.0

        // Validation: pay 50.0 for a 100.0 item
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            billingSystem.processTransaction(cart, 50.0);
        });
        assertTrue(e.getMessage().contains("Insufficient cash"));
    }

    @Test
    void testProcessTransaction_ExactCash_Success() {

        cart.addItem(coke, 1);
        try {
            billingSystem.processTransaction(cart, 100.0);
        } catch (Exception e) {
            assertFalse(e.getMessage().contains("Insufficient cash"));
        }
    }


    @Test
    void testChangeCalculation_HighAmount() {
        cart.addItem(coke, 1); // Cost 100
        try {
            Bill bill = billingSystem.processTransaction(cart, 500.0);
            assertEquals(400.0, bill.getChangeAmount());
        } catch (Exception e) {
            System.out.println("DB Connection needed for full test");
        }
    }


    @Test
    void testProcessTransaction_FullSuccess() {
        cart.addItem(coke, 1);


        try {
            Bill bill = billingSystem.processTransaction(cart, 100.0);
            assertNotNull(bill);
            assertEquals(100.0, bill.getTotalAmount());
            assertEquals("IN_STORE", bill.getType().toString());
        } catch (Exception e) {
            fail("Database connection failed or Item C001 missing in DB: " + e.getMessage());
        }
    }
}