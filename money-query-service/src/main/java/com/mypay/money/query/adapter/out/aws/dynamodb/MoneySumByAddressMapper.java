package com.mypay.money.query.adapter.out.aws.dynamodb;


import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

public class MoneySumByAddressMapper {
    public MoneySumByAddress mapToMoneySumByAddress(
          Map<String, AttributeValue> item
    ){
        return new MoneySumByAddress(
                item.get("PK").s(),
                Integer.parseInt(item.get("SK").n()),
                Integer.parseInt(item.get("balance").n())
        );
    }
}
