package com.mypay.money.query.application.port.out;


public interface GetMemberAddressInfoPort {
    MemberAddressInfo getMemberAddressInfo(
            String membershipId
    );
}
