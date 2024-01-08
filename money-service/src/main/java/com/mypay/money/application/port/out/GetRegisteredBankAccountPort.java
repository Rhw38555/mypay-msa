package com.mypay.money.application.port.out;

import com.mypay.money.adapter.out.persistence.MemberMoneyJpaEntity;
import com.mypay.money.domain.MemberMoney;

public interface GetRegisteredBankAccountPort {
    RegisteredBankAccountAggreateIdentifier getRegisterBankAccount(String membershipId);
}
