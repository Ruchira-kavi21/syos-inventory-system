package test.domain;

import com.syos.domain.Batch;
import com.syos.domain.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    private Item item;
    private Batch batch1;

    @BeforeEach
    void setUp() {
        item = new Item("C001", "Coke", 150.0, 20);
        batch1 = new Batch("B1", LocalDate.now(), LocalDate.of(2025, 12, 31), 50);
    }

    @Test
    void testItemCodeCorrect() { assertEquals("C001", item.getCode()); }

    @Test
    void testItemNameCorrect() { assertEquals("Coke", item.getName()); }

    @Test
    void testItemPriceCorrect() { assertEquals(150.0, item.getPrice()); }

    @Test
    void testItemShelfCapacityCorrect() {

    }

    @Test
    void testAddBatchIncreasesStoreQty() {
        item.addBatchToStore(batch1);
        assertEquals(50, item.getStoreQuantity());
    }

    @Test
    void testAddBatchUpdatesTotalQty() {
        item.addBatchToStore(batch1);
        assertEquals(50, item.getTotalQuantity());
    }

    @Test
    void testInitialStoreQtyIsZero() {
        assertEquals(0, item.getStoreQuantity());
    }

    @Test
    void testInitialShelfQtyIsZero() {
        assertEquals(0, item.getShelfQuantity());
    }

    @Test
    void testShelfBatchesListIsEmptyInitially() {
        assertTrue(item.getShelfBatches().isEmpty());
    }

    @Test
    void testReduceStockSmallAmount() {
        setupStock();
        item.reduceStock(5);
        assertEquals(20, item.getShelfQuantity(), "Shelf should auto-refill to max capacity");


        assertEquals(25, item.getStoreQuantity(), "Back store should decrease to refill the shelf");
    }

    @Test
    void testReduceStockExactAmount() {
        setupStock();

        item.reduceStock(5);


        assertEquals(20, item.getShelfQuantity(), "Shelf should auto-refill to max");


        assertEquals(25, item.getStoreQuantity(), "Back store stock should decrease to refill shelf");
    }

    @Test
    void testReduceStockMultipleTimes() {
        setupStock();
        item.reduceStock(5);
        item.reduceStock(5);
        assertEquals(20, item.getShelfQuantity());


        assertEquals(20, item.getStoreQuantity());
    }

    @Test
    void testReduceStockUpdatesStoreAfterRefill() {
        setupStock();
        item.reduceStock(20);

        assertEquals(10, item.getStoreQuantity());
    }

    @Test
    void testReduceStockInsufficientTotal() {
        setupStock();
        assertThrows(IllegalArgumentException.class, () -> item.reduceStock(100));
    }

    @Test
    void testReduceStockNegative() {
        setupStock();

    }

    @Test
    void testReduceStockZero() {
        setupStock();
        item.reduceStock(0);
        assertEquals(20, item.getShelfQuantity());
    }

    private void setupStock() {
        item.addBatchToStore(batch1);
        item.restockShelf();
    }
}