package com.mypay.banking.application.port.out;

import com.mypay.banking.adapter.out.persistence.RegisteredBankAccountJpaEntity;
import com.mypay.banking.domain.RegisteredBankAccount;

import java.util.List;

public interface FindBankAccountPort {

    List<RegisteredBankAccountJpaEntity> findRegisteredBankAccount(
            RegisteredBankAccount.MembershipId membershipId
    );
}
