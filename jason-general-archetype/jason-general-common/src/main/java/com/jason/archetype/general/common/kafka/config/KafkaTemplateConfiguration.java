package com.jason.archetype.general.common.kafka.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: sk-shanjiang
 * @date: 2019/10/30 17:45
 */
@Configuration
public class KafkaTemplateConfiguration {
    @Value("${spring.kafka.bootstrap-servers:127.0.0.1}")
    private String servers;

    @Value("${spring.kafka.producer.key-serializer:org.apache.kafka.common.serialization.BytesSerializer}")
    private  String keySerializer;

    @Value("${spring.kafka.producer.value-serializer:org.apache.kafka.common.serialization.BytesSerializer}")
    private String valueSerializer;

    @Bean
    public ProducerFactory producerFactory() {
        return new DefaultKafkaProducerFactory(templateConfigs());
    }

    protected Map<String,Object> templateConfigs(){
        Map<String,Object> propsMap = new HashMap<>(4);
        propsMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,servers);
        propsMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,keySerializer);
        propsMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,valueSerializer);
        return propsMap;
    }
}
