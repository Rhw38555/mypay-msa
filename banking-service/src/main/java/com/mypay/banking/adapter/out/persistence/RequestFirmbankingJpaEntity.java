package com.mypay.banking.adapter.out.persistence;

import com.mypay.banking.FirmBankingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "request_firmbanking")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestFirmbankingJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestFirmbankingId;

    private String fromBankName;

    private String fromBankAccountNumber;

    private String toBankName;

    private String toBankAccountNumber;

    private int moneyAmount; // won

    private FirmBankingStatus firmbankingStatus; // 0:요청, 1: 완료, 2: 실패

    private UUID uuid;

    private String aggregateIdentifier;

    public RequestFirmbankingJpaEntity(String fromBankName, String fromBankAccountNumber, String toBankName, String toBankAccountNumber, int moneyAmount, FirmBankingStatus firmbankingStatus, UUID uuid, String aggregateIdentifier) {
        this.fromBankName = fromBankName;
        this.fromBankAccountNumber = fromBankAccountNumber;
        this.toBankName = toBankName;
        this.toBankAccountNumber = toBankAccountNumber;
        this.moneyAmount = moneyAmount;
        this.firmbankingStatus = firmbankingStatus;
        this.uuid = uuid;
        this.aggregateIdentifier = aggregateIdentifier;
    }

    @Override
    public String toString() {
        return "RequestFirmbankingJpaEntity{" +
                "requestFirmbankingId=" + requestFirmbankingId +
                ", fromBankName='" + fromBankName + '\'' +
                ", fromBankAccountNumber='" + fromBankAccountNumber + '\'' +
                ", toBankName='" + toBankName + '\'' +
                ", toBankAccountNumber='" + toBankAccountNumber + '\'' +
                ", moneyAmount=" + moneyAmount +
                ", firmbankingStatus=" + firmbankingStatus +
                ", uuid=" + uuid +
                ", aggregateIdentifier='" + aggregateIdentifier + '\'' +
                '}';
    }
}
