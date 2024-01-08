package com.mypay.payment.adapter.in.web;

import com.mypay.common.WebAdapter;
import com.mypay.payment.application.port.in.*;
import com.mypay.payment.domain.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class RequestPaymentController {
    private final RequestPaymentUseCase requestPaymentUseCase;
    private final GetPaymentUseCase getPaymentUseCase;
    private final FinishSettlementPaymentUseCase finishSettlementPaymentUseCase;

    @PostMapping(path = "/payments")
    Payment requestPayment(@RequestBody PaymentRequest request) {
        return requestPaymentUseCase.requestPayment(
                new RequestPaymentCommand(
                        request.getRequestMembershipId(),
                        request.getRequestPrice(),
                        request.getFranchiseId(),
                        request.getFranchiseFeeRate()
                )
        );
    }

    @GetMapping(path = "/payments")
    List<Payment> listPaymentsByPeriod() {
        return getPaymentUseCase.getNormalStatusPayments();
    }

    @PostMapping(path = "/payments/{paymentId}/finish")
    void findPaymentByPaymentId(@PathVariable String paymentId) {
        finishSettlementPaymentUseCase.finishPayment(
                new FinishSettlementPaymentCommand(
                        paymentId
                )
        );
    }
}
