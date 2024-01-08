package com.mypay.membership.application.port.in;


import com.mypay.membership.domain.JwtToken;
import com.mypay.membership.domain.Membership;

public interface AuthMembershipUseCase {
    JwtToken loginMembership(LoginMembershipCommand command);
    JwtToken refreshJwtTokenByRefreshToken(RefreshTokenCommand command);
    boolean validateJwtToken(ValidateTokenCommand command);
    Membership getMembershipByJwtToken(ValidateTokenCommand command);
}
