package com.mypay.payment.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinishSettlementPaymentCommand {
    private String paymentId;
}
