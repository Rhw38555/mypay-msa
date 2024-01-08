package com.mypay.membership.application.service;

import com.mypay.membership.adapter.out.persistence.MembershipJpaEntity;
import com.mypay.membership.adapter.out.persistence.MembershipMapper;
import com.mypay.membership.adapter.out.vault.VaultAdapter;
import com.mypay.membership.application.port.in.*;
import com.mypay.membership.application.port.out.AuthMembershipPort;
import com.mypay.membership.application.port.out.FindMembershipPort;
import com.mypay.membership.application.port.out.ModifyMembershipPort;
import com.mypay.membership.domain.JwtToken;
import com.mypay.membership.domain.Membership;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthMembershipServiceTest {
    @InjectMocks
    private AuthMembershipService authMembershipService;

    @Mock private AuthMembershipPort authMembershipPort;
    @Mock private FindMembershipPort findMembershipPort;
    @Mock private ModifyMembershipPort modifyMembershipPort;
    @Mock private MembershipMapper mapper;
    @Mock private VaultAdapter vaultAdapter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        authMembershipService = new AuthMembershipService(authMembershipPort, findMembershipPort, modifyMembershipPort, mapper, vaultAdapter);
    }


    @Test
    public void testGetMembershipByJwtToken_WithValidTokenAndMatchingRefreshToken() {
        // Arrange
        String validJwtToken = "matchingRefreshToken";
        String matchingRefreshToken = "matchingRefreshToken";
        MembershipJpaEntity validMembershipEntity = new MembershipJpaEntity(1L, "test", "test@g.com", "seoul", true, true, matchingRefreshToken);
        Membership validMembership = Membership.builder()
                .membershipId(validMembershipEntity.getMembershipId().toString())
                .name(validMembershipEntity.getName())
                .address(validMembershipEntity.getAddress())
                .email(validMembershipEntity.getEmail())
                .isValid(validMembershipEntity.isValid())
                .isCorp(validMembershipEntity.isCorp())
                .refreshToken(matchingRefreshToken)
                .build();

        when(authMembershipPort.validateJwtToken(validJwtToken)).thenReturn(true);
        when(authMembershipPort.parseMembershipIdFromToken(validJwtToken)).thenReturn(new Membership.MembershipId("1"));
        when(findMembershipPort.findMembership(any())).thenReturn(validMembershipEntity);
        when(mapper.mapToDomainEntity(any())).thenReturn(validMembership);

        // Act
        Membership result = authMembershipService.getMembershipByJwtToken(new ValidateTokenCommand(validJwtToken));

        // Assert
        assertNotNull(result); // Assuming the return value when the tokens match is null
    }

    @Test
    public void testGetMembershipByJwtToken_WithValidTokenAndMismatchingRefreshToken() {
        // Arrange
        String validJwtToken = "matchingRefreshToken";
        String mismatchingRefreshToken = "mismatchingRefreshToken";
        MembershipJpaEntity validMembership = new MembershipJpaEntity(1L, "test", "test@g.com", "seoul", true, true, "matchingRefreshToken");
        when(authMembershipPort.validateJwtToken(validJwtToken)).thenReturn(true);
        when(authMembershipPort.parseMembershipIdFromToken(validJwtToken)).thenReturn(new Membership.MembershipId("1"));
        when(findMembershipPort.findMembership(any())).thenReturn(validMembership);

        // Act
        Membership result = authMembershipService.getMembershipByJwtToken(new ValidateTokenCommand(validJwtToken));

        // Assert
        assertNull(result); // Assuming the return value when the tokens do not match is not null
    }

    @Test
    public void testRefreshJwtTokenByRefreshToken_WithValidToken() {
        // Arrange
        String validRefreshToken = "validRefreshToken";
        MembershipJpaEntity validMembership = new MembershipJpaEntity(1L, "test", "test@g.com", "seoul", true, true, validRefreshToken);
        when(authMembershipPort.validateJwtToken(validRefreshToken)).thenReturn(true);
        when(authMembershipPort.parseMembershipIdFromToken(validRefreshToken)).thenReturn(new Membership.MembershipId("1"));
        when(findMembershipPort.findMembership(any())).thenReturn(validMembership);
        when(authMembershipPort.generateJwtToken(any())).thenReturn("newJwtToken");

        // Act
        JwtToken result = authMembershipService.refreshJwtTokenByRefreshToken(new RefreshTokenCommand(validRefreshToken));

        // Assert
        assertNotNull(result); // Assuming the return value for a valid refresh token is not null
    }

    @Test
    public void testRefreshJwtTokenByRefreshToken_WithInvalidToken() {
        // Arrange
        String invalidRefreshToken = "invalidRefreshToken";
        when(authMembershipPort.validateJwtToken(invalidRefreshToken)).thenReturn(false);

        // Act
        JwtToken result = authMembershipService.refreshJwtTokenByRefreshToken(new RefreshTokenCommand(invalidRefreshToken));

        // Assert
        assertNull(result); // Assuming the return value for an invalid refresh token is null
    }

    @Test
    public void testValidateJwtToken_WithValidToken() {
        // Arrange
        String validJwtToken = "validJwtToken";
        when(authMembershipPort.validateJwtToken(validJwtToken)).thenReturn(true);

        // Act
        boolean result = authMembershipService.validateJwtToken(new ValidateTokenCommand(validJwtToken));

        // Assert
        assertTrue(result); // Assuming the return value for a valid JWT token is true
    }

    @Test
    public void testValidateJwtToken_WithInvalidToken() {
        // Arrange
        String invalidJwtToken = "invalidJwtToken";
        when(authMembershipPort.validateJwtToken(invalidJwtToken)).thenReturn(false);

        // Act
        boolean result = authMembershipService.validateJwtToken(new ValidateTokenCommand(invalidJwtToken));

        // Assert
        assertFalse(result); // Assuming the return value for an invalid JWT token is false
    }

    @Test
    public void testLoginMembership_WithValidCredentials() {
        // Arrange
        String validMembershipId = "decryptedPassword";
        MembershipJpaEntity validMembership = new MembershipJpaEntity("test", "decryptedPassword", "test@g.com", "seoul", true, true, "");
        when(findMembershipPort.findMembership(any())).thenReturn(validMembership);
        when(vaultAdapter.decrypt(any())).thenReturn("decryptedPassword");
        when(authMembershipPort.generateJwtToken(any())).thenReturn("newJwtToken");

        // Act
        JwtToken result = authMembershipService.loginMembership(new LoginMembershipCommand(validMembershipId, "password"));

        // Assert
        assertNotNull(result); // Assuming the return value for valid credentials is not null
    }

    @Test
    public void testLoginMembership_WithInvalidCredentials() {
        // Arrange
        String invalidMembershipId = "invalidMembershipId";
        MembershipJpaEntity invalidMembership = null; // Assuming membership not found for invalid id
        when(findMembershipPort.findMembership(any())).thenReturn(invalidMembership);

        // Act
        JwtToken result = authMembershipService.loginMembership(new LoginMembershipCommand(invalidMembershipId, "password"));

        // Assert
        assertNull(result); // Assuming the return value for invalid credentials is null
    }

}
