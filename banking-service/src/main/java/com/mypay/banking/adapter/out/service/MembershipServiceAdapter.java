package com.mypay.banking.adapter.out.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mypay.banking.application.port.out.GetMembershipPort;
import com.mypay.common.CommonHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MembershipServiceAdapter implements GetMembershipPort {

    private final CommonHttpClient commonHttpClient;

    private final String membershipServiceUrl;

    public MembershipServiceAdapter(CommonHttpClient commonHttpClient,
                                    @Value("${service.membership.url}") String membershipServiceUrl){
        this.commonHttpClient = commonHttpClient;
        this.membershipServiceUrl = membershipServiceUrl;
    }

    @Override
    public MembershipStatus getMembership(String membershipId) {

        String url = String.join("/", membershipServiceUrl);
        try{
            String jsonResponse = commonHttpClient.sendGetRequest(url).body();
            // json Membership
            ObjectMapper mapper = new ObjectMapper();
            Membership membership = mapper.readValue(jsonResponse, Membership.class);

            if(membership.isValid()){
                return new MembershipStatus(membership.getMembershipId(), true);
            }

        }catch (Exception e){
            throw new RuntimeException(e);
        }

        return null;
    }
}
