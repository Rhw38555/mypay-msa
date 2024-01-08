package com.mypay.money.adapter.out.persistence;

import com.mypay.common.PersistenceAdapter;
import com.mypay.money.application.port.out.GetMemberMoneyPort;
import com.mypay.money.application.port.out.IncreaseMoneyPort;
import com.mypay.money.domain.MemberMoney;
import com.mypay.money.domain.MoneyChangingRequest;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class MoneyChangingRequestPersistenceAdapter implements IncreaseMoneyPort, GetMemberMoneyPort {

    private final SpringDataMoneyChangingRequestRepository moneyChangingRequestRepository;
    private final SpringDataMemberMoneyRepository memberMoneyRepository;

    @Override
    public MoneyChangingRequestJpaEntity createMoneyChangingRequest(MoneyChangingRequest.TargetMembershipId targetMembershipId, MoneyChangingRequest.MoneyChangingType moneyChangingType, MoneyChangingRequest.ChangingMoneyAmount changingMoneyAmount, MoneyChangingRequest.MoneyChangingStatus moneyChangingStatus, MoneyChangingRequest.Uuid uuid) {
        return moneyChangingRequestRepository.save(
                new MoneyChangingRequestJpaEntity(
                        targetMembershipId.getTargetMembershipId(),
                        moneyChangingType.getChangingType(),
                        changingMoneyAmount.getChangingMoneyAmount(),
                        moneyChangingStatus.getChangingMoneyStatus(),
                        new Timestamp(System.currentTimeMillis()),
                        UUID.randomUUID().toString()
                )
        );
    }

    @Override
    public MemberMoneyJpaEntity increaseMoney(MemberMoney.MembershipId membershipId, int increaseMoneyAmount) {
        MemberMoneyJpaEntity entity;
        try{
            entity = memberMoneyRepository.findByMembershipId(Long.parseLong(membershipId.getMembershipId())).get(0);
        } catch (Exception e){

            entity = new MemberMoneyJpaEntity(
                    Long.parseLong(membershipId.getMembershipId()),
                    increaseMoneyAmount,
                    ""
            );
            return memberMoneyRepository.save(entity);
        }

        entity.setBalance(entity.getBalance() + increaseMoneyAmount);
        return memberMoneyRepository.save(entity);
    }

    @Override
    public MemberMoneyJpaEntity getMemberMoney(MemberMoney.MembershipId membershipId) {
        List<MemberMoneyJpaEntity> entityList = memberMoneyRepository.findByMembershipId(Long.parseLong(membershipId.getMembershipId()));
        if(entityList.size() == 0) {
            MemberMoneyJpaEntity entity = new MemberMoneyJpaEntity(
                Long.parseLong(membershipId.getMembershipId()),
                0,
                ""
            );
            entity = memberMoneyRepository.save(entity);
            return entity;
        }
        return entityList.get(0);
    }

    @Override
    public List<MemberMoneyJpaEntity> getMemberMoneyListByMembershipIds(List<String> membershipIds) {
        return memberMoneyRepository.findMemberMoneyListByMembershipIds(membershipIds
                .stream()
                .map(Long::parseLong)
                .collect(Collectors.toList()));
    }
}
