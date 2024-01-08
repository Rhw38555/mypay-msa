package com.mypay.common.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestFirmbankingCommand {

    private String requestFirmbankingId;

    @TargetAggregateIdentifier
    private String aggregateIdentifier;

    private String rechargeRequestId;

    private String membershipId;

    private String fromBankName;

    private String fromBankAccountNumber;

    private String toBankName;

    private String toBankAccountNumber;

    private int moneyAmount;
}
