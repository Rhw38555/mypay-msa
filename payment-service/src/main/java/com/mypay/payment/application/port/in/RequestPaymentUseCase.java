package com.mypay.payment.application.port.in;

import com.mypay.payment.domain.Payment;

public interface RequestPaymentUseCase {
    Payment requestPayment(RequestPaymentCommand command);
}
