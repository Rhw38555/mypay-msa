package com.mypay.common;

import com.mypay.common.subtask.SubTask;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RechargingMoneyTask implements Task {

    private String taskId;
    private String taskName;
    private String membershipId;
    private List<SubTask> subTaskList;
    // 법인 계좌
    private String toBankName;
    // 법인 계좌 번호
    private String toBankAccountNumber;
    private int moneyAmount; // won

    @Override
    public String getTaskId() {
        return taskId;
    }

    @Override
    public List<SubTask> getSubTaskList() {
        return subTaskList;
    }

}
