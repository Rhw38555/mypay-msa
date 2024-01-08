package com.mypay.remittance.application.port.out.membership;


import com.mypay.remittance.adapter.out.service.membership.MembershipStatus;

public interface GetMembershipPort {

    public MembershipStatus getMembership(String membershipId);

}
