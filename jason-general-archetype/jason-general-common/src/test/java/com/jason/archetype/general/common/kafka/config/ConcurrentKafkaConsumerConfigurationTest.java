package com.jason.archetype.general.common.kafka.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * @author: sk-shanjiang
 * @date: 2019/10/26 17:47
 */
@SpringBootTest(classes = {ConcurrentKafkaConsumerConfiguration.class})
public class ConcurrentKafkaConsumerConfigurationTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private ConcurrentKafkaConsumerConfiguration concurrentKafkaConsumerConfiguration;

    @Test
    public void testKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory factory = concurrentKafkaConsumerConfiguration.kafkaListenerContainerFactory();
        Assert.assertNotNull(factory);
        Assert.assertEquals(concurrentKafkaConsumerConfiguration.getConcurrency(),1);
        Assert.assertEquals(factory.getConsumerFactory().getConfigurationProperties().size(),7);
    }

    @Test
    public void testconsumerConfigs() {
        Map map =  concurrentKafkaConsumerConfiguration.consumerConfigs();
        Assert.assertEquals(map.size(),7);
    }
}
