package com.mypay.membership.adapter.out.persistence;

import com.mypay.common.PersistenceAdapter;
import com.mypay.membership.adapter.out.vault.VaultAdapter;
import com.mypay.membership.application.port.out.FindMembershipPort;
import com.mypay.membership.application.port.out.ModifyMembershipPort;
import com.mypay.membership.application.port.out.RegisterMembershipPort;
import com.mypay.membership.domain.Membership;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class MembershipPersistenceAdapter implements RegisterMembershipPort, FindMembershipPort, ModifyMembershipPort {

    private final SpringDataMembershipRepository membershipRepository;
    private final VaultAdapter vaultAdapter;
    @Override
    public MembershipJpaEntity createMembership(Membership.MembershipName membershipName, Membership.MembershipPassword password, Membership.MembershipEmail membershipEmail, Membership.MembershipAddress membershipAddress, Membership.MembershipIsValid membershipIsValid, Membership.MembershipIsCorp membershipIsCorp) {

        // email, password 암호화
        String originEmail = membershipEmail.getEmailValue();
        String encryptedEmail = vaultAdapter.encrypt(originEmail);
        String encryptedPassword = vaultAdapter.encrypt(password.getPassword());

        MembershipJpaEntity savedMembershipEntity = membershipRepository.save(
                new MembershipJpaEntity(
                        membershipName.getNameValue(),
                        encryptedPassword,
                        membershipAddress.getAddressValue(),
                        encryptedEmail,
                        membershipIsValid.isValidValue(),
                        membershipIsCorp.isCorpValue(),
                        ""
                )
        );

        MembershipJpaEntity clone = savedMembershipEntity.clone();
        clone.setEmail(membershipEmail.getEmailValue());
        return clone;
    }

    @Override
    public MembershipJpaEntity findMembership(Membership.MembershipId membershipId) {
        MembershipJpaEntity membershipJpaEntity = membershipRepository.getById(Long.parseLong(membershipId.getMembershipId()));
        String encryptedEmailString = membershipJpaEntity.getEmail();
        String decryptedEmailString = vaultAdapter.decrypt(encryptedEmailString);

        MembershipJpaEntity clone = membershipJpaEntity.clone();
        clone.setEmail(decryptedEmailString);
        return clone;
    }

    @Override
    public List<MembershipJpaEntity> findMembershipListByAddress(Membership.MembershipAddress membershipAddress) {
        // 관악구, 서초구, 강남구 중 하나
        List<MembershipJpaEntity> membershipJpaEntityList = membershipRepository.findByAddress(membershipAddress.getAddressValue())
                .stream()
                .map((entity) -> {
                    MembershipJpaEntity clone = entity.clone();
                    String decryptedEmail = vaultAdapter.decrypt(entity.getEmail());
                    clone.setEmail(decryptedEmail);
                    return clone;
                })
                .collect(Collectors.toList());
        return membershipJpaEntityList;
    }

    @Override
    public MembershipJpaEntity modifyMembership(Membership.MembershipId membershipId, Membership.MembershipName membershipName, Membership.MembershipEmail membershipEmail, Membership.MembershipAddress membershipAddress, Membership.MembershipIsValid membershipIsValid, Membership.MembershipIsCorp membershipIsCorp, Membership.MembershipRefreshToken refreshToken) {
        MembershipJpaEntity entity = membershipRepository.getById(Long.parseLong(membershipId.getMembershipId()));

        // email
        String encryptedEmail = vaultAdapter.encrypt(membershipEmail.getEmailValue());

        entity.setName(membershipName.getNameValue());
        entity.setAddress(membershipAddress.getAddressValue());
        entity.setEmail(encryptedEmail);
        entity.setCorp(membershipIsCorp.isCorpValue());
        entity.setValid(membershipIsValid.isValidValue());
        entity.setRefreshToken(refreshToken.getRefreshToken());
        membershipRepository.save(entity);

        MembershipJpaEntity clone = entity.clone();
        clone.setEmail(membershipEmail.getEmailValue());
        return clone;
    }
}
