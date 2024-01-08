package com.mypay.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class TaskProducer {

    private final KafkaProducer<String, String> producer;

    private final String topic;

    public TaskProducer(@Value("${kafka.clusters.bootstrapservers}") String bootstrapServers, @Value("${task.topic}")String topic) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", bootstrapServers);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        this.producer = new KafkaProducer<>(properties);
        this.topic = topic;
    }

    public void sendTask(Task task) {
        this.sendMessage(task.getTaskId(), task);
    }

    //    public void sendMessage(String key, RechargingMoneyTask value){
    public void sendMessage(String key, Task value){
        ObjectMapper mapper = new ObjectMapper();
        // jsonString
        String jsonStringToProduce;
        try{
            jsonStringToProduce = mapper.writeValueAsString(value);
        }catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }

        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, jsonStringToProduce);
        producer.send(record, (metadata, exception) -> {
            if(exception == null) {
            }else{
                exception.printStackTrace();
            }
        });
    }

}
