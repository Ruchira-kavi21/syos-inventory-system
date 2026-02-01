package com.syos.infrastructure;

import com.syos.domain.Batch;
import com.syos.domain.Cart;
import com.syos.domain.Item;
import com.syos.domain.Bill;
import com.syos.infrastructure.DatabaseConnection;
import com.syos.infrastructure.ItemDAO;
import com.syos.infrastructure.StoreData;
import com.syos.usecase.BillingSystem;
import com.syos.usecase.ReportService;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        StoreData storeData = StoreData.getInstance();
        ItemDAO itemDAO = new ItemDAO();
        BillingSystem billingSystem = new BillingSystem();
        ReportService reportService = new ReportService();

        // Load items from DB into memory
        List<Item> items = itemDAO.loadAllItems();
        for (Item item : items) {
            storeData.getInventory().put(item.getCode(), item);
            item.restockShelf();
        }

        Cart cart = new Cart();
        boolean running = true;

        System.out.println("=== SYOS Inventory & Billing System ===");

        while (running) {
            System.out.println("\n1. View Items");
            System.out.println("2. Add Item to Cart");
            System.out.println("3. Checkout");
            System.out.println("4. Reports");
            System.out.println("5. Clear Cart");
            System.out.println("6. Exit");
            System.out.print("Select option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1 -> {
                    System.out.println("\n--- AVAILABLE ITEMS & BATCH DETAILS ---");
                    for (Item item : storeData.getInventory().values()) {
                        System.out.println("\n📦 " + item.getName() + " [" + item.getCode() + "] (Total Shelf: " + item.getShelfQuantity() + ")");

                        for (Batch b : item.getShelfBatches()) {
                            System.out.printf("   - Batch: %-10s | Exp: %-10s | Qty: %d%n",
                                    b.getBatchNumber(), b.getExpiryDate(), b.getQuantity());
                        }
                    }
                }

                case 2 -> {
                    System.out.print("Enter Item Code: ");
                    String code = scanner.nextLine();

                    Item item = storeData.getInventory().get(code);
                    if (item == null) {
                        System.out.println("Invalid item code.");
                        break;
                    }

                    System.out.print("Enter quantity: ");
                    int qty = scanner.nextInt();
                    scanner.nextLine();

                    try {
                        cart.addItem(item, qty);
                        System.out.println("Item added to cart.");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                case 3 -> {
                    if (cart.isEmpty()) {
                        System.out.println("Cart is empty!");
                        break;
                    }
                    showCartSummary(cart);

                    double total = cart.calculateTotal();
                    System.out.println("------------------------------------------------");
                    System.out.println("TOTAL AMOUNT: " + total);
                    System.out.println("------------------------------------------------");

                    System.out.print("Enter cash amount: ");
                    double cash = scanner.nextDouble();

                    try {
                        Bill bill = billingSystem.processTransaction(cart, cash);
                        System.out.println("Payment successful!");
                        System.out.println("Change: " + bill.getChangeAmount());
                        cart.clearCart();
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                }


                case 4 -> {
                    System.out.println("\n--- MANAGER REPORT SUITE ---");
                    System.out.println("1. Daily Sales Breakdown");
                    System.out.println("2. Stock Valuation Report");
                    System.out.println("3. Shelf Restocking Report");
                    System.out.println("4. Low Stock / Re-Order Report");
                    System.out.println("5. Transaction History Log");
                    System.out.print("Select report: ");

                    int r = scanner.nextInt();
                    scanner.nextLine();

                    switch (r) {
                        case 1 -> reportService.generateDailySalesReport();
                        case 2 -> reportService.generateStockValueReport();
                        case 3 -> reportService.generateReshelvingReport();
                        case 4 -> reportService.generateLowStockReport();
                        case 5 -> reportService.generateTransactionSummary();
                        default -> System.out.println("Invalid report option.");
                    }
                }

                case 5 -> {
                    cart.clearCart();
                    System.out.println("Cart cleared.");
                }
                case 6 -> {
                    running = false;
                    System.out.println("Exiting system...");
                }

                default -> System.out.println("Invalid option.");
            }
        }

        scanner.close();
    }
    private static void showCartSummary(Cart cart) {
        System.out.println("\n--- BILL PREVIEW ---");
        cart.getItems().forEach((item, qty) -> {
            double lineTotal = item.getPrice() * qty;
            System.out.printf("%-20s | Qty: %d | Price: %.2f | Total: %.2f%n",
                    item.getName(), qty, item.getPrice(), lineTotal);
        });
        System.out.println("--------------------------------");
        System.out.printf("TOTAL AMOUNT: %.2f%n", cart.calculateTotal());
    }

}