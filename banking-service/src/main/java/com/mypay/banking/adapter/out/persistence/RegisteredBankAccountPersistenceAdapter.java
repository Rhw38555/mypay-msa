package com.mypay.banking.adapter.out.persistence;

import com.mypay.banking.application.port.out.FindBankAccountPort;
import com.mypay.banking.application.port.out.RegisterBankAccountPort;
import com.mypay.banking.domain.RegisteredBankAccount;
import com.mypay.common.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class RegisteredBankAccountPersistenceAdapter implements RegisterBankAccountPort, FindBankAccountPort {

    private final SpringDataRegisteredBankAccountRepository registeredBankAccountRepository;

    @Override
    public RegisteredBankAccountJpaEntity createRegisteredBankAccount(
            RegisteredBankAccount.MembershipId membershipId,
            RegisteredBankAccount.BankName bankName,
            RegisteredBankAccount.BankAccountNumber bankAccountNumber,
            RegisteredBankAccount.LinkedStatusIsValid linkedStatusIsValid,
            RegisteredBankAccount.AggregateIdentifier aggregateIdentifier
    ) {
        return registeredBankAccountRepository.save(
            new RegisteredBankAccountJpaEntity(
                    membershipId.getMembershipId(),
                    bankName.getBankName(),
                    bankAccountNumber.getBankAccountNumber(),
                    linkedStatusIsValid.isLinkedStatusIsValid(),
                    aggregateIdentifier.getAggregateIdentifier()
            )
        );
    }

    @Override
    public List<RegisteredBankAccountJpaEntity> findRegisteredBankAccount(RegisteredBankAccount.MembershipId membershipId) {
        return registeredBankAccountRepository.findByMembershipId(membershipId.getMembershipId());
    }
}
