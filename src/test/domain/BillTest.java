package test.domain;

import com.syos.domain.Bill;
import com.syos.domain.TransactionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BillTest {


    @Test
    void testBillBuilderCreatesObject() {
        LocalDateTime now = LocalDateTime.now();
        Bill bill = new Bill.BillBuilder()
                .setBillNumber("B100")
                .setDate(now)
                .setTotalAmount(500.0)
                .build();

        assertNotNull(bill);
        assertEquals("B100", bill.getBillNumber());
        assertEquals(500.0, bill.getTotalAmount());
    }

    @ParameterizedTest
    @ValueSource(doubles = {10.0, 50.5, 100.0, 999.99, 0.0})
    void testTotalAmount(double amount) {
        Bill bill = new Bill.BillBuilder().setTotalAmount(amount).build();
        assertEquals(amount, bill.getTotalAmount());
    }

    @ParameterizedTest
    @ValueSource(doubles = {100.0, 200.0, 500.0, 1000.0, 5000.0})
    void testCashReceived(double cash) {
        Bill bill = new Bill.BillBuilder().setCashReceived(cash).build();
        assertEquals(cash, bill.getCashReceived());
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, 5.0, 10.5, 50.0, 100.0})
    void testChangeAmount(double change) {
        Bill bill = new Bill.BillBuilder().setChangeAmount(change).build();
        assertEquals(change, bill.getChangeAmount());
    }

    @ParameterizedTest
    @EnumSource(TransactionType.class)
    void testTransactionTypes(TransactionType type) {
        Bill bill = new Bill.BillBuilder().setType(type).build();
        assertEquals(type, bill.getType());
    }

    @Test
    void testBillNumberNotNull() {
        Bill bill = new Bill.BillBuilder().setBillNumber("B999").build();
        assertNotNull(bill.getBillNumber());
    }
}