package com.mypay.taskconsumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mypay.common.CountDownLatchManager;
import com.mypay.common.Task;
import com.mypay.common.subtask.SubTask;
import com.mypay.common.subtask.SubTaskStatus;
import com.mypay.taskconsumer.subtask.handler.SubTaskHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;

@Component
public class TaskConsumer {

    private ApplicationContext applicationContext; // 스프링 컨텍스트 주입

    private final KafkaConsumer<String, String> consumer;

    private final TaskResultProducer taskResultProducer;

//    private Map<String, List<SubTaskHandler>> subTaskHandlerMap = new HashMap<>();
    private Map<String, SubTaskHandler> subTaskHandlerMap = new HashMap<>();

    public TaskConsumer(@Value("${kafka.clusters.bootstrapservers}") String bootstrapServers,
                        @Value("${task.topic}")String topic, TaskResultProducer taskResultProducer,
                        ApplicationContext applicationContext, CountDownLatchManager countDownLatchManager) {


        this.taskResultProducer = taskResultProducer;
        this.applicationContext = applicationContext;

        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("group.id", "my-group");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        this.consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topic));



        Thread consumerThread = new Thread(() -> {
            try {
                initHandlers();
                while (true) {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
                    ObjectMapper mapper = new ObjectMapper();
                    for (ConsumerRecord<String, String> record : records) {

                        // record: RechargingMoneyTask (jsonstring)
//                        RechargingMoneyTask task;
                        Task task;
                        // task run
                        try {
//                            task = mapper.readValue(record.value(), RechargingMoneyTask.class);
                            Class<? extends Task> taskClass = getTaskClass(record.value());
                            task = mapper.readValue(record.value(), taskClass);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }

                        // task result
                        boolean taskResult = true;
                        for(SubTask subTask: task.getSubTaskList()){
                            // 각 서브태스크 이름별 처리기 목록에서 핸들러 찾기
                            SubTaskHandler handler = subTaskHandlerMap.get(subTask.getSubTaskName());

                            boolean subTaskResult = handler.handle(subTask);

                            if(subTaskResult){
                                // subtask, membership, banking
                                // external port, adapter 실제 호출 과정 필요
                                subTask.setStatus(SubTaskStatus.SUCCESS);
                            }
                        }

                        // produce TaskResult
                        this.taskResultProducer.sendTaskResult(task.getTaskId(), task);

//                        if(taskResult){
//                            this.countDownLatchManager.setDatForKey(task.getTaskId(),"success");
//                        }else{
//                            this.countDownLatchManager.setDatForKey(task.getTaskId(),"failed");
//                        }
//                        this.countDownLatchManager.getCountDownLatch(task.getTaskId()).countDown();

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                consumer.close();
            }
        });
        consumerThread.start();
    }

    // 사용하는 모든 핸들러 초기화
    private void initHandlers() {
        Map<String, SubTaskHandler> handlers = applicationContext.getBeansOfType(SubTaskHandler.class);
        for (SubTaskHandler handler : handlers.values()) {
            String subTaskName = handler.getSupportedSubTaskName(); // 핸들러에서 직접 지원하는 서브태스크 이름 가져오기
            subTaskHandlerMap.put(subTaskName, handler);
        }
    }

    // Task 타입 변환
    private Class<? extends Task> getTaskClass(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> taskMap = mapper.readValue(jsonString, Map.class);
            String type = (String) taskMap.get("taskName");
            Class<?> taskClass = Class.forName("com.mypay.common." + type);
            return taskClass.asSubclass(Task.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
