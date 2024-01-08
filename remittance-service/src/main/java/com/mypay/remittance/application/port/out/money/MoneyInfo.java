package com.mypay.remittance.application.port.out.money;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 송금 서비스에서 머니 정보
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoneyInfo {
    private String membershipId;
    private int balance;
}
