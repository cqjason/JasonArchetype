package com.jason.archetype.general.common.kafka.producer;

import org.apache.kafka.common.utils.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

/**
 * @author: sk-shanjiang
 * @date: 2019/11/5 14:40
 */
public class DefaultKafkaProducer extends AbstractKafkaProducer {
    @Autowired
    public DefaultKafkaProducer(ProducerFactory producerFactory) {
        super(producerFactory);
    }

    @Override
    public KafkaTemplate<Bytes, Bytes> createKafkaTemplate(ProducerFactory producerFactory) {
        if (producerFactory == null) {
            return null;
        }
        return new KafkaTemplate<>(producerFactory);
    }

    public KafkaTemplate getKafkaTemplate() {
        return kafkaTemplate;
    }
}
