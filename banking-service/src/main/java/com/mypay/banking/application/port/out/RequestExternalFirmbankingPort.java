package com.mypay.banking.application.port.out;

import com.mypay.banking.adapter.out.persistence.external.firmbanking.ExternalFirmbankingReqeust;
import com.mypay.banking.adapter.out.persistence.external.firmbanking.FirmbankingResult;

public interface RequestExternalFirmbankingPort {
    FirmbankingResult requestExternalFirmbanking(ExternalFirmbankingReqeust reqeust);
}
