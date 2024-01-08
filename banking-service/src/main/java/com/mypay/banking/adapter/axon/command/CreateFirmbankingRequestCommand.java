package com.mypay.banking.adapter.axon.command;

import com.mypay.banking.adapter.axon.aggregate.FirmbankingRequestAggregate;
import lombok.*;
import org.axonframework.commandhandling.CommandHandler;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateFirmbankingRequestCommand {

    private String fromBankName;
    private String fromBankAccountNumber;
    private String toBankName;
    private String toBankAccountNumber;
    private int moneyAmount;
}
