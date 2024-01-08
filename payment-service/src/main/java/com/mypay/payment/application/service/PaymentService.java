package com.mypay.payment.application.service;

import com.mypay.common.UseCase;
import com.mypay.payment.application.port.in.*;
import com.mypay.payment.application.port.out.*;
import com.mypay.payment.domain.Payment;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;
import java.util.List;

@UseCase
@RequiredArgsConstructor
@Transactional
public class PaymentService implements RequestPaymentUseCase, GetPaymentUseCase, FinishSettlementPaymentUseCase {

    private final CreatePaymentPort createPaymentPort;
    private final GetPaymentPort getPaymentPort;
    private final ChangePaymentPort changePaymentPort;

    private final GetMembershipPort getMembershipPort;
    private final GetRegisteredBankAccountPort getRegisteredBankAccountPort;

    @Override
    public Payment requestPayment(RequestPaymentCommand command) {

        // 충전도, 멤버십, 머니 유효성 확인.....
        // getMembershipPort.getMembership(command.getRequestMembershipId());

        //getRegisteredBankAccountPort.getRegisteredBankAccount(command.getRequestMembershipId());

        //....

        // Todo Money Service -> Member Money 정보를 가져오기 위한 Port

        // createPaymentPort
        return createPaymentPort.createPayment(
                command.getRequestMembershipId(),
                command.getRequestPrice(),
                command.getFranchiseId(),
                command.getFranchiseFeeRate());
    }

    @Override
    public List<Payment> getNormalStatusPayments() {
        return getPaymentPort.getPayment();
    }

    @Override
    public void finishPayment(FinishSettlementPaymentCommand command) {
        changePaymentPort.changePaymentRequestStatus(command.getPaymentId(), 2);
    }
}
