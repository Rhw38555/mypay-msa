package com.mypay.money.application.service;

import com.mypay.common.*;
import com.mypay.money.ChangingMoneyStatus;
import com.mypay.money.ChangingType;
import com.mypay.money.adapter.axon.command.RechargingMoneyRequestCreateCommand;
import com.mypay.money.adapter.out.persistence.MemberMoneyJpaEntity;
import com.mypay.money.adapter.out.persistence.MemberMoneyMapper;
import com.mypay.money.adapter.out.persistence.MoneyChangingRequestMapper;
import com.mypay.money.application.port.in.FindMemberMoneyListByMembershipIdsCommand;
import com.mypay.money.application.port.in.FindMemberMoneyUseCase;
import com.mypay.money.application.port.in.IncreaseMoneyChangingRequestCommand;
import com.mypay.money.application.port.in.IncreaseMoneyChangingRequestUseCase;
import com.mypay.money.application.port.out.GetMemberMoneyPort;
import com.mypay.money.application.port.out.GetMembershipPort;
import com.mypay.money.application.port.out.IncreaseMoneyPort;
import com.mypay.money.application.port.out.SendRechargingMoneyTaskPort;
import com.mypay.money.domain.MemberMoney;
import com.mypay.money.domain.MoneyChangingRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@UseCase
@RequiredArgsConstructor
@Transactional
public class FindMemberMoneyService implements FindMemberMoneyUseCase {

    private final GetMemberMoneyPort getMemberMoneyPort;
    private final MemberMoneyMapper memberMoneyMapper;

    @Override
    public List<MemberMoney> findMemberMoneyListByMembershipIds(FindMemberMoneyListByMembershipIdsCommand command) {
        // 여러 membership Ids를 기준으로 memberMoney를 가져온다.
        return getMemberMoneyPort.getMemberMoneyListByMembershipIds(command.getTargetMembershipId())
                .stream()
                .map(memberMoneyMapper::mapToDomainEntity)
                .collect(Collectors.toList());
    }
}
