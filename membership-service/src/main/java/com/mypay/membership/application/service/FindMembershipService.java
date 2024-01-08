package com.mypay.membership.application.service;

import com.mypay.common.UseCase;
import com.mypay.membership.adapter.out.persistence.MembershipJpaEntity;
import com.mypay.membership.adapter.out.persistence.MembershipMapper;
import com.mypay.membership.application.port.in.FindMembershipCommand;
import com.mypay.membership.application.port.in.FindMembershipListByAddressCommand;
import com.mypay.membership.application.port.in.FindMembershipUseCase;
import com.mypay.membership.application.port.out.FindMembershipPort;
import com.mypay.membership.domain.Membership;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@UseCase
@Transactional
public class FindMembershipService implements FindMembershipUseCase {

    private final FindMembershipPort findMembershipPort;

    private final MembershipMapper membershipMapper;
    @Override
    public Membership findMembership(FindMembershipCommand command) {
        MembershipJpaEntity entity = findMembershipPort.findMembership(new Membership.MembershipId(command.getMembershipId()));
        return membershipMapper.mapToDomainEntity(entity);
    }

    @Override
    public List<Membership> findMembershipListByAddress(FindMembershipListByAddressCommand command) {
        List<MembershipJpaEntity> membershipJpaEntities = findMembershipPort.findMembershipListByAddress(new Membership.MembershipAddress(command.getAddressName()));
        return membershipJpaEntities
                .stream()
                .map(membershipMapper::mapToDomainEntity)
                .collect(Collectors.toList());
    }
}