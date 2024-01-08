package com.mypay.money.adapter.axon.saga;

import com.mypay.common.command.CheckRegisteredBankAccountCommand;
import com.mypay.common.command.RollbackFirmbankingRequestCommand;
import com.mypay.common.event.CheckRegisteredBankAccountEvent;
import com.mypay.common.command.RequestFirmbankingCommand;
import com.mypay.common.event.RequestFirmbankingFinishedEvent;
import com.mypay.common.event.RollbackFirmbankingFinishedEvent;
import com.mypay.money.adapter.axon.event.RechargingCreatedEvent;
import com.mypay.money.adapter.out.persistence.MemberMoneyJpaEntity;
import com.mypay.money.application.port.out.IncreaseMoneyPort;
import com.mypay.money.domain.MemberMoney;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Saga
@NoArgsConstructor
public class MoneyRechargeSaga {

    @NonNull
    private transient CommandGateway commandGateway;

    @Autowired
    public void setCommandGateway(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }


    @StartSaga
    // 하나의 충전 요청 사가에 대한 기준 속성을 선정함
    @SagaEventHandler(associationProperty = "rechargingRequestId")
    public void handle(RechargingCreatedEvent event){
//    public CompletableFuture<Void> handle(RechargingCreatedEvent event){
        // association key 설정
        // 다음 이벤트 핸들링에서 키로 구분하겠다.
        String checkRegisteredBankAccountId = UUID.randomUUID().toString();
        SagaLifecycle.associateWith("checkRegisteredBankAccountId", checkRegisteredBankAccountId);

        // 충전 요청이 시작 되었다

        // CheckRegisteredBankAccountCommand 뱅킹의 계좌 등록 여부 확인하기. (RegisteredBankAccount)
        // axon server -> Banking service -> 요청과 응답 서비스에서 다 사용 common
        // axon framework 에서 모든 aggregate 변경은 aggregate 단위로 되어야 한다.
        commandGateway.send(new CheckRegisteredBankAccountCommand(
                event.getRegisteredBankAccountAggregateIdentifier(),
                event.getRechargingRequestId(),
                event.getMembershipId(),
                checkRegisteredBankAccountId,
                event.getBankName(),
                event.getBankAccountNumber(),
                event.getAmount()
        )).whenComplete(
                (result, throwable) -> {
                    if(throwable != null) {
                        throwable.printStackTrace();
                    }else{
                    }
                }
        );

    }

    // CheckRegisteredBankAccountEvent 받기
    @SagaEventHandler(associationProperty = "checkedRegisteredBankAccountId")
    public void handle(CheckRegisteredBankAccountEvent event){

        boolean status = event.isChecked();
        if(status) {

        }else{

        }

        String requestFirmbankingId = UUID.randomUUID().toString();
        SagaLifecycle.associateWith("requestFirmbankingId", requestFirmbankingId);

        // 송금 요청
        // 고객 계좌 -> 법인 계좌
        commandGateway.send(new RequestFirmbankingCommand(
                requestFirmbankingId,
                event.getFirmBankingRequestAggregateIdentifier(),
                event.getRechargingRequestId(),
                event.getMembershipId(),
                event.getFromBankName(),
                event.getFromBankAccountNumber(),
                "rhwbank", // fromBank
                "123456789", //
                event.getAmount()
        )).whenComplete(
                (result, throwable) -> {
                    if(throwable != null) {
                        throwable.printStackTrace();
                    }else{
                    }
                }
        );
    }

    // RequestFirmbankingFinishedEvent 받기
    @SagaEventHandler(associationProperty = "requestFirmbankingId")
    public void handle(RequestFirmbankingFinishedEvent event, IncreaseMoneyPort increaseMoneyPort){

        boolean status = event.getStatus() == 0;
        if(status) {
        }else{
        }

        // 만약 마지막 트랜잭션이 제대로 수행 안될경우 보상 트랜잭션 rollback 수행
        MemberMoneyJpaEntity resultEntity = null;
                increaseMoneyPort.increaseMoney(
                        new MemberMoney.MembershipId(event.getMembershipId()),
                        event.getMoneyAmount()
                );

        if(resultEntity == null) {
            // 실패시 롤백 이벤트
            String rollbackFirmbankingId = UUID.randomUUID().toString();
            SagaLifecycle.associateWith("rollbackFirmbankingId",rollbackFirmbankingId);
            commandGateway.send(new RollbackFirmbankingRequestCommand(
                    rollbackFirmbankingId,
                    event.getRequestFirmbankingAggregateIdentifier(),
                    event.getRechargingRequestId(),
                    event.getMembershipId(),
                    event.getFromBankName(),
                    event.getFromBankAccountNumber(),
                    event.getToBankName(),
                    event.getToBankAccountNumber(),
                    event.getMoneyAmount()
            )).whenComplete(
                    (result, throwable) -> {
                        if(throwable != null) {
                            throwable.printStackTrace();
                        }else{
                            SagaLifecycle.end();
                        }
                    }
            );
        }else{
            // 성공 시 saga 종료
            SagaLifecycle.end();
        }

    }

    @EndSaga
    @SagaEventHandler(associationProperty = "rollbackFirmbankingId")
    public void handle(RollbackFirmbankingFinishedEvent event){

    }


}
