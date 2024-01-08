package com.mypay.taskconsumer.subtask.handler;

import com.mypay.common.subtask.SubTask;
import com.mypay.taskconsumer.application.port.out.MembershipTaskPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidMembershipStatusTaskHandler implements SubTaskHandler {

    private final MembershipTaskPort membershipTaskPort;

    @Override
    public boolean handle(SubTask subTask) {
        return membershipTaskPort.validMembershipStatusTask(subTask.getMembershipId());
    }

    @Override
    public String getSupportedSubTaskName() {
        return "validMembershipStatusTask";
    }
}
