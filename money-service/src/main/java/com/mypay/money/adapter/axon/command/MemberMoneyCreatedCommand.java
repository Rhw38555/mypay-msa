package com.mypay.money.adapter.axon.command;

import com.mypay.common.SelfValidating;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class MemberMoneyCreatedCommand extends SelfValidating<MemberMoneyCreatedCommand> {

    @NotNull
    private String membershipId;

    public MemberMoneyCreatedCommand(String membershipId) {
        this.membershipId = membershipId;
        this.validateSelf();
    }

    public MemberMoneyCreatedCommand() {
    }
}
