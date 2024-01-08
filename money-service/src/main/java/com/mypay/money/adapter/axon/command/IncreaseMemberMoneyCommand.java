package com.mypay.money.adapter.axon.command;

import com.mypay.common.SelfValidating;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import javax.validation.constraints.NotNull;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class IncreaseMemberMoneyCommand extends SelfValidating<IncreaseMemberMoneyCommand> {

    // aggregateIdentifier 가지는 어그리거트를 변경시키는것이 목적이여서 선언해야됨.
    @NotNull
    @TargetAggregateIdentifier
    private String aggregateIdentifier;

    @NotNull
    private final String membershipId;

    @NotNull
    private final int amount;

}
