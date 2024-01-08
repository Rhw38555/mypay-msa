package com.mypay.remittance.adapter.in.web;

import com.mypay.remittance.RemittanceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestRemittanceRequest {
    // 송금 요청을 위한 정보
    private String fromMembershipId;

    private String toMembershipId;

    private String toBankName;

    private String toBankAccountNumber;

    private boolean isValid;

    private RemittanceType remittanceType; // 내부, 외부

    private int amount;
}