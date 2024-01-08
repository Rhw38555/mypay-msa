package com.mypay.money.application.port.out;

import com.mypay.money.domain.MemberMoney;

public interface CreateMemberMoneyPort {
    void createMemberMoney(
            MemberMoney.MembershipId membershipId,
            MemberMoney.MoneyAggregateIdentifier aggregateIdentifier
    );
}
