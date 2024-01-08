package com.mypay.money.adapter.in.web;

import com.mypay.money.domain.MoneyChangingRequest;
import org.springframework.stereotype.Component;

@Component
public class MoneyChangingResultDetailMapper {

    public static MoneyChangingResultDetail mapToMoneyChangingResultDetail(MoneyChangingRequest moneyChangingRequest) {
        return new MoneyChangingResultDetail(
                moneyChangingRequest.getMoneyChangingRequestId(),
                moneyChangingRequest.getChangingType(),
                moneyChangingRequest.getChangingMoneyStatus(),
                moneyChangingRequest.getChangingMoneyAmount()
        );
    }
}
