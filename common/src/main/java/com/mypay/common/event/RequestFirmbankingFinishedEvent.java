package com.mypay.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestFirmbankingFinishedEvent {
    private String requestFirmbankingId;
    private String rechargingRequestId;
    private String membershipId;
    private String fromBankName;
    private String fromBankAccountNumber;
    private String toBankName;
    private String toBankAccountNumber;
    private int moneyAmount;
    private int status;
    private String requestFirmbankingAggregateIdentifier;
}
