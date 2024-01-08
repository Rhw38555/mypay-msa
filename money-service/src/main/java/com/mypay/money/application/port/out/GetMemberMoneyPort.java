package com.mypay.money.application.port.out;

import com.mypay.money.adapter.out.persistence.MemberMoneyJpaEntity;
import com.mypay.money.domain.MemberMoney;

import java.util.List;

public interface GetMemberMoneyPort {
    MemberMoneyJpaEntity getMemberMoney(
            MemberMoney.MembershipId membershipId
    );

    List<MemberMoneyJpaEntity> getMemberMoneyListByMembershipIds(
            List<String> membershipIds
    );
}
