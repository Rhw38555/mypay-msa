package com.mypay.taskconsumer.adapter.out.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mypay.common.CommonHttpClient;
import com.mypay.taskconsumer.application.port.out.BankingTaskPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BankingTaskAdapter implements BankingTaskPort {

    private final CommonHttpClient commonHttpClient;

    private final String bankingServiceUrl;

    public BankingTaskAdapter(CommonHttpClient commonHttpClient,
                                 @Value("${service.banking.url}") String bankingServiceUrl){
        this.commonHttpClient = commonHttpClient;
        this.bankingServiceUrl = bankingServiceUrl;
    }

    @Override
    public boolean validBankingAccountTask(String membershipId) {

        String url = String.join("/", bankingServiceUrl) + "/banking/accounts/" + membershipId;
        try{
            String jsonResponse = commonHttpClient.sendGetRequest(url).body();
            // json Membership
            ObjectMapper mapper = new ObjectMapper();
            Membership membership = mapper.readValue(jsonResponse, Membership.class);

            if(membership.isValid()){
                return true;
            }

        }catch (Exception e){
            throw new RuntimeException(e);
        }

        return false;
    }

    @Override
    public boolean getExternalBankAccountInfoTask() {

        return true;
    }
}
