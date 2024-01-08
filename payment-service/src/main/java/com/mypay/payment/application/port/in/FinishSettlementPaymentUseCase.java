package com.mypay.payment.application.port.in;

import com.mypay.payment.domain.Payment;

import java.util.List;

public interface FinishSettlementPaymentUseCase {

    void finishPayment(FinishSettlementPaymentCommand command);
}
