package com.mypay.remittance.application.port.in;

import com.mypay.common.SelfValidating;
import com.mypay.remittance.RemittanceType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class RequestRemittanceCommand extends SelfValidating<RequestRemittanceCommand> {

    @NotNull
    private String fromMembershipId;

    private String toMembershipId;

    private String toBankName;

    private String toBankAccountNumber;

    private RemittanceType remittanceType; // 내부, 외부

//    @NotNull
//    @NotBlank
    private int amount;

    public RequestRemittanceCommand(String fromMembershipId, String toMembershipId, String toBankName, String toBankAccountNumber, RemittanceType remittanceType, int amount) {
        this.fromMembershipId = fromMembershipId;
        this.toMembershipId = toMembershipId;
        this.toBankName = toBankName;
        this.toBankAccountNumber = toBankAccountNumber;
        this.remittanceType = remittanceType;
        this.amount = amount;
        this.validateSelf();
    }
}
