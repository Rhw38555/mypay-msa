package com.mypay.membership.adapter.in.web;

import com.mypay.common.WebAdapter;
import com.mypay.membership.application.port.in.*;
import com.mypay.membership.domain.JwtToken;
import com.mypay.membership.domain.Membership;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@WebAdapter
@RestController
@RequiredArgsConstructor
public class AuthMembershipController {

    private final AuthMembershipUseCase authMembershipUseCase;

    @PostMapping(path = "/membership/login")
    JwtToken loginMembership(@RequestBody LoginMembershipRequest request) {

        LoginMembershipCommand command = LoginMembershipCommand.builder()
                .membershipId(request.getMembershipId())
                .password(request.getPassword())
                .build();

        return authMembershipUseCase.loginMembership(command);
    }

    @PostMapping(path = "/membership/token/refresh")
    JwtToken refreshToken(@RequestBody RefreshTokenRequest request) {

        RefreshTokenCommand command = RefreshTokenCommand.builder()
                .refreshToken(request.getRefreshToken())
                .build();

        return authMembershipUseCase.refreshJwtTokenByRefreshToken(command);
    }

    @PostMapping(path = "/membership/token/valid")
    boolean validateToken(@RequestBody ValidateTokenRequest request) {

        ValidateTokenCommand command = ValidateTokenCommand.builder()
                .jwtToken(request.getJwtToken())
                .build();

        return authMembershipUseCase.validateJwtToken(command);
    }
}
