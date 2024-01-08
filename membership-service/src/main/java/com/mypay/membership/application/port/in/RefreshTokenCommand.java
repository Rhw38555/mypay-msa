package com.mypay.membership.application.port.in;

import com.mypay.common.SelfValidating;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class RefreshTokenCommand extends SelfValidating<RefreshTokenCommand> {

    @NotNull
    private final String refreshToken;

    public RefreshTokenCommand(String refreshToken) {
        this.refreshToken = refreshToken;
        this.validateSelf();
    }
}
