package com.mypay.membership.application.port.in;

import com.mypay.common.SelfValidating;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class LoginMembershipCommand extends SelfValidating<LoginMembershipCommand> {

    @NotNull
    private final String membershipId;
    private final String password;

    public LoginMembershipCommand(String membershipId, String password) {
        this.membershipId = membershipId;
        this.password = password;
        this.validateSelf();
    }
}
