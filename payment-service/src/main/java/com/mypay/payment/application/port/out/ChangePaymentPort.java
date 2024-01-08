package com.mypay.payment.application.port.out;

import com.mypay.payment.domain.Payment;

public interface ChangePaymentPort {
    void changePaymentRequestStatus(String paymentId, int status);
}
