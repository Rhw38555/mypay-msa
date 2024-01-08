package com.mypay.money.application.port.in;


import com.mypay.money.domain.MoneyChangingRequest;

public interface DecreaseMoneyChangingRequestUseCase {
    MoneyChangingRequest decreaseMoneyRequest(DecreaseMoneyChangingRequestCommand command);
}
