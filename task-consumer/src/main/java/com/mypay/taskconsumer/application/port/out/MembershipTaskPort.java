package com.mypay.taskconsumer.application.port.out;

public interface MembershipTaskPort {

    public boolean validMembershipTask(String membershipId);
    public boolean validMembershipStatusTask(String membershipId);
}
