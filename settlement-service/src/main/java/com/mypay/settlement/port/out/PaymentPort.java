package com.mypay.settlement.port.out;

import com.mypay.settlement.adapter.out.service.Payment;

import java.util.List;

public interface PaymentPort {
    List<Payment> getNormalStatusPayments(); // membershipId = franchiseId 간주.

    // 타겟 계좌, 금액
    void finishSettlement(Long paymentId);
}
