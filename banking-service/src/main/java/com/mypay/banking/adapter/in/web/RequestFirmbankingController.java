package com.mypay.banking.adapter.in.web;

import com.mypay.banking.application.port.in.*;
import com.mypay.banking.domain.FirmbankingRequest;
import com.mypay.banking.domain.RegisteredBankAccount;
import com.mypay.common.WebAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@WebAdapter
@RestController
@RequiredArgsConstructor
public class RequestFirmbankingController {

    private final RequestFirmbankingUseCase requestFirmbankingUsecase;
    private final UpdateFirmbankingUseCase updateFirmbankingUseCase;

    // 계좌를 등록하는 작업이 계좌에 대한 기본 작업
//    @PostMapping(path = "/banking/firmbanking/account")
//    FirmbankingRequest registerBankAccount(@RequestBody RequestFirmbankingRequest request) {
//
//        RequestFirmbankingCommand command = RequestFirmbankingCommand.builder()
//                .fromBankName(request.getFromBankName())
//                .fromBankAccountNumber(request.getFromBankAccountNumber())
//                .toBankName(request.getToBankName())
//                .toBankAccountNumber(request.getToBankAccountNumber())
//                .monoeyAmount(request.getMoneyAmount())
//                .build();
//
//        return requestFirmbankingUsecase.requestFirmbanking(command);
//    }

    // 계좌를 등록하는 작업이 계좌에 대한 기본 작업
    @PostMapping(path = "/banking/firmbanking/acoount")
    void registerBankAccountByEvent(@RequestBody RequestFirmbankingRequest request) {

        RequestFirmbankingCommand command = RequestFirmbankingCommand.builder()
                .fromBankName(request.getFromBankName())
                .fromBankAccountNumber(request.getFromBankAccountNumber())
                .toBankName(request.getToBankName())
                .toBankAccountNumber(request.getToBankAccountNumber())
                .monoeyAmount(request.getMoneyAmount())
                .build();

        requestFirmbankingUsecase.requestFirmbankingByEvent(command);
    }

    // 계좌를 수정 작업
    @PutMapping(path = "/banking/firmbanking/acoount")
    void updateBankAccountByEvent(@RequestBody UpdateFirmbankingRequest request) {

        UpdateFirmbankingCommand command = UpdateFirmbankingCommand.builder()
                .firmbankingRequestAggregateIdentifier(request.getFirmbankingRequestAggregateIdentifier())
                .status(request.getStatus())
                .build();

        updateFirmbankingUseCase.updateFirmbankingByEvent(command);
    }
}
