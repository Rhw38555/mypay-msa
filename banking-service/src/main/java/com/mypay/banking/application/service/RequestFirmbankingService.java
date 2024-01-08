package com.mypay.banking.application.service;

import com.mypay.banking.FirmBankingStatus;
import com.mypay.banking.adapter.axon.command.CreateFirmbankingRequestCommand;
import com.mypay.banking.adapter.axon.command.UpdateFirmbankingRequestCommand;
import com.mypay.banking.adapter.out.persistence.RegisteredBankAccountMapper;
import com.mypay.banking.adapter.out.persistence.RequestFirmbankingJpaEntity;
import com.mypay.banking.adapter.out.persistence.RequestFirmbankingMapper;
import com.mypay.banking.adapter.out.persistence.SpringDataFirmbankingRequestRepository;
import com.mypay.banking.adapter.out.persistence.external.firmbanking.ExternalFirmbankingReqeust;
import com.mypay.banking.adapter.out.persistence.external.firmbanking.FirmbankingResult;
import com.mypay.banking.application.port.in.RequestFirmbankingCommand;
import com.mypay.banking.application.port.in.RequestFirmbankingUseCase;
import com.mypay.banking.application.port.in.UpdateFirmbankingCommand;
import com.mypay.banking.application.port.in.UpdateFirmbankingUseCase;
import com.mypay.banking.application.port.out.RequestExternalFirmbankingPort;
import com.mypay.banking.application.port.out.RequestFirmbankingPort;
import com.mypay.banking.domain.FirmbankingRequest;
import com.mypay.common.UseCase;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@UseCase
@RequiredArgsConstructor
@Transactional
public class RequestFirmbankingService implements RequestFirmbankingUseCase, UpdateFirmbankingUseCase {

    private final RequestFirmbankingPort requestFirmbankingPort;

    private final RequestFirmbankingMapper requestFirmbankingMapper;

    private final RequestExternalFirmbankingPort requestExternalFirmbankingPort;

    private final CommandGateway commandGateway;

    @Override
    public FirmbankingRequest requestFirmbanking(RequestFirmbankingCommand command) {

        // a->b 계좌
        // 1. 요청에 대해 정보를 요청 상태로 write
        RequestFirmbankingJpaEntity requestedEntity = requestFirmbankingPort.createFirmbankingRequest(
                new FirmbankingRequest.FromBankName(command.getFromBankName()),
                new FirmbankingRequest.FromBankAccountNumber(command.getFromBankAccountNumber()),
                new FirmbankingRequest.ToBankName(command.getToBankName()),
                new FirmbankingRequest.ToBankAccountNumber(command.getToBankAccountNumber()),
                new FirmbankingRequest.MoneyAmount(command.getMonoeyAmount()),
                new FirmbankingRequest.FirmStatus(FirmBankingStatus.REQUEST),
                new FirmbankingRequest.AggregateIdentifier("")
        );

        // 2. 외부 은행에 펌뱅킹 요청
        FirmbankingResult result = requestExternalFirmbankingPort.requestExternalFirmbanking(new ExternalFirmbankingReqeust(
                command.getFromBankName(),
                command.getFromBankAccountNumber(),
                command.getToBankName(),
                command.getToBankAccountNumber(),
                command.getMonoeyAmount()
        ));

        UUID randomUUID = UUID.randomUUID();
        requestedEntity.setUuid(randomUUID);
        // 3. 결과에 따라 1번에서 작성했던 FirmbankingRequest 정보를 update
        if(result.getResultCode() == FirmBankingStatus.REQUEST){
            // 성공
            requestedEntity.setFirmbankingStatus(FirmBankingStatus.COMPLETED);
        }else{
            // 실패
            requestedEntity.setFirmbankingStatus(FirmBankingStatus.FAILED);
        }

        // 4. 결과를 리턴하기 전에 바뀐 상태 값을 기준으로 save
        return requestFirmbankingMapper.mapToDomainEntity(requestFirmbankingPort.modifyFirmbankingRequest(requestedEntity), randomUUID);
    }

    @Override
    public void requestFirmbankingByEvent(RequestFirmbankingCommand command) {
        // axon command -> event sourcing
        CreateFirmbankingRequestCommand createFirmbankingRequestCommand = CreateFirmbankingRequestCommand.builder()
                .toBankName(command.getToBankName())
                .toBankAccountNumber(command.getToBankAccountNumber())
                .fromBankName(command.getFromBankName())
                .fromBankAccountNumber(command.getFromBankAccountNumber())
                .moneyAmount(command.getMonoeyAmount())
                .build();

        commandGateway.send(createFirmbankingRequestCommand).whenComplete((result, throwable) -> {
           if(throwable != null){
               // 실패
               throwable.printStackTrace();
           }else{
               // 성공
               // Request Firmbanking DB save
               RequestFirmbankingJpaEntity requestedEntity = requestFirmbankingPort.createFirmbankingRequest(
                       new FirmbankingRequest.FromBankName(command.getFromBankName()),
                       new FirmbankingRequest.FromBankAccountNumber(command.getFromBankAccountNumber()),
                       new FirmbankingRequest.ToBankName(command.getToBankName()),
                       new FirmbankingRequest.ToBankAccountNumber(command.getToBankAccountNumber()),
                       new FirmbankingRequest.MoneyAmount(command.getMonoeyAmount()),
                       new FirmbankingRequest.FirmStatus(FirmBankingStatus.REQUEST),
                       new FirmbankingRequest.AggregateIdentifier(result.toString())
               );

               // 2. 외부 은행에 펌뱅킹 요청
               FirmbankingResult firmbankingResult = requestExternalFirmbankingPort.requestExternalFirmbanking(new ExternalFirmbankingReqeust(
                       command.getFromBankName(),
                       command.getFromBankAccountNumber(),
                       command.getToBankName(),
                       command.getToBankAccountNumber(),
                       command.getMonoeyAmount()
               ));

               UUID randomUUID = UUID.randomUUID();
               requestedEntity.setUuid(randomUUID);
               // 3. 결과에 따라 1번에서 작성했던 FirmbankingRequest 정보를 update
               if(firmbankingResult.getResultCode() == FirmBankingStatus.REQUEST){
                   // 성공
                   requestedEntity.setFirmbankingStatus(FirmBankingStatus.COMPLETED);
               }else{
                   // 실패
                   requestedEntity.setFirmbankingStatus(FirmBankingStatus.FAILED);
               }

               requestFirmbankingPort.modifyFirmbankingRequest(requestedEntity);

           }
        });
    }

    @Override
    public void updateFirmbankingByEvent(UpdateFirmbankingCommand command) {
        UpdateFirmbankingRequestCommand updateFirmbankingRequestCommand = new UpdateFirmbankingRequestCommand(command.getFirmbankingRequestAggregateIdentifier(), command.getStatus());
        // axon command
        commandGateway.send(updateFirmbankingRequestCommand)
                .whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        // 실패
                        throwable.printStackTrace();
                    } else {
                        // Request Firmbanking DB save
                        RequestFirmbankingJpaEntity entity = requestFirmbankingPort.getFirmbankingRequest(
                          new FirmbankingRequest.AggregateIdentifier(updateFirmbankingRequestCommand.getAggregateIdentifier())
                        ).get(0);
                        // status의 변경으로 인한 외부 은행과의 커뮤니케이션 if rollback -> 0, status 변경
                        entity.setFirmbankingStatus(command.getStatus());
                        requestFirmbankingPort.modifyFirmbankingRequest(entity);
                    }
                });
    }
}
