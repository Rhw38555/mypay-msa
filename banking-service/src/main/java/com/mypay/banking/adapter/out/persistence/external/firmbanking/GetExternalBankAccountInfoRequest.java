package com.mypay.banking.adapter.out.persistence.external.firmbanking;

import lombok.Data;

@Data
public class GetExternalBankAccountInfoRequest {
    private String bankName;

    private String bankAccountNumber;

    public GetExternalBankAccountInfoRequest(String bankName, String bankAccountNumber) {
        this.bankName = bankName;
        this.bankAccountNumber = bankAccountNumber;
    }
}
