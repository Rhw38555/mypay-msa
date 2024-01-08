package com.mypay.common;

import com.mypay.common.subtask.SubTask;
import com.mypay.common.subtask.SubTaskStatus;
import com.mypay.common.subtask.SubTaskType;

import java.util.List;

public interface Task {

    String getTaskId();

    List<SubTask> getSubTaskList();

//    // 태스크의 유형을 식별하는 방법
//    default SubTaskType getTaskType() {
//        return SubTaskType.MEMBERSHIP;
//    }
//
//    // 태스크의 상태를 관리하는 방법
//    default SubTaskStatus getStatus() {
//        return SubTaskStatus.READY;
//    }

}
