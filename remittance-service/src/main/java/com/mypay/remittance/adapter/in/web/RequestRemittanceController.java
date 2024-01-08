package com.mypay.remittance.adapter.in.web;

import com.mypay.common.WebAdapter;
import com.mypay.remittance.application.port.in.RequestRemittanceCommand;
import com.mypay.remittance.application.port.in.RequestRemittanceUseCase;
import com.mypay.remittance.domain.RemittanceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@WebAdapter
@RestController
@RequiredArgsConstructor
public class RequestRemittanceController {

    private final RequestRemittanceUseCase requestRemittanceUseCase;

    @PostMapping(path = "/remittance/request")
    RemittanceRequest requestRemittance(@RequestBody RequestRemittanceRequest request) {


        RequestRemittanceCommand command = RequestRemittanceCommand.builder()
                .fromMembershipId(request.getFromMembershipId())
                .toMembershipId(request.getToMembershipId())
                .toBankName(request.getToBankName())
                .toBankAccountNumber(request.getToBankAccountNumber())
                .amount(request.getAmount())
                .remittanceType(request.getRemittanceType())
                .build();

        return requestRemittanceUseCase.requestRemittance(command);
    }
}
