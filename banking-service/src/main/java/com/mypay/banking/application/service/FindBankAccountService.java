package com.mypay.banking.application.service;

import com.mypay.banking.adapter.out.persistence.RegisteredBankAccountJpaEntity;
import com.mypay.banking.adapter.out.persistence.RegisteredBankAccountMapper;
import com.mypay.banking.application.port.in.FindBankAccountCommand;
import com.mypay.banking.application.port.in.FindBankAccountUseCase;
import com.mypay.banking.application.port.out.FindBankAccountPort;
import com.mypay.banking.domain.RegisteredBankAccount;
import com.mypay.common.UseCase;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional
public class FindBankAccountService implements FindBankAccountUseCase {

    private final FindBankAccountPort findBankAccountPort;
    private final RegisteredBankAccountMapper registerBankAccountMapper;

    @Override
    public RegisteredBankAccount findBankAccount(FindBankAccountCommand command) {
        RegisteredBankAccountJpaEntity findRegisteredBankAccount = findBankAccountPort.findRegisteredBankAccount(new RegisteredBankAccount.MembershipId(command.getMembershipId())).get(0);
        return registerBankAccountMapper.mapToDomainEntity(findRegisteredBankAccount);
    }

}
