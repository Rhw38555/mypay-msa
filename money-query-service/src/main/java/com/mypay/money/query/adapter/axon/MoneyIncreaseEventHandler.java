package com.mypay.money.query.adapter.axon;

import com.mypay.common.event.RequestFirmbankingFinishedEvent;
import com.mypay.money.query.application.port.out.GetMemberAddressInfoPort;
import com.mypay.money.query.application.port.out.InsertMoneyIncreaseEventByAddress;
import com.mypay.money.query.application.port.out.MemberAddressInfo;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
public class MoneyIncreaseEventHandler {
    @EventHandler
    public void handler(RequestFirmbankingFinishedEvent event
            , GetMemberAddressInfoPort getMemberAddressInfoPort
            , InsertMoneyIncreaseEventByAddress insertMoneyIncreaseEventByAddress) {

        // 고객의 주소 정보
        MemberAddressInfo memberAddressInfo = getMemberAddressInfoPort.getMemberAddressInfo(event.getMembershipId());

        // Dynamodb Insert!
        String address = memberAddressInfo.getAddress();
        int moneyIncrease = event.getMoneyAmount();

        insertMoneyIncreaseEventByAddress.insertMoneyIncreaseEventByAddress(address, moneyIncrease);
    }
}
