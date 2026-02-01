package test.domain;

import com.syos.domain.Bill;
import com.syos.domain.Item;
import com.syos.infrastructure.StoreData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class StoreDataTest {

    private StoreData storeData;

    @BeforeEach
    void setUp() {
        storeData = StoreData.getInstance();
        storeData.getInventory().clear();
        storeData.getTransactionHistory().clear();
    }


    @Test
    void testGetInstance_NotNull() {
        assertNotNull(StoreData.getInstance());
    }

    @Test
    void testGetInstance_IsSingleton() {
        StoreData s1 = StoreData.getInstance();
        StoreData s2 = StoreData.getInstance();
        assertSame(s1, s2, "Should return the same instance memory address");
    }

    @Test
    void testGetInstance_StatePersistence() {
        StoreData s1 = StoreData.getInstance();
        s1.getInventory().put("TEST", new Item("TEST", "T", 1.0, 1));

        StoreData s2 = StoreData.getInstance();
        assertTrue(s2.getInventory().containsKey("TEST"), "Changes should be visible across instances");
    }


    @Test
    void testInventory_StartsEmpty() {
        assertTrue(storeData.getInventory().isEmpty());
    }

    @Test
    void testInventory_AddItem() {
        Item item = new Item("C1", "Coke", 10.0, 10);
        storeData.getInventory().put(item.getCode(), item);
        assertEquals(1, storeData.getInventory().size());
    }

    @Test
    void testInventory_RetrieveItem() {
        Item item = new Item("C1", "Coke", 10.0, 10);
        storeData.getInventory().put(item.getCode(), item);

        Item retrieved = storeData.getInventory().get("C1");
        assertEquals("Coke", retrieved.getName());
    }

    @Test
    void testInventory_RetrieveNonExistent() {
        assertNull(storeData.getInventory().get("GHOST"), "Should return null for missing items");
    }


    @Test
    void testHistory_StartsEmpty() {
        assertTrue(storeData.getTransactionHistory().isEmpty());
    }

    @Test
    void testAddBill_IncreasesSize() {
        storeData.addBill(new Bill.BillBuilder().setBillNumber("B1").build());
        assertEquals(1, storeData.getTransactionHistory().size());
    }

    @Test
    void testAddBill_PreservesOrder() {
        storeData.addBill(new Bill.BillBuilder().setBillNumber("FIRST").build());
        storeData.addBill(new Bill.BillBuilder().setBillNumber("SECOND").build());

        assertEquals("FIRST", storeData.getTransactionHistory().get(0).getBillNumber());
        assertEquals("SECOND", storeData.getTransactionHistory().get(1).getBillNumber());
    }

    @Test
    void testAddBill_StoresCorrectData() {
        double total = 55.55;
        storeData.addBill(new Bill.BillBuilder().setBillNumber("B1").setTotalAmount(total).build());

        assertEquals(total, storeData.getTransactionHistory().get(0).getTotalAmount());
    }
}