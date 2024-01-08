package com.mypay.taskconsumer.adapter.out.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mypay.common.CommonHttpClient;
import com.mypay.taskconsumer.application.port.out.MembershipTaskPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MembershipTaskAdapter implements MembershipTaskPort {

    private final CommonHttpClient commonHttpClient;

    private final String membershipServiceUrl;

    public MembershipTaskAdapter(CommonHttpClient commonHttpClient,
                                    @Value("${service.membership.url}") String membershipServiceUrl){
        this.commonHttpClient = commonHttpClient;
        this.membershipServiceUrl = membershipServiceUrl;
    }

    @Override
    public boolean validMembershipTask(String membershipId) {
        String url = String.join("/", membershipServiceUrl) + "/membership/" + membershipId;
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
    public boolean validMembershipStatusTask(String membershipId) {
        String url = String.join("/", membershipServiceUrl) + "/membership/" + membershipId;
        try{
            String jsonResponse = commonHttpClient.sendGetRequest(url).body();

            // json Membership
            ObjectMapper mapper = new ObjectMapper();
            Membership membership = mapper.readValue(jsonResponse, Membership.class);

            if(membership.isCorp()){
                return true;
            }

        }catch (Exception e){
            throw new RuntimeException(e);
        }

        return false;
    }
}
