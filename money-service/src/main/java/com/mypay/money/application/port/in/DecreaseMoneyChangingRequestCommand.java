package com.mypay.money.application.port.in;

import com.mypay.common.SelfValidating;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class DecreaseMoneyChangingRequestCommand extends SelfValidating<DecreaseMoneyChangingRequestCommand> {

    @NotNull
    private final String targetMembershipId;

    @NotNull
    private final int amount;

    public DecreaseMoneyChangingRequestCommand(String targetMembershipId, int amount) {
        this.targetMembershipId = targetMembershipId;
        this.amount = amount;
        this.validateSelf();
    }
}
