package com.mypay.remittance.adapter.out.service.banking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mypay.common.CommonHttpClient;
import com.mypay.common.ExternalAdapter;
import com.mypay.remittance.adapter.out.service.membership.Membership;
import com.mypay.remittance.adapter.out.service.membership.MembershipStatus;
import com.mypay.remittance.application.port.out.banking.BankingInfo;
import com.mypay.remittance.application.port.out.banking.BankingPort;
import com.mypay.remittance.application.port.out.money.MoneyPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@ExternalAdapter
public class BankingServiceAdapter implements BankingPort {

    private final CommonHttpClient commonHttpClient;

    private final String bankingServiceEndpoint;

    public BankingServiceAdapter(CommonHttpClient commonHttpClient,
                                 @Value("${service.banking.url}") String bankingServiceEndpoint){
        this.commonHttpClient = commonHttpClient;
        this.bankingServiceEndpoint = bankingServiceEndpoint;
    }

    @Override
    public BankingInfo getMembershipBankingInfo(String bankName, String bankAccount) {
        return null;
    }

    @Override
    public boolean requestFirmBanking(String bankName, String bankAccountNumber, int amount) {
        return false;
    }
}
