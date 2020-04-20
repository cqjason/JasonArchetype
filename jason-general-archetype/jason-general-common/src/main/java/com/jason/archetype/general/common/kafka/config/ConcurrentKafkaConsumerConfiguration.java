package com.jason.archetype.general.common.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: sk-shanjiang
 * @date: 2019/10/26 17:36
 */
@Configuration
@EnableKafka
public class ConcurrentKafkaConsumerConfiguration {
    @Value("${com.skspruce.ism.mobile.sync.base.ConcurrentKafkaConsumerConfiguration.concurrency:1}")
    private int concurrency;

    @Value("${spring.kafka.bootstrap-servers:127.0.0.1}")
    private String servers;

    @Value("${spring.kafka.consumer.auto-offset-reset:latest}")
    private String autoOffestRest;

    @Value("${spring.kafka.consumer.enable-auto-commit:false}")
    private boolean enableAutoCommit;

    @Value("${spring.kafka.consumer.group-id:test.group}")
    private String groupId;

    @Value("${spring.kafka.consumer.key-deserializer:org.apache.kafka.common.serialization.BytesDeserializer}")
    private  String keyDeserializer;

    @Value("${spring.kafka.consumer.value-deserializer:org.apache.kafka.common.serialization.BytesDeserializer}")
    private String valueDeserializer;

    @Value("${spring.kafka.consumer.max-poll-records:1000}")
    private int maxPollRecords;

    private Map<String,Object> propsMap;

    public int getConcurrency() {
        return concurrency;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(concurrency);
        factory.setBatchListener(true);
        factory.getContainerProperties()
                .setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    private ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    /**
     * config properties，protected access is provided for subclass modification
     * @return
     */
    protected Map<String,Object> consumerConfigs(){
        Map<String,Object> propsMap = new HashMap<>(8);
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,servers);
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG,groupId);
        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,autoOffestRest);
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,enableAutoCommit);
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,keyDeserializer);
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,valueDeserializer);
        propsMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,maxPollRecords);
        return propsMap;
    }
}
