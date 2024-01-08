package com.mypay.money.query.application.port.out;

import java.util.Date;

public interface GetMoneySumByRegionPort {
    int getMoneySumByAddress(String address);
}
