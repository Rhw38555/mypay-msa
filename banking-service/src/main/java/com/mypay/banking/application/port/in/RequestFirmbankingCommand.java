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
public class RequestFirmbankingCommand extends SelfValidating<RequestFirmbankingCommand> {

    @NotNull
    private final String fromBankName;

    @NotNull
    private final String fromBankAccountNumber;

    @NotNull
    private final String toBankName;

    @NotNull
    private final String toBankAccountNumber;

    @NotNull
    private final int monoeyAmount;

    public RequestFirmbankingCommand(String fromBankName, String fromBankAccountNumber, String toBankName, String toBankAccountNumber, int monoeyAmount) {
        this.fromBankName = fromBankName;
        this.fromBankAccountNumber = fromBankAccountNumber;
        this.toBankName = toBankName;
        this.toBankAccountNumber = toBankAccountNumber;
        this.monoeyAmount = monoeyAmount;
        this.validateSelf();
    }
}
