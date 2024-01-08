package com.mypay.banking.adapter.axon.aggregate;

import com.mypay.banking.adapter.axon.command.CreateRegisteredBankAccountCommand;
import com.mypay.banking.adapter.axon.event.CreateRegisteredBankAccountEvent;
import com.mypay.banking.adapter.out.persistence.external.firmbanking.GetExternalBankAccountInfoRequest;
import com.mypay.banking.adapter.out.persistence.external.firmbanking.GetExternalBankAccountInfoResponse;
import com.mypay.banking.application.port.out.GetExternalBankAccountInfoPort;
import com.mypay.common.command.CheckRegisteredBankAccountCommand;
import com.mypay.common.event.CheckRegisteredBankAccountEvent;
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
public class RegisteredBankAccountAggregate {

    @AggregateIdentifier
    private String id;

    private String membershipId;

    private String bankName;

    private String bankAccountNumber;

    @CommandHandler
    public RegisteredBankAccountAggregate(CreateRegisteredBankAccountCommand command){
        apply(new CreateRegisteredBankAccountEvent(command.getMembershipId(), command.getBankName(), command.getBankAccountNumber()));
    }

    @CommandHandler
    public void handle(@NotNull CheckRegisteredBankAccountCommand command, GetExternalBankAccountInfoPort getExternalBankAccountInfoPort) {
        // Saga의 시작으로부터 이벤트를 받음 (트랜잭션)
        // comamnd를 통해 이 어그리거트(RegisteredBankAccount)가 정상인지를 확인해야한다.

        // check Registered Bank Account
        GetExternalBankAccountInfoResponse getExternalBankAccountInfoResponse = getExternalBankAccountInfoPort.getExternalBankAccountInfo(new GetExternalBankAccountInfoRequest(command.getBankName(), command.getBankAccountNumber()));
        boolean isValidAccount = getExternalBankAccountInfoResponse.isValid();

        String firmbankingUUID = UUID.randomUUID().toString();

        // checkRegisteredBankAccountEvent
        apply(new CheckRegisteredBankAccountEvent(
                command.getRechargeRequestId(),
                command.getCheckRegisteredBankAccountId(),
                command.getMembershipId(),
                isValidAccount,
                command.getAmount(),
                firmbankingUUID,
                getExternalBankAccountInfoResponse.getBankName(),
                getExternalBankAccountInfoResponse.getBankAccountNumber()
        ));
    }

    @EventSourcingHandler
    public void on (CreateRegisteredBankAccountEvent event) {
        id = UUID.randomUUID().toString();
        membershipId = event.getMembershipId();
        bankName = event.getBankName();
        bankAccountNumber = event.getBankAccountNumber();
    }

    public RegisteredBankAccountAggregate() {
    }
}
