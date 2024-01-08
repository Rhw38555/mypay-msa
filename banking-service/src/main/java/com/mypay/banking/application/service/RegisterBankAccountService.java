package com.mypay.banking.application.service;

import com.mypay.banking.adapter.axon.command.CreateRegisteredBankAccountCommand;
import com.mypay.banking.adapter.out.persistence.RegisteredBankAccountJpaEntity;
import com.mypay.banking.adapter.out.persistence.RegisteredBankAccountMapper;
import com.mypay.banking.adapter.out.persistence.external.firmbanking.GetExternalBankAccountInfoRequest;
import com.mypay.banking.adapter.out.persistence.external.firmbanking.GetExternalBankAccountInfoResponse;
import com.mypay.banking.adapter.out.service.MembershipStatus;
import com.mypay.banking.application.port.in.RegisterBankAccountCommand;
import com.mypay.banking.application.port.in.RegisterBankAccountUseCase;
import com.mypay.banking.application.port.out.GetExternalBankAccountInfoPort;
import com.mypay.banking.application.port.out.GetMembershipPort;
import com.mypay.banking.application.port.out.RegisterBankAccountPort;
import com.mypay.banking.domain.RegisteredBankAccount;
import com.mypay.common.*;
import com.mypay.common.subtask.SubTask;
import com.mypay.common.subtask.SubTaskStatus;
import com.mypay.common.subtask.SubTaskType;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@UseCase
@RequiredArgsConstructor
@Transactional
public class RegisterBankAccountService implements RegisterBankAccountUseCase {

    private final RegisterBankAccountPort registerBankAccountPort;
    private final RegisteredBankAccountMapper registerBankAccountMapper;

    private final GetExternalBankAccountInfoPort getExternalBankAccountInfoPort;
    private final GetMembershipPort getMembershipPort;

    private final TaskProducer taskProducer;

    private final CountDownLatchManager countDownLatchManager;
    private final CommandGateway commandGateway;

    @Override
    public RegisteredBankAccount registerBankAccount(RegisterBankAccountCommand command) {

        // 은행 계좌 등록해야 하는 서비스

        // 멤버(외부) 서비스 정상 여부 확인
        MembershipStatus membershipStatus = getMembershipPort.getMembership(command.getMembershipId());
        if(!membershipStatus.isValid()){
            return null;
        }

        // 1. 등록된 계좌인지 확인한다(외부 은행에 이 계좌가 정상인지 확인한다)
        GetExternalBankAccountInfoResponse accountInfo = getExternalBankAccountInfoPort.getExternalBankAccountInfo(
                new GetExternalBankAccountInfoRequest(command.getBankName(), command.getBankAccount())
        );

        // 유효한 계좌 확인
        if (!accountInfo.isValid()) {
            return null;
        }

        // 계좌 저장
        RegisteredBankAccountJpaEntity savedAccountInfo = registerBankAccountPort.createRegisteredBankAccount(
                new RegisteredBankAccount.MembershipId(command.getMembershipId()),
                new RegisteredBankAccount.BankName(command.getBankName()),
                new RegisteredBankAccount.BankAccountNumber(command.getBankAccount()),
                new RegisteredBankAccount.LinkedStatusIsValid(command.isValid()),
                new RegisteredBankAccount.AggregateIdentifier("")
        );
        return registerBankAccountMapper.mapToDomainEntity(savedAccountInfo);
    }

    @Override
    public void registerBankAccountByEvent(RegisterBankAccountCommand command) {

        // SubTask
        // 멤버십 상태 유효성 검사 Task
        SubTask validMembershipStatusTask = SubTask.builder()
                .subTaskName("validMembershipStatusTask")
                .membershipId(command.getMembershipId())
                .taskType(SubTaskType.MEMBERSHIP)
                .status(SubTaskStatus.READY)
                .build();


        // Banking Sub task
        SubTask getExternalBankAccountInfoResponseTask = SubTask.builder()
                .subTaskName("getExternalBankAccountInfoTask")
                .membershipId(command.getMembershipId())
                .taskType(SubTaskType.BANKING)
                .status(SubTaskStatus.READY)
                .build();

        // Banking Account Validation
        // Amount Money Firmbanking -> ok 가정

        List<SubTask> subTaskList = new ArrayList<>();
        subTaskList.add(validMembershipStatusTask);
        subTaskList.add(getExternalBankAccountInfoResponseTask);

        RegisterBankAccountTask task = RegisterBankAccountTask.builder()
                .taskId(UUID.randomUUID().toString())
                .taskName("RegisterBankAccountTask")
                .subTaskList(subTaskList)
                .membershipId(command.getMembershipId())
                .toBankAccountNumber(command.getBankAccount())
                .toBankName(command.getBankName())
                .build();

        // task producer는 taskId로 getCountDownLatch 생성
        // Kakfa Cluster Produce
        taskProducer.sendTask(task);
        countDownLatchManager.addCountDownLatch(task.getTaskId());
        // Wait
        try {
            countDownLatchManager.getCountDownLatch(task.getTaskId()).await();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 4. Task Result Consume
        String countDownResult = countDownLatchManager.getDataForKey(task.getTaskId());
        if(countDownResult.equals("success")){
            // insert event sourcing
            commandGateway.send(new CreateRegisteredBankAccountCommand(command.getMembershipId(), command.getBankName(), command.getBankAccount()))
                    .whenComplete(
                            (result, throwable) -> {
                                if(throwable != null) {
                                    throwable.printStackTrace();
                                }else{
                                    // 정상 이벤트 소싱, 계좌 insert
                                    registerBankAccountPort.createRegisteredBankAccount(
                                            new RegisteredBankAccount.MembershipId(command.getMembershipId()),
                                            new RegisteredBankAccount.BankName(command.getBankName()),
                                            new RegisteredBankAccount.BankAccountNumber(command.getBankAccount()),
                                            new RegisteredBankAccount.LinkedStatusIsValid(command.isValid()),
                                            new RegisteredBankAccount.AggregateIdentifier(result.toString())
                                    );
                                }
                            }
                    );
        }
    }
}
