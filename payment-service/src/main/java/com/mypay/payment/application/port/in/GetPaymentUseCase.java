package com.mypay.payment.application.port.in;

import com.mypay.payment.domain.Payment;

import java.util.List;

public interface GetPaymentUseCase {
    // TODO : command -> start date end date
    List<Payment> getNormalStatusPayments();
}
