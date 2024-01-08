package com.mypay.membership.application.service;

import com.mypay.membership.adapter.out.persistence.MembershipJpaEntity;
import com.mypay.membership.adapter.out.persistence.MembershipMapper;
import com.mypay.membership.application.port.in.FindMembershipCommand;
import com.mypay.membership.application.port.in.FindMembershipListByAddressCommand;
import com.mypay.membership.application.port.in.ValidateTokenCommand;
import com.mypay.membership.application.port.out.FindMembershipPort;
import com.mypay.membership.domain.Membership;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class FindMembershipServiceTest {
    @InjectMocks
    private FindMembershipService findMembershipService;

    @Mock
    private FindMembershipPort findMembershipPort;

    @Mock
    private MembershipMapper membershipMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        findMembershipService = new FindMembershipService(findMembershipPort, membershipMapper);
    }

    @Test
    public void testFindMembership() {


        String membershipId = "1";
        MembershipJpaEntity membershipJpaEntity = new MembershipJpaEntity(1L,"test","test@g.com","seoul",true,true,"");

        Membership want = Membership.builder()
                .membershipId(membershipId)
                .name("test")
                .address("test@g.com")
                .email("seoul")
                .isValid(true)
                .isCorp(true)
                .refreshToken("")
                .build();

        FindMembershipCommand command = FindMembershipCommand
                .builder()
                .membershipId(membershipId)
                .build();

        when(findMembershipPort.findMembership(new Membership.MembershipId(command.getMembershipId())))
                .thenReturn(membershipJpaEntity);

        when(membershipMapper.mapToDomainEntity(membershipJpaEntity))
                .thenReturn(want);

        Membership got = findMembershipService.findMembership(command);

        assertEquals(want, got);
    }

    @Test
    public void testFindMembershipListByAddress() {

        String membershipId = "1";
        List<MembershipJpaEntity> membershipJpaEntityList = List.of(new MembershipJpaEntity(1L,"test","test@g.com","seoul",true,true,""));

        List<Membership> wantList = List.of(Membership.builder()
                .membershipId(membershipId)
                .name("test")
                .address("test@g.com")
                .email("seoul")
                .isValid(true)
                .isCorp(true)
                .refreshToken("")
                .build());

        FindMembershipListByAddressCommand command = FindMembershipListByAddressCommand
                .builder()
                .addressName("seoul")
                .build();

        when(findMembershipPort.findMembershipListByAddress(new Membership.MembershipAddress("seoul")))
                .thenReturn(membershipJpaEntityList);

        when(membershipMapper.mapToDomainEntity(membershipJpaEntityList.get(0)))
                .thenReturn(wantList.get(0));

        List<Membership> got = findMembershipService.findMembershipListByAddress(command);

        assertEquals(wantList, got);
    }

}
