package com.mypay.payment.application.port.out;

import com.mypay.payment.domain.Payment;

import java.util.List;

public interface GetPaymentPort {
    List<Payment> getPayment();
}
