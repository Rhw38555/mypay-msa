package com.mypay.remittance.adapter.out.service.money;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mypay.common.CommonHttpClient;
import com.mypay.common.ExternalAdapter;
import com.mypay.remittance.adapter.out.service.membership.Membership;
import com.mypay.remittance.adapter.out.service.membership.MembershipStatus;
import com.mypay.remittance.application.port.out.banking.BankingPort;
import com.mypay.remittance.application.port.out.money.MoneyInfo;
import com.mypay.remittance.application.port.out.money.MoneyPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@ExternalAdapter
public class MoneyServiceAdapter implements MoneyPort {

    private final CommonHttpClient commonHttpClient;

    private final String moneyServiceEndpoint;

    public MoneyServiceAdapter(CommonHttpClient commonHttpClient,
                               @Value("${service.money.url}") String moneyServiceEndpoint){
        this.commonHttpClient = commonHttpClient;
        this.moneyServiceEndpoint = moneyServiceEndpoint;
    }

    @Override
    public MoneyInfo getMoneyInfo(String membershipId) {
        return null;
    }

    @Override
    public boolean requestMoneyRecharging(String membershipId, int amount) {
        return false;
    }

    @Override
    public boolean requestMoneyIncrease(String membershipId, int amount) {
        return false;
    }

    @Override
    public boolean requestMoneyDecrease(String membershipId, int amount) {
        return false;
    }
}
