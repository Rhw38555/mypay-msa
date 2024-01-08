package com.mypay.remittance.application.port.in;


import com.mypay.remittance.domain.RemittanceRequest;

public interface RequestRemittanceUseCase {
    RemittanceRequest requestRemittance(RequestRemittanceCommand command);

}
