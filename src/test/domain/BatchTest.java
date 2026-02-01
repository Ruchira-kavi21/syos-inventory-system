package test.domain;

import com.syos.domain.Batch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class BatchTest {

    private Batch batch;
    private LocalDate purchaseDate;
    private LocalDate expiryDate;

    @BeforeEach
    void setUp() {
        purchaseDate = LocalDate.now().minusDays(10);
        expiryDate = LocalDate.now().plusYears(1);

        batch = new Batch("B001", purchaseDate, expiryDate, 50);
    }

    @Test
    void testGetBatchNumber() {
        assertEquals("B001", batch.getBatchNumber());
    }

    @Test
    void testGetPurchaseDate() {
        assertEquals(purchaseDate, batch.getDateOfPurchase());
    }

    @Test
    void testGetExpiryDate() {
        assertEquals(expiryDate, batch.getExpiryDate());
    }

    @Test
    void testGetQuantity() {
        assertEquals(50, batch.getQuantity());
    }


    @Test
    void testDecreaseQuantityNormal() {
        batch.decreaseQuantity(10);
        assertEquals(40, batch.getQuantity());
    }

    @Test
    void testDecreaseQuantityToZero() {
        batch.decreaseQuantity(50);
        assertEquals(0, batch.getQuantity());
    }

    @Test
    void testDecreaseQuantityZero() {
        // Decrease by 0 should change nothing
        batch.decreaseQuantity(0);
        assertEquals(50, batch.getQuantity());
    }

    @Test
    void testDecreaseQuantityMoreThanAvailable() {
        // Your code throws an exception when stock is low. This test now PASSES if that happens.
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            batch.decreaseQuantity(100);
        });

    }

    @Test
    void testExpiryCheck() {
        LocalDate today = LocalDate.now();
        assertTrue(batch.getExpiryDate().isAfter(today), "Batch should not be expired yet");
    }
}