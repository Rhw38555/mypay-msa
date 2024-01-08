package com.mypay.banking.application.port.out;

import com.mypay.banking.adapter.out.service.MembershipStatus;

public interface GetMembershipPort {

    public MembershipStatus getMembership(String membershipId);

}
