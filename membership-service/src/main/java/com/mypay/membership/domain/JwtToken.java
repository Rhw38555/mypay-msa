package com.mypay.membership.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtToken {
    @Getter private final String membershipId;
    @Getter private final String jwtToken;
    @Getter private final String refreshToken;

    // Membership
    // 오염이 되면 안되는 클래스. 고객 정보. 핵심 도메인

    public static JwtToken generateJwtToken (
            MembershipId membershipId
            , MembershipJwtToken membershipJwtToken
            , MembershipRefreshToken membershipRefreshToken
    ){
        return new JwtToken(
                membershipId.membershipId,
                membershipJwtToken.jwtToken,
                membershipRefreshToken.refreshToken
        );
    }

    @Value
    public static class MembershipId {
        public MembershipId(String value) {
            this.membershipId = value;
        }
        String membershipId ;
    }

    @Value
    public static class MembershipJwtToken {
        public MembershipJwtToken(String value) {
            this.jwtToken = value;
        }
        String jwtToken ;
    }
    @Value
    public static class MembershipRefreshToken {
        public MembershipRefreshToken(String value) {
            this.refreshToken = value;
        }
        String refreshToken;
    }

}
