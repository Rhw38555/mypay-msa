package com.mypay.membership.application.service;

import com.mypay.common.UseCase;
import com.mypay.membership.adapter.out.persistence.MembershipJpaEntity;
import com.mypay.membership.adapter.out.persistence.MembershipMapper;
import com.mypay.membership.application.port.in.RegisterMembershipCommand;
import com.mypay.membership.application.port.in.RegisterMembershipUseCase;
import com.mypay.membership.application.port.out.RegisterMembershipPort;
import com.mypay.membership.domain.Membership;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;

import javax.transaction.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional
public class RegisterMembershipService implements RegisterMembershipUseCase {

    private final RegisterMembershipPort registerMembershipPort;
    private final MembershipMapper membershipMapper;
    @Override
    public Membership registerMembership(RegisterMembershipCommand command) {
        MembershipJpaEntity jpaEntity = registerMembershipPort.createMembership(
                new Membership.MembershipName(command.getName()),
                new Membership.MembershipPassword(command.getPassword()),
                new Membership.MembershipEmail(command.getEmail()),
                new Membership.MembershipAddress(command.getAddress()),
                new Membership.MembershipIsValid(command.isValid()),
                new Membership.MembershipIsCorp(command.isCorp())
        );
        // entity -> Membership
        return membershipMapper.mapToDomainEntity(jpaEntity);
    }

}
