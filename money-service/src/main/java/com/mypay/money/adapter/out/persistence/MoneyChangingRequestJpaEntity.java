package com.mypay.money.adapter.out.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.mypay.money.ChangingMoneyStatus;
import com.mypay.money.ChangingType;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "money_changing_request")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoneyChangingRequestJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long moneyChangingRequestId;

    private String targetMembershipId;

    private ChangingType moneyChangingType;

    private int moneyAmount;

    private ChangingMoneyStatus changingMoneyStatus;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    private String uuid;

    public MoneyChangingRequestJpaEntity(String targetMembershipId, ChangingType moneyChangingType, int moneyAmount, ChangingMoneyStatus changingMoneyStatus, Timestamp timestamp, String uuid) {
        this.targetMembershipId = targetMembershipId;
        this.moneyChangingType = moneyChangingType;
        this.moneyAmount = moneyAmount;
        this.changingMoneyStatus = changingMoneyStatus;
        this.timestamp = timestamp;
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "MoneyChangingRequestJpaEntity{" +
                "moneyChangingRequestId=" + moneyChangingRequestId +
                ", targetMembershipId='" + targetMembershipId + '\'' +
                ", moneyChangingType=" + moneyChangingType +
                ", moneyAmount=" + moneyAmount +
                ", changingMoneyStatus=" + changingMoneyStatus +
                ", timestamp=" + timestamp +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
