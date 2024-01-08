package com.mypay.remittance.adapter.out.service.membership;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mypay.common.CommonHttpClient;
import com.mypay.remittance.application.port.out.membership.GetMembershipPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MembershipServiceAdapter implements GetMembershipPort {

    private final CommonHttpClient commonHttpClient;

    private final String membershipServiceEndpoint;

    public MembershipServiceAdapter(CommonHttpClient commonHttpClient,
                                    @Value("${service.membership.url}") String membershipServiceEndpoint){
        this.commonHttpClient = commonHttpClient;
        this.membershipServiceEndpoint = membershipServiceEndpoint;
    }

    @Override
    public MembershipStatus getMembership(String membershipId) {

        String url = String.join("/", membershipServiceEndpoint);
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
