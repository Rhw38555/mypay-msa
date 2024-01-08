package com.mypay.payment.adapter.out.persistence;

import com.mypay.common.PersistenceAdapter;
import com.mypay.payment.application.port.out.ChangePaymentPort;
import com.mypay.payment.application.port.out.CreatePaymentPort;
import com.mypay.payment.application.port.out.GetPaymentPort;
import com.mypay.payment.domain.Payment;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class PaymentPersistenceAdapter implements CreatePaymentPort, GetPaymentPort, ChangePaymentPort {
    private final SpringDataPaymentRepository paymentRepository;
    private final PaymentMapper mapper;
    @Override
    public Payment createPayment(String requestMembershipId, String requestPrice, String franchiseId, String franchiseFeeRate) {
        PaymentJpaEntity jpaEntity = paymentRepository.save(
                new PaymentJpaEntity(
                        requestMembershipId,
                        Integer.parseInt(requestPrice),
                        franchiseId,
                        franchiseFeeRate,
                        0, // 0: 승인, 1: 실패, 2: 정산 완료.
                        null
                )
        );
        return mapper.mapToDomainEntity(jpaEntity);
    }

    @Override
    public List<Payment> getPayment() {
        List<PaymentJpaEntity> paymentJpaEntityList = paymentRepository.findByPaymentStatus(0);
        if(paymentJpaEntityList != null){
            return paymentRepository.findByPaymentStatus(0)
                    .stream()
                    .map(mapper::mapToDomainEntity)
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public void changePaymentRequestStatus(String paymentId, int status) {
        Optional<PaymentJpaEntity> paymentJpaEntity = paymentRepository.findById(Long.parseLong(paymentId));
        if(paymentJpaEntity.isPresent()){
            paymentJpaEntity.get().setPaymentStatus(status);
            paymentRepository.save(paymentJpaEntity.get());
        }
    }
}
