package com.mypay.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckRegisteredBankAccountEvent {

    private String rechargingRequestId;

    private String checkedRegisteredBankAccountId;

    private String membershipId;

    private boolean isChecked;

    private int amount;

    private String firmBankingRequestAggregateIdentifier;

    private String fromBankName;

    private String fromBankAccountNumber;

    @Override
    public String toString() {
        return "CheckRegisteredBankAccountEvent{" +
                "rechargingRequestId='" + rechargingRequestId + '\'' +
                ", checkedRegisteredBankAccountId='" + checkedRegisteredBankAccountId + '\'' +
                ", membershipId='" + membershipId + '\'' +
                ", isChecked=" + isChecked +
                ", amount=" + amount +
                ", firmBankingRequestAggregateIdentifier='" + firmBankingRequestAggregateIdentifier + '\'' +
                ", toBankName='" + fromBankName + '\'' +
                ", toBankAccountNumber='" + fromBankAccountNumber + '\'' +
                '}';
    }
}
