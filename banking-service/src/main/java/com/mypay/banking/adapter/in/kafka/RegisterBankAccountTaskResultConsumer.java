package com.mypay.banking.adapter.in.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mypay.common.CountDownLatchManager;
import com.mypay.common.LoggingProducer;
import com.mypay.common.Task;
import com.mypay.common.subtask.SubTask;
import com.mypay.common.subtask.SubTaskStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

@Component
@Slf4j
public class RegisterBankAccountTaskResultConsumer {

    private final KafkaConsumer<String, String> consumer;

    private final LoggingProducer loggingProducer;

    private final CountDownLatchManager countDownLatchManager;

//    public RechargingMoneyResultConsumer(@Value("${kafka.clusters.bootstrapservers}") String bootstrapServers,
    public RegisterBankAccountTaskResultConsumer(@Value("${kafka.clusters.bootstrapservers}") String bootstrapServers,
                              @Value("${task.result.topic}")String topic, LoggingProducer loggingProducer, CountDownLatchManager countDownLatchManager) {


        this.loggingProducer = loggingProducer;
        this.countDownLatchManager = countDownLatchManager;


        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("group.id", "my-group");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        this.consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topic));

        Thread consumerThread = new Thread(() -> {
            try {
                while (true) {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
                    ObjectMapper mapper = new ObjectMapper();
                    for (ConsumerRecord<String, String> record : records) {

                        // record: RechargingMoneyTask (jsonstring)
                        Task task;
                        // task run
                        try {
//                            task = mapper.readValue(record.value(), RechargingMoneyTask.class);
                            Class<? extends Task> taskClass = getTaskClass(record.value());
                            task = mapper.readValue(record.value(), taskClass);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }

                        boolean taskResult = true;
                        // task result
                        for(SubTask subTask: task.getSubTaskList()){
                            // 한번만 실패해도 실패한 task 간주
                            if(subTask.getStatus().equals(SubTaskStatus.FAIL)){
                                taskResult = false;
                                break;
                            }
                        }

                        if(taskResult){
                            this.loggingProducer.sendMessage(task.getTaskId(), "task success");
                            this.countDownLatchManager.setDatForKey(task.getTaskId(),"success");
                        }else{
                            this.loggingProducer.sendMessage(task.getTaskId(), "task failed");
                            this.countDownLatchManager.setDatForKey(task.getTaskId(),"failed");
                        }

                        this.countDownLatchManager.getCountDownLatch(task.getTaskId()).countDown();
                    }
                }
            } finally {
                consumer.close();
            }
        });
        consumerThread.start();
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
