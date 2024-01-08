package com.mypay.remittance.domain;

import com.mypay.remittance.RemittanceStatus;
import com.mypay.remittance.RemittanceType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RemittanceRequest { // 송금 요청에 대한 상태

    @Getter private final String remittanceRequestId;

    @Getter private final String remittanceFromMembershipId;

    @Getter private final String toBankName;

    @Getter private final String toBankAccountNumber;

    @Getter private final RemittanceType remittanceType;

    @Getter private final RemittanceStatus registerStatus;

    @Getter private final int amount;



    public static RemittanceRequest generateRemittanceRequest(
            RemittanceRequestId remittanceId,
            RemittanceFromMembershipId remittanceFromMembershipId,
            ToBankName toBankName,
            ToBankAccountNumber toBankAccountNumber,
            RemittanceRequestType remittanceRequestType,
            RemittanceRequestStatus remittanceStatus,
            Amount amount
    ){
        return new RemittanceRequest(
                remittanceId.remittanceRequestId,
                remittanceFromMembershipId.remittanceFromMembershipId,
                toBankName.toBankName,
                toBankAccountNumber.toBankAccountNumber,
                remittanceRequestType.remittanceType,
                remittanceStatus.remittanceStatus,
                amount.amount
        );
    }

    @Value
    public static class RemittanceRequestId {
        public RemittanceRequestId(String value) {
            this.remittanceRequestId = value;
        }
        String remittanceRequestId ;
    }

    @Value
    public static class RemittanceFromMembershipId {
        public RemittanceFromMembershipId(String value) {
            this.remittanceFromMembershipId = value;
        }
        String remittanceFromMembershipId;
    }

    @Value
    public static class ToBankName {
        public ToBankName(String value) {
            this.toBankName = value;
        }
        String toBankName;
    }

    @Value
    public static class ToBankAccountNumber {
        public ToBankAccountNumber(String value) {
            this.toBankAccountNumber = value;
        }
        String toBankAccountNumber;
    }

    @Value
    public static class RemittanceRequestType {
        public RemittanceRequestType(RemittanceType value) {
            this.remittanceType = value;
        }
        RemittanceType remittanceType;
    }

    @Value
    public static class RemittanceRequestStatus {
        public RemittanceRequestStatus(RemittanceStatus value) {
            this.remittanceStatus = value;
        }
        RemittanceStatus remittanceStatus;
    }

    @Value
    public static class Amount {
        public Amount(int value) {
            this.amount = value;
        }
        int amount;
    }

}
