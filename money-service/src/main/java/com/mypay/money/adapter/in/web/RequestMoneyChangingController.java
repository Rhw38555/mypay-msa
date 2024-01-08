package com.mypay.money.adapter.in.web;

import com.mypay.common.WebAdapter;
import com.mypay.money.application.port.in.*;
import com.mypay.money.domain.MemberMoney;
import com.mypay.money.domain.MoneyChangingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@WebAdapter
@RestController
@RequiredArgsConstructor
public class RequestMoneyChangingController {

    private final IncreaseMoneyChangingRequestUseCase increaseMoneyChangingRequestUseCase;
//    private final DecreaseMoneyChangingRequestUseCase decreaseMoneyChangingRequestUseCase;

//    @PostMapping(path = "/money/membership/{targetMembershipId}/increase")
//    MoneyChangingResultDetail increaseMoneyChangingRequest(@PathVariable String targetMembershipId, @RequestBody IncreaseMoneyChangingRequest request) {
//
//        IncreaseMoneyChangingRequestCommand command = IncreaseMoneyChangingRequestCommand.builder()
//                .targetMembershipId(targetMembershipId)
//                .amount(request.getAmount())
//                .build();
//
//        // MoneyChangingResultDetail 파싱 필요
////        MoneyChangingRequest moneyChangingRequest = increaseMoneyChangingRequestUseCase.increaseMoneyRequest(command);
//        MoneyChangingRequest moneyChangingRequest = increaseMoneyChangingRequestUseCase.increaseMoneyRequestAsync(command);
//        return MoneyChangingResultDetailMapper.mapToMoneyChangingResultDetail(moneyChangingRequest);
//    }

    // Eda
    @PostMapping(path = "/money/membership/{targetMembershipId}/increase")
    void increaseMoneyChangingRequestByEvent(@PathVariable String targetMembershipId, @RequestBody IncreaseMoneyChangingRequest request) {

        IncreaseMoneyChangingRequestCommand command = IncreaseMoneyChangingRequestCommand.builder()
                .targetMembershipId(targetMembershipId)
                .amount(request.getAmount())
                .build();

        // MoneyChangingResultDetail 파싱 필요
        increaseMoneyChangingRequestUseCase.increaseMoneyRequestByEvent(command);
    }


    @PostMapping(path = "/money/membership/{targetMembershipId}/decrease")
    void decreaseMoneyChangingRequest(@PathVariable String targetMembershipId, @RequestBody DecreaseMoneyChangingRequest request) {

        IncreaseMoneyChangingRequestCommand command = IncreaseMoneyChangingRequestCommand.builder()
                .targetMembershipId(targetMembershipId)
                .amount(request.getAmount() * -1)
                .build();
        // MoneyChangingResultDetail 파싱 작업
        increaseMoneyChangingRequestUseCase.increaseMoneyRequestByEvent(command);
    }

}
