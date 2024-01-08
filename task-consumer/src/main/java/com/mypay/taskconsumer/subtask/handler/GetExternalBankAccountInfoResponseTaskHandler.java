package com.mypay.taskconsumer.subtask.handler;

import com.mypay.common.subtask.SubTask;
import com.mypay.taskconsumer.application.port.out.BankingTaskPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetExternalBankAccountInfoResponseTaskHandler implements SubTaskHandler {

    private final BankingTaskPort bankingTaskPort;

    @Override
    public boolean handle(SubTask subTask) {
        return bankingTaskPort.getExternalBankAccountInfoTask();
    }

    @Override
    public String getSupportedSubTaskName() {
        return "getExternalBankAccountInfoTask";
    }
}
