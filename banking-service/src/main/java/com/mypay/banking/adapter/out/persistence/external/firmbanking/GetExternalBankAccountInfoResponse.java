package com.mypay.banking.adapter.out.persistence.external.firmbanking;

import lombok.Data;
import lombok.Getter;

@Data
public class GetExternalBankAccountInfoResponse {
    private String bankName;
    private String bankAccountNumber;
    private boolean isValid;

    public GetExternalBankAccountInfoResponse(String bankName, String bankAccountNumber, boolean isValid) {
        this.bankName = bankName;
        this.bankAccountNumber = bankAccountNumber;
        this.isValid = isValid;
    }
}
