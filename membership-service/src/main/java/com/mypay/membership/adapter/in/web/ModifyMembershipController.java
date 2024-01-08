package com.mypay.membership.adapter.in.web;

import com.mypay.common.WebAdapter;
import com.mypay.membership.application.port.in.ModifyMembershipCommand;
import com.mypay.membership.application.port.in.ModifyMembershipUseCase;
import com.mypay.membership.domain.Membership;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class ModifyMembershipController {
    private final ModifyMembershipUseCase modifyMembershipUseCase;
    @PutMapping(path = "/membership")
    ResponseEntity<Membership> modifyMembershipByMemberId(@PathVariable String membershipId, @RequestBody ModifyMembershipRequest request) {

        ModifyMembershipCommand command = ModifyMembershipCommand.builder()
                .membershipId(membershipId)
                .name(request.getName())
                .address(request.getAddress())
                .email(request.getEmail())
                .isValid(request.isValid())
                .isCorp(request.isCorp())
                .build();
        return ResponseEntity.ok(modifyMembershipUseCase.modifyMembership(command));
    }
}
