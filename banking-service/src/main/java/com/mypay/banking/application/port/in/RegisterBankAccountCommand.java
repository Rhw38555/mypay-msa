package com.mypay.banking.application.port.in;

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
public class RegisterBankAccountCommand extends SelfValidating<RegisterBankAccountCommand> {

    @NotNull
    private final String membershipId;

    @NotNull
    private final String bankName;

    @NotNull
    @NotBlank
    private final String bankAccount;

    private final boolean isValid;

    public RegisterBankAccountCommand(String membershipId, String bankName, String bankAccount, boolean isValid) {
        this.membershipId = membershipId;
        this.bankName = bankName;
        this.bankAccount = bankAccount;
        this.isValid = isValid;

        this.validateSelf();
    }
}
