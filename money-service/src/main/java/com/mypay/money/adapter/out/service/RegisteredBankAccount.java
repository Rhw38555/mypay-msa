package com.mypay.money.adapter.out.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisteredBankAccount {

    private String registeredBankAccountId;
    private String membershipId;
    private String bankName;
    private String bankAccountNumber;
    private boolean linkedStatusIsValid; // 통신 연결 상태
    private String aggregateIdentifier; // saga aggregateIdentifier

}
