package com.mypay.banking.application.port.in;

import com.mypay.common.SelfValidating;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class FindBankAccountCommand extends SelfValidating<FindBankAccountCommand> {

    @NotNull
    private final String membershipId;


    public FindBankAccountCommand(String membershipId) {
        this.membershipId = membershipId;

        this.validateSelf();
    }
}
