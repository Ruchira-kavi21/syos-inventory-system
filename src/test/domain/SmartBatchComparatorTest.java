package test.domain;

import com.syos.domain.Batch;
import com.syos.domain.SmartBatchComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class SmartBatchComparatorTest {

    private SmartBatchComparator comparator;
    private LocalDate now;

    @BeforeEach
    void setUp() {
        comparator = new SmartBatchComparator();
        now = LocalDate.now();
    }


    @Test
    void testCompare_DifferentExpiryDates() {
        Batch earlyExpiry = new Batch("B1", now, now.plusMonths(1), 10);

        Batch lateExpiry = new Batch("B2", now, now.plusYears(1), 10);

        int result = comparator.compare(earlyExpiry, lateExpiry);
        assertTrue(result < 0, "Earlier expiry should be sorted first");
    }

    @Test
    void testCompare_DifferentExpiryDates_Reverse() {
        // Swap the order to prove it works both ways
        Batch earlyExpiry = new Batch("B1", now, now.plusMonths(1), 10);
        Batch lateExpiry = new Batch("B2", now, now.plusYears(1), 10);

        int result = comparator.compare(lateExpiry, earlyExpiry);
        assertTrue(result > 0, "Later expiry should be sorted last");
    }



    @Test
    void testCompare_SameExpiry_DifferentPurchaseDate() {
        LocalDate expiry = now.plusMonths(12);

        Batch oldStock = new Batch("OLD", now.minusDays(1), expiry, 10);

        Batch newStock = new Batch("NEW", now, expiry, 10);

        int result = comparator.compare(oldStock, newStock);
        assertTrue(result < 0, "If expiry is same, older purchase date should come first");
    }

    @Test
    void testCompare_SameExpiry_SamePurchase() {
        // Identical dates
        Batch b1 = new Batch("B1", now, now, 10);
        Batch b2 = new Batch("B2", now, now, 10);

        int result = comparator.compare(b1, b2);
        assertEquals(0, result, "Identical dates should be considered equal");
    }


    @Test
    void testCompare_NullBatch1() {
        Batch b2 = new Batch("B2", now, now, 10);

        assertThrows(NullPointerException.class, () -> comparator.compare(null, b2));
    }
}