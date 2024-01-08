package com.mypay.banking.application.port.out;

import com.mypay.banking.adapter.out.persistence.RegisteredBankAccountJpaEntity;
import com.mypay.banking.adapter.out.persistence.RequestFirmbankingJpaEntity;
import com.mypay.banking.domain.FirmbankingRequest;
import com.mypay.banking.domain.RegisteredBankAccount;

import java.util.List;

public interface RequestFirmbankingPort {

    RequestFirmbankingJpaEntity createFirmbankingRequest(
            FirmbankingRequest.FromBankName fromBankName,
            FirmbankingRequest.FromBankAccountNumber fromBankAccountNumber,
            FirmbankingRequest.ToBankName toBankName,
            FirmbankingRequest.ToBankAccountNumber toBankAccountNumber,
            FirmbankingRequest.MoneyAmount moneyAmount,
            FirmbankingRequest.FirmStatus firmbankingStatus,
            FirmbankingRequest.AggregateIdentifier aggregateIdentifier
    );

    RequestFirmbankingJpaEntity modifyFirmbankingRequest(
            RequestFirmbankingJpaEntity entity
    );

    List<RequestFirmbankingJpaEntity> getFirmbankingRequest(
            FirmbankingRequest.AggregateIdentifier aggregateIdentifier
    );

}
