package com.mypay.money.application.service;

import com.mypay.common.*;
import com.mypay.money.ChangingMoneyStatus;
import com.mypay.money.ChangingType;
import com.mypay.money.adapter.axon.command.MemberMoneyCreatedCommand;
import com.mypay.money.adapter.out.persistence.MemberMoneyJpaEntity;
import com.mypay.money.adapter.out.persistence.MoneyChangingRequestMapper;
import com.mypay.money.application.port.in.CreateMemberMoneyCommand;
import com.mypay.money.application.port.in.CreateMemberMoneyUseCase;
import com.mypay.money.application.port.in.IncreaseMoneyChangingRequestCommand;
import com.mypay.money.application.port.in.IncreaseMoneyChangingRequestUseCase;
import com.mypay.money.application.port.out.CreateMemberMoneyPort;
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

@Slf4j
@UseCase
@RequiredArgsConstructor
@Transactional
public class CreateMemberMoneyService implements CreateMemberMoneyUseCase {

    private final CommandGateway commandGateway;
    private final CreateMemberMoneyPort createMemberMoneyPort;

    @Override
    public void createMemberMoney(CreateMemberMoneyCommand command) {

        // axon 전용 command 생성, axon 동작 1
        MemberMoneyCreatedCommand axonCommand = new MemberMoneyCreatedCommand(command.getMembershipId());
        // command 만들어 send하면 command 이벤트가 전송된다. , axon 동작 2
        // whenComplete eventSourcing을 기다리고 그 이후 작업을 처리할 수 있다. , axon 동작 5
        commandGateway.send(axonCommand).whenComplete((result, throwable) ->{
           if(throwable != null) {
               throw new RuntimeException(throwable);
           }else{
               // DB 저장
               createMemberMoneyPort.createMemberMoney(
                       new MemberMoney.MembershipId(command.getMembershipId()),
                       new MemberMoney.MoneyAggregateIdentifier(result.toString())
               );
           }
        });
    }
}
