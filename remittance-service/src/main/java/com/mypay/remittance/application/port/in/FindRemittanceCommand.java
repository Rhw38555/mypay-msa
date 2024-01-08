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
public class FindRemittanceCommand extends SelfValidating<FindRemittanceCommand> {

    @NotNull
    private String membershipId;

    public FindRemittanceCommand(String membershipId) {
        this.membershipId = membershipId;
        this.validateSelf();
    }
}
