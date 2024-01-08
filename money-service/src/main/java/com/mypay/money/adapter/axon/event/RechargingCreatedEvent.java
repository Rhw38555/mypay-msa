package com.mypay.money.adapter.axon.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RechargingCreatedEvent {
    // 충전 동작 요청이 생성 되었다는 이벤트
    private String rechargingRequestId;
    private String membershipId;
    private int amount;
    // aggregateId로 찾아야됨
    private String registeredBankAccountAggregateIdentifier;
    private String bankName;
    private String bankAccountNumber;

}
