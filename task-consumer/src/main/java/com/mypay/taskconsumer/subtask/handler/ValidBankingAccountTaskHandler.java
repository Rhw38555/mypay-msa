package com.mypay.taskconsumer.subtask.handler;

import com.mypay.common.subtask.SubTask;
import org.springframework.stereotype.Component;

@Component
public class ValidBankingAccountTaskHandler implements SubTaskHandler {

    @Override
    public boolean handle(SubTask subTask) {
        // ...
        return true;
    }

    @Override
    public String getSupportedSubTaskName() {
        return "validBankingAccountTask";
    }
}
