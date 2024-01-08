package com.mypay.remittance.application.service;

import com.mypay.common.UseCase;
import com.mypay.remittance.RemittanceStatus;
import com.mypay.remittance.RemittanceType;
import com.mypay.remittance.adapter.out.persistence.RemittanceMapper;
import com.mypay.remittance.adapter.out.persistence.RemittanceRequestJpaEntity;
import com.mypay.remittance.adapter.out.persistence.SpringDataRemittanceRequestRepository;
import com.mypay.remittance.adapter.out.service.membership.MembershipStatus;
import com.mypay.remittance.application.port.in.FindRemittanceCommand;
import com.mypay.remittance.application.port.in.FindRemittanceUseCase;
import com.mypay.remittance.application.port.in.RequestRemittanceCommand;
import com.mypay.remittance.application.port.in.RequestRemittanceUseCase;
import com.mypay.remittance.application.port.out.FindRemittancePort;
import com.mypay.remittance.application.port.out.RequestRemittancePort;
import com.mypay.remittance.application.port.out.banking.BankingPort;
import com.mypay.remittance.application.port.out.membership.GetMembershipPort;
import com.mypay.remittance.application.port.out.money.MoneyInfo;
import com.mypay.remittance.application.port.out.money.MoneyPort;
import com.mypay.remittance.domain.RemittanceRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@UseCase
@RequiredArgsConstructor
@Transactional
public class FindRemittanceService implements FindRemittanceUseCase {

    private final FindRemittancePort findRemittancePort;

    private final RemittanceMapper remittanceMapper;

    @Override
    public List<RemittanceRequest> findRemittanceHistory(FindRemittanceCommand command) {
        return findRemittancePort.findRemittanceHistory(command)
                .stream()
                .map(remittanceMapper::mapToDomainEntity)
                .collect(Collectors.toList());
    }
}
