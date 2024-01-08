package com.mypay.remittance.adapter.out.persistence;

import com.mypay.remittance.RemittanceStatus;
import com.mypay.remittance.RemittanceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Table(name = "remittance_request")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RemittanceRequestJpaEntity {

    @Id
    @GeneratedValue
    private Long remittanceRequestId;

    private String fromMembershipId;

    private String toMembershipId;

    private String toBankName;

    private String toBankAccountNumber;

    private RemittanceType remittanceType; // memberhsip 내부, bank 외부

    private RemittanceStatus remittanceStatus;

    private int amount;

    public RemittanceRequestJpaEntity(String fromMembershipId, String toMembershipId, String toBankName, String toBankAccountNumber, RemittanceType remittanceType, RemittanceStatus remittanceStatus, int amount) {
        this.fromMembershipId = fromMembershipId;
        this.toMembershipId = toMembershipId;
        this.toBankName = toBankName;
        this.toBankAccountNumber = toBankAccountNumber;
        this.remittanceType = remittanceType;
        this.remittanceStatus = remittanceStatus;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "RemittanceRequestJpaEntity{" +
                "remittanceRequestId=" + remittanceRequestId +
                ", fromMembershipId='" + fromMembershipId + '\'' +
                ", toMembershipId='" + toMembershipId + '\'' +
                ", toBankName='" + toBankName + '\'' +
                ", toBankAccountNumber='" + toBankAccountNumber + '\'' +
                ", remittanceType=" + remittanceType +
                ", remittanceStatus=" + remittanceStatus +
                ", amount=" + amount +
                '}';
    }
}
