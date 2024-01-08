package com.mypay.banking.application.port.in;


import com.mypay.banking.domain.FirmbankingRequest;
import com.mypay.banking.domain.RegisteredBankAccount;

public interface RequestFirmbankingUseCase {
    FirmbankingRequest requestFirmbanking(RequestFirmbankingCommand command);
    void requestFirmbankingByEvent(RequestFirmbankingCommand command);
}
