package com.mypay.banking.adapter.out.persistence;

import com.mypay.banking.application.port.out.FindBankAccountPort;
import com.mypay.banking.application.port.out.RegisterBankAccountPort;
import com.mypay.banking.application.port.out.RequestFirmbankingPort;
import com.mypay.banking.domain.FirmbankingRequest;
import com.mypay.banking.domain.RegisteredBankAccount;
import com.mypay.common.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@PersistenceAdapter
@RequiredArgsConstructor
public class RequestFirmbankingPersistenceAdapter implements RequestFirmbankingPort {

    private final SpringDataFirmbankingRequestRepository firmbankingRequestRepository;


    @Override
    public RequestFirmbankingJpaEntity createFirmbankingRequest(FirmbankingRequest.FromBankName fromBankName, FirmbankingRequest.FromBankAccountNumber fromBankAccountNumber, FirmbankingRequest.ToBankName toBankName, FirmbankingRequest.ToBankAccountNumber toBankAccountNumber, FirmbankingRequest.MoneyAmount moneyAmount, FirmbankingRequest.FirmStatus firmbankingStatus, FirmbankingRequest.AggregateIdentifier aggregateIdentifier) {
        return firmbankingRequestRepository.save(
                new RequestFirmbankingJpaEntity(
                        fromBankName.getFromBankName(),
                        fromBankAccountNumber.getFromBankAccountNumber(),
                        toBankName.getToBankName(),
                        toBankAccountNumber.getToBankAccountNumber(),
                        moneyAmount.getMoneyAmount(),
                        firmbankingStatus.getFirmbankingStatus(),
                        UUID.randomUUID(),
                        aggregateIdentifier.getAggregateIdentifier()
                )
        );
    }

    @Override
    public RequestFirmbankingJpaEntity modifyFirmbankingRequest(RequestFirmbankingJpaEntity entity) {
        return firmbankingRequestRepository.save(entity);
    }


    @Override
    public List<RequestFirmbankingJpaEntity> getFirmbankingRequest(FirmbankingRequest.AggregateIdentifier aggregateIdentifier) {
        return firmbankingRequestRepository.findByAggregateIdentifer(aggregateIdentifier.getAggregateIdentifier());
    }
}
