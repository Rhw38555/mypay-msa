package com.mypay.money.application.port.out;


import com.mypay.money.adapter.out.service.MembershipStatus;

public interface GetMembershipPort {

    public MembershipStatus getMembership(String membershipId);

}
