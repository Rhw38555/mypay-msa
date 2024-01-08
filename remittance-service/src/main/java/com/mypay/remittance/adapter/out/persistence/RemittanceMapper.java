package com.mypay.remittance.adapter.out.persistence;

import com.mypay.remittance.domain.RemittanceRequest;
import org.springframework.stereotype.Component;

@Component
public class RemittanceMapper {
    public RemittanceRequest mapToDomainEntity(RemittanceRequestJpaEntity remittanceRequestJpaEntity) {
        return RemittanceRequest.generateRemittanceRequest(
                new RemittanceRequest.RemittanceRequestId(remittanceRequestJpaEntity.getRemittanceRequestId()+""),
                new RemittanceRequest.RemittanceFromMembershipId(remittanceRequestJpaEntity.getFromMembershipId()),
                new RemittanceRequest.ToBankName(remittanceRequestJpaEntity.getToBankName()),
                new RemittanceRequest.ToBankAccountNumber(remittanceRequestJpaEntity.getToBankAccountNumber()),
                new RemittanceRequest.RemittanceRequestType(remittanceRequestJpaEntity.getRemittanceType()),
                new RemittanceRequest.RemittanceRequestStatus(remittanceRequestJpaEntity.getRemittanceStatus()),
                new RemittanceRequest.Amount(remittanceRequestJpaEntity.getAmount())
        );
    }
}
