package com.mypay.money.application.port.in;


import com.mypay.money.domain.MoneyChangingRequest;

public interface IncreaseMoneyChangingRequestUseCase {
    void increaseMoneyRequestByEvent(IncreaseMoneyChangingRequestCommand command);
    MoneyChangingRequest increaseMoneyRequest(IncreaseMoneyChangingRequestCommand command);

    MoneyChangingRequest increaseMoneyRequestAsync(IncreaseMoneyChangingRequestCommand command);
}
