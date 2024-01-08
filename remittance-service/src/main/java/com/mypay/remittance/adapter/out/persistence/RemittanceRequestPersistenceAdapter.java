package com.mypay.remittance.adapter.out.persistence;

import com.mypay.common.PersistenceAdapter;
import com.mypay.remittance.application.port.in.FindRemittanceCommand;
import com.mypay.remittance.application.port.in.RequestRemittanceCommand;
import com.mypay.remittance.application.port.out.FindRemittancePort;
import com.mypay.remittance.application.port.out.RequestRemittancePort;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@PersistenceAdapter
@RequiredArgsConstructor
public class RemittanceRequestPersistenceAdapter implements RequestRemittancePort, FindRemittancePort {
    private final SpringDataRemittanceRequestRepository remittanceRequestRepository;

    @Override
    public RemittanceRequestJpaEntity createRemittanceRequestHistory(RequestRemittanceCommand command) {
        return remittanceRequestRepository.save(RemittanceRequestJpaEntity.builder()
                        .fromMembershipId(command.getFromMembershipId())
                        .toMembershipId(command.getToMembershipId())
                        .toBankName(command.getToBankName())
                        .toBankAccountNumber(command.getToBankAccountNumber())
                        .remittanceType(command.getRemittanceType())
                        .amount(command.getAmount())
                .build());
    }

    @Override
    public Boolean saveRemittanceRequestHistory(RemittanceRequestJpaEntity entity) {
        remittanceRequestRepository.save(entity);
        return true;
    }

    @Override
    public List<RemittanceRequestJpaEntity> findRemittanceHistory(FindRemittanceCommand command) {
        return remittanceRequestRepository.findByMembershipId(Long.parseLong(command.getMembershipId()));
    }
}
