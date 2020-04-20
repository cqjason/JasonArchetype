package com.jason.archetype.general.common.kafka.producer;

import com.jason.archetype.general.common.kafka.Init;
import com.jason.archetype.general.common.kafka.config.KafkaTemplateConfiguration;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.utils.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.util.concurrent.ListenableFuture;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * @author: sk-shanjiang
 * @date: 2019/11/5 14:41
 */
@SpringBootTest(classes = {MyKafkaProducer.class, KafkaTemplateConfiguration.class, Init.class})
public class DefaultKafkaProducerTest extends AbstractTestNGSpringContextTests {
    @Autowired
    MyKafkaProducer kafkaProducer;


    @Value("${jason.kafka.test.data.topic}")
    String topicName;

    @Autowired
    AdminClient adminClient;

    @Test
    public void testGetKafkaTemplate() {
        Assert.assertNotNull(kafkaProducer.getKafkaTemplate());
    }

    @Test
    public void testSend() throws InterruptedException, ExecutionException {
        ListenableFuture<SendResult<Bytes, Bytes>> future = kafkaProducer.send("mobile_test", Bytes.wrap("helloWorld".getBytes()));
        SendResult<Bytes, Bytes> result = future.get();
        Assert.assertNotNull(result);

        MyKafkaProducer kafkaProducer = new MyKafkaProducer(null);
        future = kafkaProducer.send(topicName, Bytes.wrap("helloWorld".getBytes()));
        Assert.assertNull(future);
    }

    @Test(dataProvider = "sendSyncData")
    public void testSendSync(MyKafkaProducer kafkaProducer, boolean expected) {
        boolean result = kafkaProducer.sendSync(topicName, Bytes.wrap("helloWorld".getBytes()));
        Assert.assertEquals(result, expected);
    }

    @DataProvider(name = "sendSyncData")
    public Object[][] sendSyncData() {
        return new Object[][]{{kafkaProducer, true}, {new MyKafkaProducer(null), false}};
    }

}