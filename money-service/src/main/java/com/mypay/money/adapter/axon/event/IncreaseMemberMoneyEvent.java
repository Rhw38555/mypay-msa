package com.mypay.money.adapter.axon.event;

import com.mypay.common.SelfValidating;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class IncreaseMemberMoneyEvent extends SelfValidating<IncreaseMemberMoneyEvent> {

    private String aggregateIdentifier;

    @NotNull
    private String membershipId;

    private int amount;

    public IncreaseMemberMoneyEvent(String aggregateIdentifier, String membershipId, int amount) {
        this.aggregateIdentifier = aggregateIdentifier;
        this.membershipId = membershipId;
        this.amount = amount;
    }

    public IncreaseMemberMoneyEvent() {
    }
}
