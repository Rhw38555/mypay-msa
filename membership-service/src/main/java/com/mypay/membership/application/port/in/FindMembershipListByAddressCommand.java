package com.mypay.membership.application.port.in;



import com.mypay.common.SelfValidating;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public
class FindMembershipListByAddressCommand extends SelfValidating<FindMembershipListByAddressCommand> {

    private final String addressName; //

}
