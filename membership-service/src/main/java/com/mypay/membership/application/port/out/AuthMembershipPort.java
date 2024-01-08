package com.mypay.membership.application.port.out;

import com.mypay.membership.adapter.out.persistence.MembershipJpaEntity;
import com.mypay.membership.domain.Membership;

public interface AuthMembershipPort {

    // membershipId 기준 jwt 토큰생성
    String generateJwtToken(
            Membership.MembershipId membershipId
    );

    // membershipId 기준 refresh 토큰 생성
    String generateRefreshToken(
            Membership.MembershipId membershipId
    );

    // jwt 유효성 검사
    boolean validateJwtToken(String jwtToken);

    // jwtToken으로 어떤 멤버십 id를 가지는지 확인
    Membership.MembershipId parseMembershipIdFromToken(String jwtToken);

}
