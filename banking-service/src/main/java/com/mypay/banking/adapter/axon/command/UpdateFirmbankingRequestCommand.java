package com.mypay.banking.adapter.axon.command;

import com.mypay.banking.FirmBankingStatus;
import lombok.*;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateFirmbankingRequestCommand {

    @NotNull
    @TargetAggregateIdentifier
    private String aggregateIdentifier;

    private FirmBankingStatus status;


}
