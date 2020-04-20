package com.jason.archetype.general.common.kafka.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * @author: sk-shanjiang
 * @date: 2019/10/30 18:00
 */
@SpringBootTest(classes = {KafkaTemplateConfiguration.class})
public class KafkaTemplateConfigurationTest extends AbstractTestNGSpringContextTests {
    @Autowired
    KafkaTemplateConfiguration kafkaTemplateConfiguration;

    @Test
    public void testProducerFactory() {
        ProducerFactory producerFactory = kafkaTemplateConfiguration.producerFactory();
        Assert.assertNotNull(producerFactory);
    }

    @Test
    public void testTemplateConfigs() {
        Map map = kafkaTemplateConfiguration.templateConfigs();
        Assert.assertEquals(map.size(),3);
    }
}