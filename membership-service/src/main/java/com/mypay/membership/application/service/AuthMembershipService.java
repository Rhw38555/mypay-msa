package com.mypay.membership.application.service;

import com.mypay.common.UseCase;
import com.mypay.membership.adapter.out.persistence.MembershipJpaEntity;
import com.mypay.membership.adapter.out.persistence.MembershipMapper;
import com.mypay.membership.adapter.out.vault.VaultAdapter;
import com.mypay.membership.application.port.in.*;
import com.mypay.membership.application.port.out.AuthMembershipPort;
import com.mypay.membership.application.port.out.FindMembershipPort;
import com.mypay.membership.application.port.out.ModifyMembershipPort;
import com.mypay.membership.application.port.out.RegisterMembershipPort;
import com.mypay.membership.domain.JwtToken;
import com.mypay.membership.domain.Membership;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;

import javax.transaction.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional
public class AuthMembershipService implements AuthMembershipUseCase {

    private final AuthMembershipPort authMembershipPort;
    private final FindMembershipPort findMembershipPort;
    private final ModifyMembershipPort modifyMembershipPort;
    private final MembershipMapper mapper;
    private final VaultAdapter vaultAdapter;

    @Override
    public Membership getMembershipByJwtToken(ValidateTokenCommand command) {
        String jwtToken = command.getJwtToken();
        boolean isValid = authMembershipPort.validateJwtToken(jwtToken);

        // 멤버십 id 가져오기
        if(isValid) {
            Membership.MembershipId membership = authMembershipPort.parseMembershipIdFromToken(jwtToken);
            String membershipIdString = membership.getMembershipId();

            // 고객의 refreshToken, 요청받은 refreshToken 일치
            MembershipJpaEntity membershipJpaEntity = findMembershipPort.findMembership(membership);
            if(!membershipJpaEntity.getRefreshToken().equals(
                    jwtToken
            )) {
                return null;
            }
            return mapper.mapToDomainEntity(membershipJpaEntity);
        }
        return null;
    }

    @Override
    public JwtToken refreshJwtTokenByRefreshToken(RefreshTokenCommand command) {
        String requestedRefreshToken = command.getRefreshToken();
        boolean isValid = authMembershipPort.validateJwtToken(requestedRefreshToken);

        // 멤버십 id 가져오기
        if(isValid) {
            Membership.MembershipId membership = authMembershipPort.parseMembershipIdFromToken(requestedRefreshToken);
            String membershipIdString = membership.getMembershipId();

            // 고객의 refreshToken, 요청받은 refreshToken 일치
            MembershipJpaEntity membershipJpaEntity = findMembershipPort.findMembership(membership);
            if(!membershipJpaEntity.getRefreshToken().equals(
                command.getRefreshToken()
            )) {
                return null;
            }

            if(membershipJpaEntity.isValid()) {
                String newJwtToken = authMembershipPort.generateJwtToken(
                        new Membership.MembershipId(membershipIdString)
                );

                return JwtToken.generateJwtToken(
                        new JwtToken.MembershipId(membershipIdString),
                        new JwtToken.MembershipJwtToken(newJwtToken),
                        new JwtToken.MembershipRefreshToken(requestedRefreshToken)
                );
            }
        }
        return null;
    }

    @Override
    public boolean validateJwtToken(ValidateTokenCommand command) {
        String jwtToken = command.getJwtToken();
        return authMembershipPort.validateJwtToken(jwtToken);
    }

    @Override
    public JwtToken loginMembership(LoginMembershipCommand command) {

        String membershipId = command.getMembershipId();
        MembershipJpaEntity membershipJpaEntity = findMembershipPort.findMembership(
                new Membership.MembershipId(membershipId));

        // 멤버십 상태확인,  비밀번호 확인
        if(membershipJpaEntity != null && membershipJpaEntity.isValid() &&
                vaultAdapter.decrypt(command.getPassword()).equals(membershipJpaEntity.getPassword())
        ) {
            String jwtToken = authMembershipPort.generateJwtToken(
                    new Membership.MembershipId(membershipId)
            );

            String refreshToken = authMembershipPort.generateRefreshToken(
                    new Membership.MembershipId(membershipId)
            );

            // refreshToken 저장
            modifyMembershipPort.modifyMembership(
                    new Membership.MembershipId(membershipId),
                    new Membership.MembershipName(membershipJpaEntity.getName()),
                    new Membership.MembershipEmail(membershipJpaEntity.getEmail()),
                    new Membership.MembershipAddress(membershipJpaEntity.getAddress()),
                    new Membership.MembershipIsValid(membershipJpaEntity.isValid()),
                    new Membership.MembershipIsCorp(membershipJpaEntity.isCorp()),
                    new Membership.MembershipRefreshToken(refreshToken)
            );

            return JwtToken.generateJwtToken(
                    new JwtToken.MembershipId(membershipId),
                    new JwtToken.MembershipJwtToken(jwtToken),
                    new JwtToken.MembershipRefreshToken(refreshToken)
            );
        }

        return null;
    }
}
