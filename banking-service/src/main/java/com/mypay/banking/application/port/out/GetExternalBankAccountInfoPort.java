package com.mypay.banking.application.port.out;

import com.mypay.banking.adapter.out.persistence.external.firmbanking.GetExternalBankAccountInfoRequest;
import com.mypay.banking.adapter.out.persistence.external.firmbanking.GetExternalBankAccountInfoResponse;

public interface GetExternalBankAccountInfoPort {


    GetExternalBankAccountInfoResponse getExternalBankAccountInfo (GetExternalBankAccountInfoRequest request);
}
