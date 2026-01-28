package com.syos.usecase;

import com.syos.domain.Bill;
import com.syos.domain.Cart;

public interface BillingService {
    Bill processTransaction(Cart cart, double cashTendered);
}
