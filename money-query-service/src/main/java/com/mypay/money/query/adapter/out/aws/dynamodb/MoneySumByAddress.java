package com.mypay.money.query.adapter.out.aws.dynamodb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoneySumByAddress {
    private String PK;
    private int SK;
    private int balance;
}
