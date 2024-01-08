package com.mypay.banking.adapter.in.web;

import com.mypay.banking.application.port.in.FindBankAccountCommand;
import com.mypay.banking.application.port.in.FindBankAccountUseCase;
import com.mypay.banking.application.port.in.RegisterBankAccountCommand;
import com.mypay.banking.application.port.in.RegisterBankAccountUseCase;
import com.mypay.banking.domain.RegisteredBankAccount;
import com.mypay.common.WebAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@WebAdapter
@RestController
@RequiredArgsConstructor
public class FindBankAccountController {

    private final FindBankAccountUseCase findBankAccountUseCase;

    // 계좌를 등록하는 작업이 계좌에 대한 기본 작업
    @GetMapping(path = "/banking/accounts/{membershipId}")
    ResponseEntity<RegisteredBankAccount> findRegisteredBankingAccountByMemberId(@PathVariable String membershipId) {
        FindBankAccountCommand command = FindBankAccountCommand.builder()
                .membershipId(membershipId)
                .build();

        return ResponseEntity.ok(findBankAccountUseCase.findBankAccount(command));
    }
}
