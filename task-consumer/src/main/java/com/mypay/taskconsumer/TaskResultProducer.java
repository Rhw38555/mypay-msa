package com.mypay.taskconsumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class TaskResultProducer {
    private final KafkaProducer<String, String> producer;

    private final String topic;

    public TaskResultProducer(@Value("${kafka.clusters.bootstrapservers}") String bootstrapServers, @Value("${task.result.topic}")String topic) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", bootstrapServers);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        this.producer = new KafkaProducer<>(properties);
        this.topic = topic;
    }

    public void sendTaskResult(String key, Object task){
        ObjectMapper mapper = new ObjectMapper();
        // jsonString
        String jsonStringToProduce;
        try{
            jsonStringToProduce = mapper.writeValueAsString(task);
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
