package com.mypay.banking.adapter.out.persistence;

import com.mypay.banking.domain.FirmbankingRequest;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RequestFirmbankingMapper {
    public FirmbankingRequest mapToDomainEntity(RequestFirmbankingJpaEntity requestFirmbankingJpaEntity, UUID uuid) {
        return FirmbankingRequest.generateFirmbankingRequest(
                new FirmbankingRequest.FirmbankingRequestId(requestFirmbankingJpaEntity.getRequestFirmbankingId()+""),
                new FirmbankingRequest.FromBankName(requestFirmbankingJpaEntity.getFromBankName()),
                new FirmbankingRequest.FromBankAccountNumber(requestFirmbankingJpaEntity.getFromBankAccountNumber()),
                new FirmbankingRequest.ToBankName(requestFirmbankingJpaEntity.getToBankName()),
                new FirmbankingRequest.ToBankAccountNumber(requestFirmbankingJpaEntity.getToBankAccountNumber()),
                new FirmbankingRequest.MoneyAmount(requestFirmbankingJpaEntity.getMoneyAmount()),
                new FirmbankingRequest.FirmStatus(requestFirmbankingJpaEntity.getFirmbankingStatus()),
                uuid,
                new FirmbankingRequest.AggregateIdentifier(requestFirmbankingJpaEntity.getAggregateIdentifier())
        );
    }
}
