package com.jason.archetype.general.common.kafka.producer;

import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;

/**
 * @author: sk-shanjiang
 * @date: 2019/11/5 16:22
 */
@Component
public class MyKafkaProducer extends DefaultKafkaProducer {
    public MyKafkaProducer(ProducerFactory producerFactory) {
        super(producerFactory);
    }
}
