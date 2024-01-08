package com.mypay.money.application.port.in;

import com.mypay.common.SelfValidating;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class IncreaseMoneyChangingRequestCommand extends SelfValidating<IncreaseMoneyChangingRequestCommand> {

    @NotNull
    private final String targetMembershipId;

    @NotNull
    private final int amount;

    public IncreaseMoneyChangingRequestCommand(String targetMembershipId, int amount) {
        this.targetMembershipId = targetMembershipId;
        this.amount = amount;
        this.validateSelf();
    }
}
