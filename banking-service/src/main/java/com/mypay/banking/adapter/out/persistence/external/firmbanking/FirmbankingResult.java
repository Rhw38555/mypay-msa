package com.mypay.banking.adapter.out.persistence.external.firmbanking;

import com.mypay.banking.FirmBankingStatus;
import lombok.Data;

@Data
public class FirmbankingResult {

    private FirmBankingStatus resultCode; // 0: 성공, 1: 실패

    public FirmbankingResult(FirmBankingStatus resultCode) {
        this.resultCode = resultCode;
    }
}
