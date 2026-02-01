package test.domain;

import com.syos.domain.Batch;
import com.syos.domain.Bill;
import com.syos.domain.BillItem;
import com.syos.domain.Item;
import com.syos.infrastructure.StoreData;
import com.syos.usecase.ReportService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReportServiceTest {

    private ReportService reportService;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));

        StoreData.getInstance().getInventory().clear();
        StoreData.getInstance().getTransactionHistory().clear();

        reportService = new ReportService();
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void testDailySales_Empty() {
        reportService.generateDailySalesReport();
        String output = outContent.toString();

        assertTrue(output.contains("DAILY ITEM SALES BREAKDOWN"));
        assertTrue(output.contains("TOTAL REVENUE: 0.00"));
    }

    @Test
    void testDailySales_WithData() {
        List<BillItem> items = new ArrayList<>();
        items.add(new BillItem("Coke", 2, 100.0, 200.0));

        Bill bill = new Bill.BillBuilder()
                .setBillNumber("B1")
                .setTotalAmount(200.0)
                .addItemList(items)
                .setDate(LocalDateTime.now())
                .build();

        StoreData.getInstance().addBill(bill);

        reportService.generateDailySalesReport();
        String output = outContent.toString();

        assertTrue(output.contains("Coke"));
        assertTrue(output.contains("200.00")); // Check total revenue
    }

    @Test
    void testDailySales_MultipleBills() {
        StoreData.getInstance().addBill(new Bill.BillBuilder().setTotalAmount(100.0).addItemList(new ArrayList<>()).build());
        StoreData.getInstance().addBill(new Bill.BillBuilder().setTotalAmount(50.0).addItemList(new ArrayList<>()).build());

        reportService.generateDailySalesReport();

        assertTrue(outContent.toString().contains("150.00"));
    }


    @Test
    void testStockValue_EmptyInventory() {
        reportService.generateStockValueReport();
        assertTrue(outContent.toString().contains("TOTAL INVENTORY VALUE: 0.00"));
    }

    @Test
    void testStockValue_NormalItems() {
        Item coke = new Item("C1", "Coke", 100.0, 50);
        coke.addBatchToStore(new Batch("B1", LocalDate.now(), LocalDate.now().plusMonths(1), 50));
        StoreData.getInstance().getInventory().put(coke.getCode(), coke);

        reportService.generateStockValueReport();

        assertTrue(outContent.toString().contains("5000.00"));
    }

    @Test
    void testStockValue_ZeroQuantityItem() {
        Item emptyItem = new Item("E1", "Empty", 500.0, 50);
        StoreData.getInstance().getInventory().put(emptyItem.getCode(), emptyItem);

        reportService.generateStockValueReport();

        assertTrue(outContent.toString().contains("0.00"));
    }


    @Test
    void testReshelving_NoActionNeeded() {
        Item item = new Item("C1", "Coke", 10.0, 20);
        item.addBatchToStore(new Batch("B1", LocalDate.now(), LocalDate.now(), 50));
        item.restockShelf(); // Fills shelf to 20

        StoreData.getInstance().getInventory().put(item.getCode(), item);

        reportService.generateReshelvingReport();
        assertTrue(outContent.toString().contains("No shelving actions required"));
    }

    @Test
    void testReshelving_ActionNeeded() {
        Item item = new Item("C1", "Coke", 10.0, 20);
        item.addBatchToStore(new Batch("B1", LocalDate.now(), LocalDate.now(), 50));

        StoreData.getInstance().getInventory().put(item.getCode(), item);

        reportService.generateReshelvingReport();
        assertTrue(outContent.toString().contains("20"));
    }

    @Test
    void testReshelving_StoreEmpty() {
        Item item = new Item("C1", "Coke", 10.0, 20);
        StoreData.getInstance().getInventory().put(item.getCode(), item);

        reportService.generateReshelvingReport();
        assertTrue(outContent.toString().contains("No shelving actions required"));
    }


    @Test
    void testLowStock_AllHealthy() {
        Item item = new Item("C1", "Coke", 10.0, 50);
        item.addBatchToStore(new Batch("B1", LocalDate.now(), LocalDate.now(), 100));
        StoreData.getInstance().getInventory().put(item.getCode(), item);

        reportService.generateLowStockReport();
        assertTrue(outContent.toString().contains("All stock levels are healthy"));
    }

    @Test
    void testLowStock_Alert() {
        Item item = new Item("C1", "Coke", 10.0, 50);
        item.addBatchToStore(new Batch("B1", LocalDate.now(), LocalDate.now(), 10)); // Only 10
        StoreData.getInstance().getInventory().put(item.getCode(), item);

        reportService.generateLowStockReport();
        assertTrue(outContent.toString().contains("ORDER NOW"));
    }

    @Test
    void testLowStock_Boundary() {
        Item item49 = new Item("C1", "Low", 10.0, 50);
        item49.addBatchToStore(new Batch("B1", LocalDate.now(), LocalDate.now(), 49));
        StoreData.getInstance().getInventory().put(item49.getCode(), item49);

        reportService.generateLowStockReport();
        assertTrue(outContent.toString().contains("ORDER NOW"));
    }


    @Test
    void testTransactionSummary_Empty() {
        reportService.generateTransactionSummary();
        assertTrue(outContent.toString().contains("TRANSACTION HISTORY LOG"));
    }

    @Test
    void testTransactionSummary_WithData() {
        StoreData.getInstance().addBill(new Bill.BillBuilder()
                .setBillNumber("T100")
                .setDate(LocalDateTime.now())
                .setTotalAmount(99.99)
                .addItemList(new ArrayList<>())
                .build());

        reportService.generateTransactionSummary();
        assertTrue(outContent.toString().contains("T100"));
        assertTrue(outContent.toString().contains("99.99"));
    }

    @Test
    void testTransactionSummary_Formatting() {
        reportService.generateTransactionSummary();
        assertTrue(outContent.toString().contains("Bill ID"));
        assertTrue(outContent.toString().contains("Time"));
    }
}