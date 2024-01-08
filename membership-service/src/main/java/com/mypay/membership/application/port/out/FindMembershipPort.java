package com.mypay.membership.application.port.out;

import com.mypay.membership.adapter.out.persistence.MembershipJpaEntity;
import com.mypay.membership.domain.Membership;

import java.util.List;

public interface FindMembershipPort {
    MembershipJpaEntity findMembership(
            Membership.MembershipId membershipId
    );

    List<MembershipJpaEntity> findMembershipListByAddress(
            Membership.MembershipAddress membershipAddress
    );
}
