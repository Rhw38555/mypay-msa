package com.mypay.money.adapter.in.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.mypay.money.ChangingMoneyStatus;
import com.mypay.money.ChangingType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoneyChangingResultDetail {

    private String moneyChangingRequestId;
    private ChangingType moneyChangingType; // enum 0:증액 1:감액
    private ChangingMoneyStatus changingMoneyStatus;
    private int amount;

}

