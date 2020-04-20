package com.jason.archetype.general.common.kafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author: sk-shanjiang
 * @date: 2019/10/30 14:04
 */
public abstract class AbstractKafkaProducer<K, V> {
    @Value("${sks.kafka.producer.timeout:1000}")
    private int waitMilliseconds;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected KafkaTemplate<K, V> kafkaTemplate;

    public AbstractKafkaProducer(ProducerFactory producerFactory) {
        kafkaTemplate = createKafkaTemplate(producerFactory);
    }
    /**
     * create KafkaTemplate by producerFactory
     * @param producerFactory
     * @return KafkaTemplate
     */
    protected abstract KafkaTemplate<K, V> createKafkaTemplate(ProducerFactory producerFactory);

    public ListenableFuture<SendResult<K,V>> send(String topic, V value){
        if (kafkaTemplate == null) {
            logger.error("Fail to send message,because the kafkaTemplate is null!");
            return null;
        }
        ListenableFuture<SendResult<K,V>> future = kafkaTemplate.send(topic,value);
        future.addCallback(new ListenableFutureCallback<SendResult<K,V>>() {
            @Override
            public void onFailure(Throwable throwable) {
                logger.error("topic:{" + topic + "}, msg:{" + value.toString() + "} Fail to send message:", throwable);
            }

            @Override
            public void onSuccess(SendResult<K,V> bytesBytesSendResult) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Send data successfully. topic:{}, partitions:{}, offset:{}, msg:{}",
                            topic, bytesBytesSendResult.getRecordMetadata().partition(), bytesBytesSendResult.getRecordMetadata().offset(), value.toString());
                }
            }
        });
        return future;
    }

    public boolean sendSync(String topic,V value){
        if (kafkaTemplate == null) {
            logger.error("Fail to send message,because the kafkaTemplate is null!");
            return false;
        }
        try {
            kafkaTemplate.send(topic,value).get(waitMilliseconds,TimeUnit.MILLISECONDS);
            logger.debug("send kafka successful! topic:{" + topic + "}, msg:{" + value.toString() + "}");
            return true;
        } catch (InterruptedException | ExecutionException | TimeoutException | NullPointerException e) {
            logger.error("topic:{" + topic + "}, msg:{" + value.toString() + "} Fail to send message:", e);
            return false;
        }
    }

}
