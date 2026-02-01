package test.domain;

import com.syos.domain.Item;
import com.syos.infrastructure.ItemDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ItemDAOTest {

    private ItemDAO itemDAO;

    @BeforeEach
    void setUp() {
        itemDAO = new ItemDAO();
    }

    @Test
    void testLoadAllItems_NotNull() {
        List<Item> items = itemDAO.loadAllItems();
        assertNotNull(items);
    }

    @Test
    void testLoadAllItems_ContainsData() {
        List<Item> items = itemDAO.loadAllItems();
        assertFalse(items.isEmpty());
    }

    @Test
    void testLoadAllItems_PropertiesSet() {
        List<Item> items = itemDAO.loadAllItems();
        if (!items.isEmpty()) {
            Item i = items.get(0);
            assertNotNull(i.getCode());
            assertNotNull(i.getName());
        }
    }


    @Test
    void testSync_ValidItem() {
        Item item = new Item("TEST-01", "Test", 1.0, 10);
        assertDoesNotThrow(() -> itemDAO.syncItemInventory(item));
    }

    @Test
    void testSync_ItemWithNoBatches() {
        Item item = new Item("TEST-02", "Empty", 1.0, 10);
        assertDoesNotThrow(() -> itemDAO.syncItemInventory(item));
    }

    @Test
    void testSync_NullItem() {
        assertThrows(NullPointerException.class, () -> itemDAO.syncItemInventory(null));
    }
}