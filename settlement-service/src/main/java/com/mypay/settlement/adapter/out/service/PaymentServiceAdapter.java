package com.mypay.settlement.adapter.out.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mypay.common.CommonHttpClient;
import com.mypay.settlement.port.out.PaymentPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentServiceAdapter implements PaymentPort {

    private final CommonHttpClient commonHttpClient;

    private final String paymentServiceUrl;

    public PaymentServiceAdapter(CommonHttpClient commonHttpClient,
                                 @Value("${service.payment.url}") String paymentServiceUrl) {
        this.commonHttpClient = commonHttpClient;
        this.paymentServiceUrl = paymentServiceUrl;
    }


    @Override
    public List<Payment> getNormalStatusPayments() {
        String url = String.join("/", paymentServiceUrl, "payment/normal-status");

        try {
            String jsonResponse = commonHttpClient.sendGetRequest(url).body();

            ObjectMapper mapper = new ObjectMapper();
            List<Payment> paymentList = mapper.readValue(jsonResponse, new TypeReference<>() {});

            return paymentList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void finishSettlement(Long paymentId) {
        String url = String.join("/", paymentServiceUrl, "payment/finish-settlement");
        ObjectMapper mapper = new ObjectMapper();

        try {
            FinishSettlementRequest request = new FinishSettlementRequest(String.valueOf(paymentId));
            commonHttpClient.sendPostRequest(url, mapper.writeValueAsString(request)).get().body();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
