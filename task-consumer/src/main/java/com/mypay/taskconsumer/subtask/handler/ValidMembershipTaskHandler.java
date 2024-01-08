package com.mypay.taskconsumer.subtask.handler;

import com.mypay.taskconsumer.application.port.out.MembershipTaskPort;
import com.mypay.common.subtask.SubTask;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidMembershipTaskHandler implements SubTaskHandler {

    private final MembershipTaskPort membershipTaskPort;

    @Override
    public boolean handle(SubTask subTask) {
        return membershipTaskPort.validMembershipTask(subTask.getMembershipId());
    }

    @Override
    public String getSupportedSubTaskName() {
        return "validMembershipTask";
    }
}
