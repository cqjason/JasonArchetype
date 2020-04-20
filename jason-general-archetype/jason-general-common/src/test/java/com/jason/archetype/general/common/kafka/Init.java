package com.jason.archetype.general.common.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;
import org.testng.Assert;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * @author jasonCQ
 * @version 1.0
 * @date 2020/3/11 15:00
 */
@Configuration
public class Init {
    @Value("${spring.kafka.bootstrap-servers}")
    String bootstrapServer;

    @Value("${jason.kafka.test.data.topic}")
    String topicName;

    private AdminClient adminClientInit;

    @Bean(name = "adminClient")
    public AdminClient createAdminClient(){
        Map<String, Object> props = new HashMap<>();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        AdminClient adminClient = AdminClient.create(new KafkaAdmin(props).getConfig());
        NewTopic topic = new NewTopic(topicName, 1, (short) 1);
        adminClient.createTopics(Arrays.asList(topic));
        adminClientInit = adminClient;

        return adminClient;
    }
}
