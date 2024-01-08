package com.mypay.remittance.application.service;

import com.mypay.common.*;
import com.mypay.remittance.RemittanceStatus;
import com.mypay.remittance.RemittanceType;
import com.mypay.remittance.adapter.out.persistence.RemittanceMapper;
import com.mypay.remittance.adapter.out.persistence.RemittanceRequestJpaEntity;
import com.mypay.remittance.adapter.out.service.membership.MembershipStatus;
import com.mypay.remittance.application.port.in.RequestRemittanceCommand;
import com.mypay.remittance.application.port.in.RequestRemittanceUseCase;
import com.mypay.remittance.application.port.out.banking.BankingPort;
import com.mypay.remittance.application.port.out.membership.GetMembershipPort;
import com.mypay.remittance.application.port.out.RequestRemittancePort;
import com.mypay.remittance.application.port.out.money.MoneyInfo;
import com.mypay.remittance.application.port.out.money.MoneyPort;
import com.mypay.remittance.domain.RemittanceRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;

@Slf4j
@UseCase
@RequiredArgsConstructor
@Transactional
public class RequestRemittanceService implements RequestRemittanceUseCase {

    private final RequestRemittancePort requestRemittancePort;
    private final RemittanceMapper remittanceMapper;
    private final GetMembershipPort membershipPort;
    private final MoneyPort moneyPort;
    private final BankingPort bankPort;

    @Override
    public RemittanceRequest requestRemittance(RequestRemittanceCommand command) {
        // 0. 송금 요청 시작 상태로 기록(persistance)
        RemittanceRequestJpaEntity entity = requestRemittancePort.createRemittanceRequestHistory(command);

        // 1. from 멤버십 상태 확인(member svc)
        MembershipStatus membershipStatus = membershipPort.getMembership(command.getFromMembershipId());
        if(!membershipStatus.isValid()) {
            return null;
        }

        // 2. 잔액 존재하는지 확인(money svc)
        MoneyInfo moneyInfo = moneyPort.getMoneyInfo(command.getFromMembershipId());
        if(moneyInfo.getBalance() < command.getAmount()){
            // 2-1. 잔액이 충분하지 않다면, 충전 요청(money svc)
            // 잔액 부족(충전 필요), 만원 단위로 올림하는 Math 함수
            int rechargeAmount = (int) Math.ceil((command.getAmount() - moneyInfo.getBalance()) / 10000.0) * 10000;
            moneyPort.requestMoneyRecharging(command.getFromMembershipId(), rechargeAmount);
        }

        // 3. 송금 타입(고객/은행)
        if(command.getRemittanceType()== RemittanceType.MEMBERSHIP){
            // 3-1. 내부 고객일 경우
            // from 고객 머니 감액, to 고객 머니  증액
            boolean remittanceResult1 = false;
            boolean remittanceResult2 = false;
            remittanceResult1 = moneyPort.requestMoneyDecrease(command.getFromMembershipId(), command.getAmount());
            remittanceResult2 = moneyPort.requestMoneyIncrease(command.getFromMembershipId(), command.getAmount());
            if(!remittanceResult1 || !remittanceResult2){
                return null;
            }
        }else if(command.getRemittanceType() == RemittanceType.BANK){
            // 3-2. 외부 은행 계좌
            // 외부 은행 계쫘가 적절한지 확인
            boolean remittanceResult = bankPort.requestFirmBanking(command.getToBankName(), command.getToBankAccountNumber(), command.getAmount());
            if(!remittanceResult) {
                return null;
            }
        }
        // 4. 송금 요청 상태를 성공으로 기록
        entity.setRemittanceStatus(RemittanceStatus.SUCCESS);
        boolean result = requestRemittancePort.saveRemittanceRequestHistory(entity);
        if(result) {
            return remittanceMapper.mapToDomainEntity(entity);
        }

        return null;
    }
}
