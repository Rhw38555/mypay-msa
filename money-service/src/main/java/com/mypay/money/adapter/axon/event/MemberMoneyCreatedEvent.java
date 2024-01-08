package com.mypay.money.adapter.axon.event;

import com.mypay.common.SelfValidating;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class MemberMoneyCreatedEvent extends SelfValidating<MemberMoneyCreatedEvent> {

    @NotNull
    private String membershipId;

    public MemberMoneyCreatedEvent(String membershipId) {
        this.membershipId = membershipId;
        this.validateSelf();
    }

    public MemberMoneyCreatedEvent() {
    }
}
