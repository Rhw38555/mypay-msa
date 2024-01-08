package com.mypay.membership.adapter.out.jwt;

import com.mypay.membership.application.port.out.AuthMembershipPort;
import com.mypay.membership.domain.Membership;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

// jwt token 생성 로직 전부
@Component
public class JwtTokenProvider implements AuthMembershipPort {

    private String jwtSecret; // 내부 비밀키

    private long jwtExpirationInMs; // jwt token 만료시간

    private long refreshTokenExpirationInMs; // refresh token 만료시간

    public JwtTokenProvider() {
        // 512bit 알고리즘 비밀키
        this.jwtSecret = "NYd4nEtyLtcU7cpS/1HTFVmQJd7MmrP+HafWoXZjWNOL7qKccOOUfQNEx5yvG6dfdpuBeyMs9eEbRmdBrPQCNg==";
        this.jwtExpirationInMs = 1000L * 20; // 20초
        this.refreshTokenExpirationInMs = 1000L * 60; // 1분
    }

    @Override
    public String generateJwtToken(Membership.MembershipId membershipId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(membershipId.getMembershipId())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    @Override
    public String generateRefreshToken(Membership.MembershipId membershipId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(membershipId.getMembershipId())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    @Override
    public boolean validateJwtToken(String jwtToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken);
            return true;
        } catch (MalformedJwtException ex) {
            // Invalid JWT token: 유효하지 않은 JWT 토큰일 때 발생하는 예외
        } catch (ExpiredJwtException ex) {
            // Expired JWT token: 토큰의 유효기간이 만료된 경우 발생하는 예외
        } catch (UnsupportedJwtException ex) {
            // Unsupported JWT token: 지원하지 않는 JWT 토큰일 때 발생하는 예외
        } catch (IllegalArgumentException ex) {
            // JWT claims string is empty: JWT 토큰이 비어있을 때 발생하는 예외
        }
        return false;
    }

    @Override
    public Membership.MembershipId parseMembershipIdFromToken(String jwtToken) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken).getBody();
        return new Membership.MembershipId(claims.getSubject());
    }
}
