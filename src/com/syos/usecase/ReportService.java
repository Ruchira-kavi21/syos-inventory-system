package com.syos.usecase;

import com.syos.domain.Bill;
import com.syos.domain.BillItem;
import com.syos.domain.Item;
import com.syos.infrastructure.StoreData;

import java.time.format.DateTimeFormatter;

public class ReportService {
    private final StoreData storeData = StoreData.getInstance();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    //Detailed sales breakdown
    public void generateDailySalesReport() {
        printHeader("REPORT 1: DAILY ITEM SALES BREAKDOWN");
        System.out.printf("%-20s | %-10s | %-10s | %-10s%n", "Item Name", "Qty Sold", "Price", "Total");
        System.out.println("------------------------------------------------------------");

        double totalRevenue = 0;
        int totalItemsSold = 0;

        for (Bill bill : storeData.getTransactionHistory()) {
            for (BillItem bItem : bill.getItems()) {
                System.out.printf("%-20s | %-10d | %-10.2f | %-10.2f%n",
                        bItem.getItemName(), bItem.getQuantity(), bItem.getUnitPrice(), bItem.getTotalPrice());
                totalItemsSold += bItem.getQuantity();
            }
            totalRevenue += bill.getTotalAmount();
        }

        System.out.println("------------------------------------------------------------");
        System.out.printf("TOTAL REVENUE: %.2f  |  ITEMS SOLD: %d%n", totalRevenue, totalItemsSold);
        System.out.println("============================================================");
    }

    //Stock valuation
    public void generateStockValueReport() {
        printHeader("REPORT 2: CURRENT STOCK VALUATION");
        System.out.printf("%-20s | %-10s | %-10s | %-12s%n", "Item Name", "Total Qty", "Unit Price", "Asset Value");
        System.out.println("------------------------------------------------------------");

        double totalStockValue = 0;

        for (Item item : storeData.getInventory().values()) {
            double value = item.getTotalQuantity() * item.getPrice();
            System.out.printf("%-20s | %-10d | %-10.2f | %-12.2f%n",
                    item.getName(), item.getTotalQuantity(), item.getPrice(), value);
            totalStockValue += value;
        }

        System.out.println("------------------------------------------------------------");
        System.out.printf("TOTAL INVENTORY VALUE: %.2f%n", totalStockValue);
        System.out.println("============================================================");
    }

    //Reshelving
    public void generateReshelvingReport() {
        printHeader("REPORT 3: SHELF RE-STOCKING LIST");
        System.out.printf("%-20s | %-10s | %-10s | %-10s%n", "Item Name", "On Shelf", "Capacity", "To Fill");
        System.out.println("------------------------------------------------------------");

        boolean actionNeeded = false;
        for (Item item : storeData.getInventory().values()) {
            int currentShelf = item.getShelfQuantity();
            // FIX: Use item.getCap() or the database capacity column.
            // If getCap() is missing in Item.java, assume standard capacity of 20 for this report
            int maxCap = 20; // Or item.getShelfCapacity() if you added that getter
            int needed = maxCap - currentShelf;

            if (needed > 0 && item.getStoreQuantity() > 0) {
                // Only ask to move if we actually have stock in the back!
                int canMove = Math.min(needed, item.getStoreQuantity());
                System.out.printf("%-20s | %-10d | %-10d | %-10d%n",
                        item.getName(), currentShelf, maxCap, canMove);
                actionNeeded = true;
            }
        }
        if (!actionNeeded) System.out.println("No shelving actions required. Shelves are full.");
        System.out.println("============================================================");
    }

    //Low stock
    public void generateLowStockReport() {
        printHeader("REPORT 4: LOW STOCK ALERTS (Re-Order Level: 50)");
        System.out.printf("%-20s | %-12s | %-10s%n", "Item Name", "Current Stock", "Status");
        System.out.println("------------------------------------------------------------");

        boolean allGood = true;
        for (Item item : storeData.getInventory().values()) {
            if (item.getTotalQuantity() < 50) {
                System.out.printf("%-20s | %-12d | %-10s%n",
                        item.getName(), item.getTotalQuantity(), "ORDER NOW");
                allGood = false;
            }
        }
        if (allGood) System.out.println("All stock levels are healthy (>50).");
        System.out.println("============================================================");
    }

    //Transaction log
    public void generateTransactionSummary() {
        printHeader("REPORT 5: TRANSACTION HISTORY LOG");
        System.out.printf("%-10s | %-10s | %-10s | %-10s%n", "Bill ID", "Time", "Items", "Total");
        System.out.println("------------------------------------------------------------");

        for (Bill bill : storeData.getTransactionHistory()) {
            int itemCount = bill.getItems().stream().mapToInt(BillItem::getQuantity).sum();
            System.out.printf("%-10s | %-10s | %-10d | %-10.2f%n",
                    bill.getBillNumber(),
                    bill.getDate().format(formatter),
                    itemCount,
                    bill.getTotalAmount());
        }
        System.out.println("============================================================");
    }

    private void printHeader(String title) {
        System.out.println("\n");
        System.out.println("============================================================");
        System.out.println("   " + title);
        System.out.println("============================================================");
    }
}