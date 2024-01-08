package com.mypay.banking.application.port.in;

import com.mypay.banking.FirmBankingStatus;
import com.mypay.common.SelfValidating;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class UpdateFirmbankingCommand extends SelfValidating<UpdateFirmbankingCommand> {

    @NotNull
    private String firmbankingRequestAggregateIdentifier;

    @NotNull
    private FirmBankingStatus status;

}
