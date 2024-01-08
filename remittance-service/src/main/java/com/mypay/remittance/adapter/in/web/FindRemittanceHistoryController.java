package com.mypay.remittance.adapter.in.web;

import com.mypay.common.WebAdapter;
import com.mypay.remittance.application.port.in.FindRemittanceCommand;
import com.mypay.remittance.application.port.in.FindRemittanceUseCase;
import com.mypay.remittance.application.port.in.RequestRemittanceCommand;
import com.mypay.remittance.application.port.in.RequestRemittanceUseCase;
import com.mypay.remittance.domain.RemittanceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@WebAdapter
@RestController
@RequiredArgsConstructor
public class FindRemittanceHistoryController {

    private final FindRemittanceUseCase findRemittanceUseCase;

    @GetMapping(path = "/remittance/{membershipId}")
    List<RemittanceRequest> findMoneyChangingRequest(@PathVariable String membershipId) {
        FindRemittanceCommand command = FindRemittanceCommand.builder()
                .membershipId(membershipId)
                .build();

        return findRemittanceUseCase.findRemittanceHistory(command);
    }
}
