package com.mypay.common.subtask;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubTask {

    private String membershipId;

    private String subTaskName;

    private SubTaskType taskType; // banking, membership

    private SubTaskStatus status; // success, fail, ready
}
