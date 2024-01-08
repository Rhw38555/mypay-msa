package com.mypay.money.adapter.out.persistence;

import com.mypay.common.PersistenceAdapter;
import com.mypay.money.application.port.out.CreateMemberMoneyPort;
import com.mypay.money.application.port.out.IncreaseMoneyPort;
import com.mypay.money.domain.MemberMoney;
import com.mypay.money.domain.MoneyChangingRequest;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@PersistenceAdapter
@RequiredArgsConstructor
public class MemberMoneyCreatePersistenceAdapter implements CreateMemberMoneyPort {

    private final SpringDataMoneyChangingRequestRepository moneyChangingRequestRepository;
    private final SpringDataMemberMoneyRepository memberMoneyRepository;

    @Override
    public void createMemberMoney(MemberMoney.MembershipId membershipId, MemberMoney.MoneyAggregateIdentifier aggregateIdentifier) {
        MemberMoneyJpaEntity entity = new MemberMoneyJpaEntity(
                Long.parseLong(membershipId.getMembershipId()),
                0,
                aggregateIdentifier.getAggregateIdentifier()
        );
        memberMoneyRepository.save(entity);
    }
}
