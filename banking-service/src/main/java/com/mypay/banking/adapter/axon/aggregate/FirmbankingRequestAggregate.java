package com.mypay.banking.adapter.axon.aggregate;

import com.mypay.banking.FirmBankingStatus;
import com.mypay.banking.adapter.axon.command.CreateFirmbankingRequestCommand;
import com.mypay.banking.adapter.axon.command.UpdateFirmbankingRequestCommand;
import com.mypay.banking.adapter.axon.event.FirmbankingRequestCreatedEvent;
import com.mypay.banking.adapter.axon.event.FirmbankingRequestUpdatedEvent;
import com.mypay.banking.adapter.out.persistence.external.firmbanking.ExternalFirmbankingReqeust;
import com.mypay.banking.adapter.out.persistence.external.firmbanking.FirmbankingResult;
import com.mypay.banking.application.port.out.RequestExternalFirmbankingPort;
import com.mypay.banking.application.port.out.RequestFirmbankingPort;
import com.mypay.banking.domain.FirmbankingRequest;
import com.mypay.common.command.RequestFirmbankingCommand;
import com.mypay.common.command.RollbackFirmbankingRequestCommand;
import com.mypay.common.event.RequestFirmbankingFinishedEvent;
import com.mypay.common.event.RollbackFirmbankingFinishedEvent;
import lombok.Data;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.UUID;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate()
@Data
public class FirmbankingRequestAggregate {

    @AggregateIdentifier
    private String id;

    private String fromBankName;

    private String fromBankAccountNumber;

    private String toBankName;
    private String toBankAccountNumber;
    private int moneyAmount;
    private FirmBankingStatus firmbankingStatus;

    @CommandHandler
    public FirmbankingRequestAggregate (CreateFirmbankingRequestCommand command){
        apply(new FirmbankingRequestCreatedEvent(command.getFromBankName(), command.getFromBankAccountNumber(), command.getToBankName(), command.getToBankAccountNumber(), command.getMoneyAmount()));
    }

    @EventSourcingHandler
    public void on (FirmbankingRequestCreatedEvent event) {

        id = UUID.randomUUID().toString();
        fromBankName = event.getFromBankName();
        fromBankAccountNumber = event.getFromBankAccountNumber();
        toBankName = event.getToBankName();
        toBankAccountNumber = event.getToBankAccountNumber();
    }

    @CommandHandler
    public String handle(UpdateFirmbankingRequestCommand command) {

        id = command.getAggregateIdentifier();

        apply(new FirmbankingRequestUpdatedEvent(command.getStatus()));
        return id;
    }

    @EventSourcingHandler
    public void on (FirmbankingRequestUpdatedEvent event) {
        firmbankingStatus = event.getStatus();
    }

    // 송금 요청 확인 커맨드
    @CommandHandler
    public String FirmbankingRequestAggregate(RequestFirmbankingCommand command, RequestFirmbankingPort requestFirmbankingPort, RequestExternalFirmbankingPort externalFirmbankingPort) {

        id = command.getAggregateIdentifier();

        // from -> to  펌뱅킹 수행
        requestFirmbankingPort.createFirmbankingRequest(
                new FirmbankingRequest.FromBankName(command.getFromBankName()),
                new FirmbankingRequest.FromBankAccountNumber(command.getFromBankAccountNumber()),
                new FirmbankingRequest.ToBankName(command.getToBankName()),
                new FirmbankingRequest.ToBankAccountNumber(command.getToBankAccountNumber()),
                new FirmbankingRequest.MoneyAmount(command.getMoneyAmount()),
                new FirmbankingRequest.FirmStatus(FirmBankingStatus.REQUEST),
                new FirmbankingRequest.AggregateIdentifier(id)
        );

        FirmbankingResult firmbankingResult = externalFirmbankingPort.requestExternalFirmbanking(new ExternalFirmbankingReqeust(
                command.getFromBankName(),
                command.getFromBankAccountNumber(),
                command.getToBankName(),
                command.getToBankAccountNumber(),
                command.getMoneyAmount()
        ));

        int resultCode = firmbankingResult.getResultCode().ordinal();


        // 성공, 실패
        apply(new RequestFirmbankingFinishedEvent(
                command.getRequestFirmbankingId(),
                command.getRechargeRequestId(),
                command.getMembershipId(),
                command.getFromBankName(),
                command.getFromBankAccountNumber(),
                command.getToBankName(),
                command.getToBankAccountNumber(),
                command.getMoneyAmount(),
                resultCode,
                id
        ));

        return id;
    }

    // RollbackFirmbankingRequestCommand 핸들러
    @CommandHandler
    public String FirmbankingRequestAggregate(RollbackFirmbankingRequestCommand command, RequestFirmbankingPort requestFirmbankingPort, RequestExternalFirmbankingPort externalFirmbankingPort) {

        id = command.getAggregateIdentifier();

        // 롤백 수행 -> 법인 계좌 -> 고객 계좌
        requestFirmbankingPort.createFirmbankingRequest(
                new FirmbankingRequest.FromBankName(command.getFromBankName()),
                new FirmbankingRequest.FromBankAccountNumber(command.getFromBankAccountNumber()),
                new FirmbankingRequest.ToBankName(command.getToBankName()),
                new FirmbankingRequest.ToBankAccountNumber(command.getToBankAccountNumber()),
                new FirmbankingRequest.MoneyAmount(command.getMoneyAmount()),
                new FirmbankingRequest.FirmStatus(FirmBankingStatus.REQUEST),
                new FirmbankingRequest.AggregateIdentifier(id)
        );

        FirmbankingResult firmbankingResult = externalFirmbankingPort.requestExternalFirmbanking(new ExternalFirmbankingReqeust(
                command.getFromBankName(),
                command.getFromBankAccountNumber(),
                command.getToBankName(),
                command.getToBankAccountNumber(),
                command.getMoneyAmount()
        ));

        int resultCode = firmbankingResult.getResultCode().ordinal();

        // 성공, 실패
        apply(new RollbackFirmbankingFinishedEvent(
                command.getRollbackFirmbankingId(),
                command.getMembershipId(),
                id
        ));

        return id;
    }

    public FirmbankingRequestAggregate() {
    }
}
