package com.mypay.banking.adapter.in.web;

import com.mypay.banking.application.port.in.RegisterBankAccountUseCase;
import com.mypay.banking.application.port.in.RegisterBankAccountCommand;
import com.mypay.banking.domain.RegisteredBankAccount;
import com.mypay.common.WebAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@WebAdapter
@RestController
@RequiredArgsConstructor
public class RegisterBankAccountController {

    private final RegisterBankAccountUseCase registerBankAccountUseCase;

//    // 계좌를 등록하는 작업이 계좌에 대한 기본 작업
//    @PostMapping(path = "/banking/accounts/{membershipId}")
//    RegisteredBankAccount registerBankAccount(@PathVariable String membershipId, @RequestBody RegisterBankAccountRequest request) {
//
//        RegisterBankAccountCommand command = RegisterBankAccountCommand.builder()
//                .membershipId(membershipId)
//                .bankName(request.getBankName())
//                .bankAccount(request.getBankAccountNumber())
//                .isValid(true)
//                .build();
//
//        return registerBankAccountUseCase.registerBankAccount(command);
//    }

    // 계좌를 등록하는 작업이 계좌에 대한 기본 작업
    @PostMapping(path = "/banking/accounts/{membershipId}")
    void registerBankAccounByEvent(@PathVariable String membershipId, @RequestBody RegisterBankAccountRequest request) {

        RegisterBankAccountCommand command = RegisterBankAccountCommand.builder()
                .membershipId(membershipId)
                .bankName(request.getBankName())
                .bankAccount(request.getBankAccountNumber())
                .isValid(true)
                .build();

        registerBankAccountUseCase.registerBankAccountByEvent(command);
    }
}
