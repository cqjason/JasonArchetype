package com.jason.archetype.general.common.kafka.consumer;

import com.jason.archetype.general.common.kafka.Init;
import com.jason.archetype.general.common.kafka.config.ConcurrentKafkaConsumerConfiguration;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.common.utils.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: sk-shanjiang
 * @date: 2019/10/29 15:15
 */
@Component
public class KafkaConsumer extends AbstractKafkaConsumer<String> {
    @Autowired
    private ConcurrentKafkaConsumerConfiguration concurrentKafkaConsumerConfiguration;

    @Autowired
    AdminClient adminClient;

    private volatile boolean process;

    public boolean isProcess() {
        return process;
    }

    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}",
            topics = "${jason.kafka.test.data.topic}",
            containerFactory = "kafkaListenerContainerFactory")
    @Override
    protected void consume(List<Bytes> recordList, Acknowledgment ack) {
        extractRecord(recordList, ack);
    }

    @Override
    protected void process(String parserRecord) {
        process = true;
    }

    @Override
    protected String parse(Bytes bytes, Class<String> classOfT) {
        return "parsed";
    }

    @Override
    protected Class<String> getClassOfTemplate() {
        return String.class;
    }
}
