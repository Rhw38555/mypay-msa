package com.mypay.money.query.application.port.in;

import com.mypay.money.query.domain.MoneySumByRegion;

public interface QueryMoneySumByRegionUseCase {

    MoneySumByRegion queryMoneySumByRegion (QueryMoneySumByRegionQuery query);
}
