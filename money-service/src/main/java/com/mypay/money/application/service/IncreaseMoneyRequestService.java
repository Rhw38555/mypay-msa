package com.mypay.money.application.service;

import com.mypay.common.*;
import com.mypay.common.subtask.SubTask;
import com.mypay.common.subtask.SubTaskStatus;
import com.mypay.common.subtask.SubTaskType;
import com.mypay.money.adapter.axon.command.RechargingMoneyRequestCreateCommand;
import com.mypay.money.application.port.in.IncreaseMoneyChangingRequestCommand;
import com.mypay.money.application.port.out.GetMemberMoneyPort;
import com.mypay.money.application.port.out.GetMembershipPort;
import com.mypay.money.application.port.out.SendRechargingMoneyTaskPort;
import com.mypay.money.domain.MoneyChangingRequest;
import lombok.RequiredArgsConstructor;
import com.mypay.money.ChangingMoneyStatus;
import com.mypay.money.ChangingType;
import com.mypay.money.adapter.out.persistence.MemberMoneyJpaEntity;
import com.mypay.money.adapter.out.persistence.MoneyChangingRequestMapper;
import com.mypay.money.application.port.in.IncreaseMoneyChangingRequestUseCase;
import com.mypay.money.application.port.out.IncreaseMoneyPort;
import com.mypay.money.domain.MemberMoney;
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
public class IncreaseMoneyRequestService implements IncreaseMoneyChangingRequestUseCase {

    private final IncreaseMoneyPort increaseMoneyPort;
    private final GetMemberMoneyPort getMemberMoneyPort;
    private final MoneyChangingRequestMapper moneyChangingRequestMapper;
    private final GetMembershipPort membershipPort;
//    private final SendRechargingMoneyTaskPort sendRechargingMoneyTaskPort;
    private final TaskProducer taskProducer;
    private final CountDownLatchManager countDownLatchManager;
    private final CommandGateway commandGateway;

    @Override
    public MoneyChangingRequest increaseMoneyRequest(IncreaseMoneyChangingRequestCommand command) {

        // 머니 충전
        // 1. 고객 정보가 정상인지 확인
        membershipPort.getMembership(command.getTargetMembershipId());

        // 2. 고객의 연동된 계좌가 있는지, 고객의 연동된 계좌의 잔액이 충분한지도 확인

        // 3. 법인 계좌 상태도 정상인지 확인

        // 4. 증액을 위한 "기록". 요청 상태로 MoneyChangingRequest를 생성한다.

        // 5. 펌뱅킹을 수행하고 (고객의 연동된 계좌 -> 마이페이 법인 계좌, 뱅킹 서비스)


        // 6-1. 결과가 정상적이라면 성공으로 MoneyChangingRequest 상태값을 변동후 리턴.
        // 성공 시 멤버의 MemberMoney 값 증액이 필요하다.
        MemberMoneyJpaEntity memberMoneyJpaEntity = increaseMoneyPort.increaseMoney(
                new MemberMoney.MembershipId(command.getTargetMembershipId())
                , command.getAmount());

        if (memberMoneyJpaEntity != null) {
            return moneyChangingRequestMapper.mapToDomainEntity(increaseMoneyPort.createMoneyChangingRequest(
                            new MoneyChangingRequest.TargetMembershipId(command.getTargetMembershipId()),
                            new MoneyChangingRequest.MoneyChangingType(ChangingType.INCREASING),
                            new MoneyChangingRequest.ChangingMoneyAmount(command.getAmount()),
                            new MoneyChangingRequest.MoneyChangingStatus(ChangingMoneyStatus.REQUESTED),
                            new MoneyChangingRequest.Uuid(UUID.randomUUID().toString())
                    )
            );
        }
        // 6-2. 결과가 실패라면 실패라고 MoneyChangingRequest 상태값을 변동후 리턴.

        return null;
    }

    @Override
    public MoneyChangingRequest increaseMoneyRequestAsync(IncreaseMoneyChangingRequestCommand command) {

        // SubTask
        // 각 서비스에 특정 membershipId로 Validation을 하기위한 Task.

        // 1. Subtask, Task
        SubTask validMembershipTask = SubTask.builder()
                .subTaskName("validMembershipTask")
                .membershipId(command.getTargetMembershipId())
                .taskType(SubTaskType.MEMBERSHIP)
                .status(SubTaskStatus.READY)
                .build();

        // Banking Sub task
        SubTask validBankingAccountTask = SubTask.builder()
                .subTaskName("validBankingAccountTask")
                .membershipId(command.getTargetMembershipId())
                .taskType(SubTaskType.BANKING)
                .status(SubTaskStatus.READY)
                .build();

        // Banking Account Validation
        // Amount Money Firmbanking -> ok 가정

        List<SubTask> subTaskList = new ArrayList<>();
        subTaskList.add(validMembershipTask);
        subTaskList.add(validBankingAccountTask);

        RechargingMoneyTask task = RechargingMoneyTask.builder()
                .taskId(UUID.randomUUID().toString())
                .taskName("RechargingMoneyTask")
                .subTaskList(subTaskList)
                .moneyAmount(command.getAmount())
                .membershipId(command.getTargetMembershipId())
                .toBankName("mybank")
                .build();

        // 2. Kakfa Cluster Produce
//        sendRechargingMoneyTaskPort.sendRechargingMoneyTaskPort(task);
        taskProducer.sendTask(task);
        // task producer는 taskId로 getCountDownLatch 생성
        countDownLatchManager.addCountDownLatch(task.getTaskId());

        // 3. Wait
        try {
            countDownLatchManager.getCountDownLatch(task.getTaskId()).await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 3-1. task-consumer
        // 등록된 subtask, status 모두 ok -> task 결과를 produce

        // 4. Task Result Consume
        String result = countDownLatchManager.getDataForKey(task.getTaskId());
        if(result.equals("success")){
            // consume ok
            MemberMoneyJpaEntity memberMoneyJpaEntity = increaseMoneyPort.increaseMoney(
                    new MemberMoney.MembershipId(command.getTargetMembershipId())
                    , command.getAmount());

            if (memberMoneyJpaEntity != null) {
                return moneyChangingRequestMapper.mapToDomainEntity(increaseMoneyPort.createMoneyChangingRequest(
                                new MoneyChangingRequest.TargetMembershipId(command.getTargetMembershipId()),
                                new MoneyChangingRequest.MoneyChangingType(ChangingType.INCREASING),
                                new MoneyChangingRequest.ChangingMoneyAmount(command.getAmount()),
                                new MoneyChangingRequest.MoneyChangingStatus(ChangingMoneyStatus.REQUESTED),
                                new MoneyChangingRequest.Uuid(UUID.randomUUID().toString())
                        )
                );
            }
        }else{
            // consume fail
            return null;
        }

        // 5. Consume ok, Logic

        return null;
    }

    @Override
    public void increaseMoneyRequestByEvent(IncreaseMoneyChangingRequestCommand command) {

        // SubTask
        // 각 서비스에 특정 membershipId로 Validation을 하기위한 Task.

        // 1. Subtask, Task
        SubTask validMembershipTask = SubTask.builder()
                .subTaskName("validMembershipTask")
                .membershipId(command.getTargetMembershipId())
                .taskType(SubTaskType.MEMBERSHIP)
                .status(SubTaskStatus.READY)
                .build();

        // Banking Sub task
        SubTask validBankingAccountTask = SubTask.builder()
                .subTaskName("validBankingAccountTask")
                .membershipId(command.getTargetMembershipId())
                .taskType(SubTaskType.BANKING)
                .status(SubTaskStatus.READY)
                .build();

        // Banking Account Validation
        // Amount Money Firmbanking -> ok 가정

        List<SubTask> subTaskList = new ArrayList<>();
        subTaskList.add(validMembershipTask);
        subTaskList.add(validBankingAccountTask);

        RechargingMoneyTask task = RechargingMoneyTask.builder()
                .taskId(UUID.randomUUID().toString())
                .taskName("RechargingMoneyTask")
                .subTaskList(subTaskList)
                .moneyAmount(command.getAmount())
                .membershipId(command.getTargetMembershipId())
                .toBankName("mybank")
                .build();

        // 2. Kakfa Cluster Produce
//        sendRechargingMoneyTaskPort.sendRechargingMoneyTaskPort(task);
        taskProducer.sendTask(task);
        // task producer는 taskId로 getCountDownLatch 생성
        countDownLatchManager.addCountDownLatch(task.getTaskId());

        // 3. Wait
        try {
            countDownLatchManager.getCountDownLatch(task.getTaskId()).await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }



        // 3-1. task-consumer
        // 등록된 subtask, status 모두 ok -> task 결과를 produce

        // 4. Task Result Consume
        String taskResult = countDownLatchManager.getDataForKey(task.getTaskId());
        if(taskResult.equals("success")){
            // consume ok
            MemberMoneyJpaEntity memberMoneyJpaEntity = getMemberMoneyPort.getMemberMoney(new MemberMoney.MembershipId(command.getTargetMembershipId()));
            String memberMoneyAggregateIdentifier = memberMoneyJpaEntity.getAggregateIdentifier();

            // saga의 시작을 나타내는 커맨드
            commandGateway.send(new RechargingMoneyRequestCreateCommand(memberMoneyAggregateIdentifier,
                    UUID.randomUUID().toString(),
                    command.getTargetMembershipId(),
                    command.getAmount()
            )).whenComplete(
                    (result, throwable) -> {
                        if (throwable != null) {
                            throwable.printStackTrace();
                            throw new RuntimeException(throwable);
                        } else {

                        }
                    }
            );
        }else{
            // consume fail
        }

        MemberMoneyJpaEntity memberMoneyJpaEntity = getMemberMoneyPort.getMemberMoney(new MemberMoney.MembershipId(command.getTargetMembershipId()));
        String memberMoneyAggregateIdentifier = memberMoneyJpaEntity.getAggregateIdentifier();

        // saga의 시작을 나타내는 커맨드
        commandGateway.send(new RechargingMoneyRequestCreateCommand(memberMoneyAggregateIdentifier,
                UUID.randomUUID().toString(),
                command.getTargetMembershipId(),
                command.getAmount()
        )).whenComplete(
                (result, throwable) -> {
                    if (throwable != null) {
                        throwable.printStackTrace();
                        throw new RuntimeException(throwable);
                    } else {

                    }
                }
        );

    }
//        MemberMoneyJpaEntity memberMoneyJpaEntity = getMemberMoneyPort.getMemberMoney(
//                new MemberMoney.MembershipId(command.getTargetMembershipId())
//        );
//
//        String aggregateIdentifier = memberMoneyJpaEntity.getAggregateIdentifier();
//
//        // axon command 생성
//        // 1. 고객 정보가 정상인지 확인(멤버)
//        commandGateway.send(IncreaseMemberMoneyCommand.builder()
//                        .aggregateIdentifier(aggregateIdentifier)
//                        .membershipId(command.getTargetMembershipId())
//                        .amount(command.getAmount()).build())
//                .whenComplete((result, throwable) ->{
//                    if(throwable != null) {
//                        throw new RuntimeException(throwable);
//                    }else{
//                        // Increase money
//                        increaseMoneyPort.increaseMoney(
//                                new MemberMoney.MembershipId(command.getTargetMembershipId())
//                                , command.getAmount());
//                    }
//                });
//
//    }
}
