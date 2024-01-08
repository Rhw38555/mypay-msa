package com.mypay.payment.application.port.out;

public interface GetMembershipPort {
    public MembershipStatus getMembership(String membershipId);
}
