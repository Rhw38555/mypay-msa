package com.mypay.membership.adapter.in.web;

import com.mypay.common.WebAdapter;
import com.mypay.membership.application.port.in.FindMembershipCommand;
import com.mypay.membership.application.port.in.FindMembershipListByAddressCommand;
import com.mypay.membership.application.port.in.FindMembershipUseCase;
import com.mypay.membership.domain.Membership;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class FindMembershipController {
    private final FindMembershipUseCase findMembershipUseCase;
    @GetMapping(path = "/membership/{membershipId}")
    ResponseEntity<Membership> findMembershipByMemberId(@PathVariable String membershipId) {

        FindMembershipCommand command = FindMembershipCommand.builder()
                .membershipId(membershipId)
                .build();
        return ResponseEntity.ok(findMembershipUseCase.findMembership(command));
    }

    // 특정구 기준 멤버 정보 리스트
    @GetMapping(path = "/membership/address/{addressName}")
    ResponseEntity<List<Membership>> findMembershipListByAddressName(@PathVariable String addressName) {

        FindMembershipListByAddressCommand command = FindMembershipListByAddressCommand.builder()
                .addressName(addressName)
                .build();
        return ResponseEntity.ok(findMembershipUseCase.findMembershipListByAddress(command));
    }
}
