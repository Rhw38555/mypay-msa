package com.mypay.money.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import com.mypay.money.ChangingMoneyStatus;
import com.mypay.money.ChangingType;

import java.util.Date;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MoneyChangingRequest {

    @Getter private final String moneyChangingRequestId;

    // 어떤 고객의 증액/감액 요청했는지 멤버 정보
    @Getter private final String targetMembershipId;

    // 요청이 증액/감액 인지
    @Getter private final ChangingType changingType; // enum 0:증액 1:감액

    // 증액/감액 금액
    @Getter private final int changingMoneyAmount;

    // 머니 변액 요청에 대한 상태
    @Getter private final ChangingMoneyStatus changingMoneyStatus; // enum

    @Getter private final String uuid;

    @Getter private final Date createdAt;




    public static MoneyChangingRequest generateMoneyChangingRequest (
            MoneyChangingRequestId moneyChangingRequestId,
            TargetMembershipId targetMembershipId,
            MoneyChangingType moneyChangingType,
            ChangingMoneyAmount changingMoneyAmount,
            MoneyChangingStatus moneyChangingStatus,
            Uuid uuid
    ){
        return new MoneyChangingRequest(
                moneyChangingRequestId.getMoneyChangingRequestId(),
                targetMembershipId.getTargetMembershipId(),
                moneyChangingType.getChangingType(),
                changingMoneyAmount.getChangingMoneyAmount(),
                moneyChangingStatus.getChangingMoneyStatus(),
                uuid.getUuid(),
                new Date()
        );
    }

    @Value
    public static class MoneyChangingRequestId {
        public MoneyChangingRequestId(String value) {
            this.moneyChangingRequestId = value;
        }
        String moneyChangingRequestId ;
    }
    @Value
    public static class TargetMembershipId {
        public TargetMembershipId(String value) {
            this.targetMembershipId = value;
        }
        String targetMembershipId ;
    }
    @Value
    public static class MoneyChangingType {
        public MoneyChangingType(ChangingType value) {
            this.changingType = value;
        }
        ChangingType changingType ;
    }
    @Value
    public static class ChangingMoneyAmount {
        public ChangingMoneyAmount(int value) {
            this.changingMoneyAmount = value;
        }
        int changingMoneyAmount ;
    }
    @Value
    public static class MoneyChangingStatus {
        public MoneyChangingStatus(ChangingMoneyStatus value) {
            this.changingMoneyStatus = value;
        }
        ChangingMoneyStatus changingMoneyStatus ;
    }

    @Value
    public static class Uuid {
        public Uuid(String uuid) {
            this.uuid = uuid;
        }
        String uuid ;
    }

}
