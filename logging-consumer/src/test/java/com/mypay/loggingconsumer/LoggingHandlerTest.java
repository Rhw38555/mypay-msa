package com.mypay.loggingconsumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class LoggingHandlerTest {

    @InjectMocks
    private LoggingHandler loggingHandler;

    @Mock
    private KafkaConsumer<String, String> loggingConsumer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        loggingHandler = new LoggingHandler(loggingConsumer);
    }

    private static Stream<String> provideStringForConsumer() {
        return Stream.of(
                new String("[log] hello world"),
                new String("[logging] hello world")
        );
    }

    @ParameterizedTest
    @MethodSource("provideStringForConsumer")
    public void testConsumerHandler(String consumedString) {

        when(loggingConsumer.poll(any(Duration.class)))
                .thenReturn(createMockRecordsFromString(consumedString));

        loggingHandler.consume();

        // 최소 한번이상
        verify(loggingConsumer, atLeastOnce()).poll(any(Duration.class));
    }

    private static ConsumerRecords<String, String> createMockRecordsFromString(String consumedString) {
        List list = new ArrayList<>();
        ConsumerRecord<String, String> dummyRecord
                = new ConsumerRecord<>("test", 0, 0, "key", consumedString);

        list.add(dummyRecord);

        Map<TopicPartition, List<ConsumerRecord<String,String>>> map
                = new HashMap<>();
        map.put(new TopicPartition("test", 0), list);
        return new ConsumerRecords<>(map);
    }

}