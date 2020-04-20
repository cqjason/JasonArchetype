package com.jason.archetype.general.common.kafka.consumer;

import com.jason.archetype.general.common.kafka.Init;
import com.jason.archetype.general.common.kafka.config.ConcurrentKafkaConsumerConfiguration;
import com.jason.archetype.general.common.kafka.config.KafkaTemplateConfiguration;
import com.jason.archetype.general.common.kafka.producer.DefaultKafkaProducer;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.utils.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * @author: sk-shanjiang
 * @date: 2019/11/5 15:05
 */
@SpringBootTest(classes = {KafkaConsumer.class, ConcurrentKafkaConsumerConfiguration.class,
        DefaultKafkaProducer.class, KafkaTemplateConfiguration.class, Init.class})
public class AbstractKafkaConsumerTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private KafkaConsumer kafkaConsumer;

    @Autowired
    private DefaultKafkaProducer kafkaProducer;

    @Value("${spring.kafka.bootstrap-servers}")
    String bootstrapServer;

    @Value("${jason.kafka.test.data.topic}")
    String topicName;

    @Test
    public void testConsume() throws InterruptedException {
        kafkaProducer.sendSync(topicName, Bytes.wrap("helloWorld".getBytes()));
        Thread.sleep(5000);
        Assert.assertTrue(kafkaConsumer.isProcess());
    }
}