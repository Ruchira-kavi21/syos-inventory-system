package test.domain;

import com.syos.domain.Bill;
import com.syos.domain.TransactionType;
import com.syos.infrastructure.BillDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class BillDAOTest {

    private BillDAO billDAO;
    private Random random;

    @BeforeEach
    void setUp() {
        billDAO = new BillDAO();
        random = new Random();
    }

    private String generateShortId() {
        return "B-" + (1000 + random.nextInt(9000));
    }


    @Test
    void testSaveBill_Standard() {
        String billId = generateShortId();
        Bill bill = new Bill.BillBuilder()
                .setBillNumber(billId)
                .setDate(LocalDateTime.now())
                .setTotalAmount(150.0)
                .setCashReceived(200.0)
                .setChangeAmount(50.0)
                .setType(TransactionType.IN_STORE)
                .build();

        assertDoesNotThrow(() -> billDAO.saveBill(bill));
    }

    @Test
    void testSaveBill_ZeroValues() {
        String billId = generateShortId();
        Bill bill = new Bill.BillBuilder()
                .setBillNumber(billId)
                .setDate(LocalDateTime.now())
                .setTotalAmount(0.0)
                .setCashReceived(0.0)
                .setType(TransactionType.IN_STORE)
                .build();

        assertDoesNotThrow(() -> billDAO.saveBill(bill));
    }

    @Test
    void testSaveBill_LargeAmount() {
        String billId = generateShortId();
        Bill bill = new Bill.BillBuilder()
                .setBillNumber(billId)
                .setDate(LocalDateTime.now())
                .setTotalAmount(9999.0)
                .setCashReceived(10000.0)
                .setType(TransactionType.IN_STORE)
                .build();

        assertDoesNotThrow(() -> billDAO.saveBill(bill));
    }

    @Test
    void testSaveBill_OnlineType() {

        String billId = generateShortId();
        Bill bill = new Bill.BillBuilder()
                .setBillNumber(billId)
                .setDate(LocalDateTime.now())
                .setTotalAmount(50.0)
                .setType(TransactionType.ONLINE) // Testing Enum support
                .build();

        assertDoesNotThrow(() -> billDAO.saveBill(bill));
    }
}