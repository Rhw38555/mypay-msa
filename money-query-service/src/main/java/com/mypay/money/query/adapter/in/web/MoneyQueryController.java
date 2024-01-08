package com.mypay.money.query.adapter.in.web;

import com.mypay.common.WebAdapter;
import com.mypay.money.query.application.port.in.QueryMoneySumByRegionQuery;
import com.mypay.money.query.application.port.in.QueryMoneySumByRegionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@WebAdapter
@RestController
@RequiredArgsConstructor
public class MoneyQueryController {

    private final QueryMoneySumByRegionUseCase useCase;
    @GetMapping(path = "/money/query/get-money-sum-by-address/{address}")
    long getMoneySumByAddress(@PathVariable String address) {
        QueryMoneySumByRegionQuery query = QueryMoneySumByRegionQuery.builder()
                .address(address)
                .build();

//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        stopWatch.stop();

        return useCase.queryMoneySumByRegion(query).getMoneySum();
    }
}
