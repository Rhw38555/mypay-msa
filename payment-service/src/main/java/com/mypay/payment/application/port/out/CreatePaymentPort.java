package com.mypay.payment.application.port.out;

import com.mypay.payment.domain.Payment;

public interface CreatePaymentPort {
    Payment createPayment(String requestMembershipId, String requestPrice, String franchiseId, String franchiseFeeRate);
}
