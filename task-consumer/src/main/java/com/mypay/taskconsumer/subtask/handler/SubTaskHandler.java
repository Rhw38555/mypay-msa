package com.mypay.taskconsumer.subtask.handler;

import com.mypay.common.subtask.SubTask;

// 서브태스크 핸들러 인터페이스
public interface SubTaskHandler {
    boolean handle(SubTask subTask);
    String getSupportedSubTaskName();
}