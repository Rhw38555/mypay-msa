package com.mypay.banking.adapter.in.web;

import com.mypay.banking.FirmBankingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateFirmbankingRequest {
    private String firmbankingRequestAggregateIdentifier;
    private FirmBankingStatus status;

    // TODO 금액 변경 등 ..
}
