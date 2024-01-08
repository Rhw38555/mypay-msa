package com.mypay.money.adapter.axon.aggregate;


import com.mypay.money.adapter.axon.command.IncreaseMemberMoneyCommand;
import com.mypay.money.adapter.axon.command.MemberMoneyCreatedCommand;
import com.mypay.money.adapter.axon.command.RechargingMoneyRequestCreateCommand;
import com.mypay.money.adapter.axon.event.IncreaseMemberMoneyEvent;
import com.mypay.money.adapter.axon.event.MemberMoneyCreatedEvent;
import com.mypay.money.adapter.axon.event.RechargingCreatedEvent;
import com.mypay.money.adapter.out.service.RegisteredBankAccount;
import com.mypay.money.application.port.out.GetRegisteredBankAccountPort;
import com.mypay.money.application.port.out.RegisteredBankAccountAggreateIdentifier;
import lombok.Data;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import javax.validation.constraints.NotNull;
import java.util.UUID;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate()
@Data
// 모든 aggregate 변경은 aggregate 단위로만 변경 가능(DDD)
public class MemberMoneyAggregate {
    @AggregateIdentifier
    private String id;

    private Long membershipId;

    private int balance;

    // command 핸들러를 통해 event 생성, axon 동작 3
    @CommandHandler
    public MemberMoneyAggregate(MemberMoneyCreatedCommand command) {


        // Event sourcing을 위해 커맨드를 apply해서 event 생성
        apply(new MemberMoneyCreatedEvent(command.getMembershipId()));
    }

    // EventsourcingHandler를 통해 event를 받음, event handler에서 aggregate 생성, axon 동작 4
    @EventSourcingHandler
    public void on(MemberMoneyCreatedEvent event){
        id = UUID.randomUUID().toString();
        membershipId = Long.parseLong(event.getMembershipId());
        balance = 0;
    }

    // get command hander
    @CommandHandler
    public String handle(@NotNull IncreaseMemberMoneyCommand command){
        id = command.getAggregateIdentifier();

        apply(new IncreaseMemberMoneyEvent(id,command.getMembershipId(), command.getAmount()));
        return id;
    }

    @EventSourcingHandler
    public void on(IncreaseMemberMoneyEvent event){
        id = event.getAggregateIdentifier();
        membershipId = Long.parseLong(event.getMembershipId());
        balance = event.getAmount();
    }

    // 머니 충전에 대한 요청 커맨드
    @CommandHandler
    public void handler(RechargingMoneyRequestCreateCommand command, GetRegisteredBankAccountPort getRegisteredBankAccountPort){
//    public void handler(RechargingMoneyRequestCreateCommand command){
        id = command.getAggregateIdentifier();
        // banking 정보 필요
        // Saga Start 시 registeredBankAccount에 대한 aggregateId를 알아야되서 GetRegisteredBankAccountPort가 필요
        RegisteredBankAccountAggreateIdentifier registeredBankAccountAggreateIdentifier
                = getRegisteredBankAccountPort.getRegisterBankAccount(command.getMembershipId());

        apply(new RechargingCreatedEvent(
                command.getRechargingRequestId(),
                command.getMembershipId(),
                command.getAmount(),
                registeredBankAccountAggreateIdentifier.getAggreateIdentifier(),
                registeredBankAccountAggreateIdentifier.getBankName(),
                registeredBankAccountAggreateIdentifier.getBankAccountNumber()
        ));
    }


    public MemberMoneyAggregate() {
    }
}
