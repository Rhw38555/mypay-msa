package com.mypay.money.adapter.out.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mypay.common.CommonHttpClient;
import com.mypay.money.application.port.out.GetRegisteredBankAccountPort;
import com.mypay.money.application.port.out.RegisteredBankAccountAggreateIdentifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BankingServiceAdapter implements GetRegisteredBankAccountPort {

    private final CommonHttpClient commonHttpClient;

    private final String bankingServiceUrl;

    public BankingServiceAdapter(CommonHttpClient commonHttpClient,  @Value("${service.banking.url}") String bankingServiceUrl) {
        this.commonHttpClient = commonHttpClient;
        this.bankingServiceUrl = bankingServiceUrl;
    }

    @Override
    public RegisteredBankAccountAggreateIdentifier getRegisterBankAccount(String membershipId) {
        String url = String.join("/", bankingServiceUrl, "banking/accounts", membershipId);
        try{

            int code = commonHttpClient.sendGetRequest(url).statusCode();
            String jsonResponse = commonHttpClient.sendGetRequest(url).body();

            // json Membership
            ObjectMapper mapper = new ObjectMapper();
            RegisteredBankAccount registeredBankAccount = mapper.readValue(jsonResponse, RegisteredBankAccount.class);

            return new RegisteredBankAccountAggreateIdentifier(
                          registeredBankAccount.getRegisteredBankAccountId(),
                    registeredBankAccount.getAggregateIdentifier(),
                    membershipId,
                    registeredBankAccount.getBankName(),
                    registeredBankAccount.getBankAccountNumber()
            );

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
