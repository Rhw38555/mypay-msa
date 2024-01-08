package com.mypay.money.adapter.in.web;

import com.mypay.common.WebAdapter;
import com.mypay.money.application.port.in.*;
import com.mypay.money.domain.MemberMoney;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@WebAdapter
@RestController
@RequiredArgsConstructor
public class MemberMoneyController {

    private final CreateMemberMoneyUseCase createMemberMoneyUseCase;
    private final FindMemberMoneyUseCase findMemberMoneyUseCase;

    @PostMapping(path = "/money/member/{membershipId}")
    void createMemberMoneyByEvent(@PathVariable String membershipId){
        createMemberMoneyUseCase.createMemberMoney(CreateMemberMoneyCommand.builder().membershipId(membershipId).build());
    }

    // ids 기준 member Money 리스트
    @PostMapping(path = "/money/member-money")
    List<MemberMoney> findMemberMoneyListByMembershipIds(@RequestBody FindMemberMoneyListByMembershipIdsRequest request) {

        FindMemberMoneyListByMembershipIdsCommand command = FindMemberMoneyListByMembershipIdsCommand.builder()
                .targetMembershipId(request.getMembershipIds())
                .build();

        // MoneyChangingResultDetail 파싱 작업
        return findMemberMoneyUseCase.findMemberMoneyListByMembershipIds(command);
    }

}
