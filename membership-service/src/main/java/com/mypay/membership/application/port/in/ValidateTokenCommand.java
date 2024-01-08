package com.mypay.membership.application.port.in;

import com.mypay.common.SelfValidating;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class ValidateTokenCommand extends SelfValidating<ValidateTokenCommand> {

    @NotNull
    private final String jwtToken;

    public ValidateTokenCommand(String jwtToken) {
        this.jwtToken = jwtToken;
        this.validateSelf();
    }
}
