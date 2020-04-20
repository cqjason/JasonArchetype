package com.jason.archetype.general.common.kafka.consumer;

import org.apache.kafka.common.utils.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: sk-shanjiang
 * @date: 2019/10/25 15:32
 */
public abstract class AbstractKafkaConsumer<T> {
    @Value("${com.skspruce.ism.mobile.sync.base.ThreadPoolExecutorConfig.threadFactory.name:sync-base-default-thread-pool}")
    private String threadFactoryName;

    @Value("${com.skspruce.ism.mobile.sync.base.ThreadPoolExecutorConfig.thread.num:4}")
    private int threadNum;

    @Value("${com.skspruce.ism.mobile.sync.base.ThreadPoolExecutorConfig.queue.capacity:4000}")
    private int queueCapacity;

    @Value("${com.skspruce.ism.mobile.sync.base.ThreadPoolExecutorConfig.keepAlive.seconds:60}")
    private int keepAliveTime;

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired()
    @Qualifier("defaultConsumerThreadPoolExecutor")
    private Executor executorService;

    /**
     * default ThreadPoolExecutor
     * @return
     */
    @Bean(name = "defaultConsumerThreadPoolExecutor")
    protected Executor createExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(threadNum);
        // 设置最大线程数
        executor.setMaxPoolSize(threadNum);
        // 设置队列容量
        executor.setQueueCapacity(queueCapacity);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(keepAliveTime);
        // 设置默认线程名称
        executor.setThreadNamePrefix(threadFactoryName);
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.initialize();
        return executor;
    }

    /**
     * KafkaListener's method call. It provide a entrance to KafkaListener
     * @param recordList
     * @param ack
     */
    protected abstract void consume(List<Bytes> recordList, Acknowledgment ack);

    /**
     * It is a template method of consumer
     * @param recordList
     * @param ack
     */
    protected final void extractRecord(List<Bytes> recordList, Acknowledgment ack){
        recordList.stream().forEach(record ->{
            String recordStr = null;
            try{
                T parserRecord = parse(record, getClassOfTemplate());
                executorService.execute(() -> process(parserRecord));
            } catch (RejectedExecutionException e){
                //Throw RejectedExecutionException when taskQueue is full because of executing AbortPolicy
                logger.warn("Submited recordStr submit failed: ", recordStr);
                logger.warn("ThreadPoolExecutor submit failed: ", e);
                //todo for executorService is busy
            } catch (Exception e){
                logger.warn("Kafka extractRecord failed: ", e);
            }
        });
        ack.acknowledge();
    }

    /**
     * process parserRecord
     * @param parserRecord
     * @return Runnable
     */
    protected abstract  void process(T parserRecord);

    /**
     * parse Bytes record  to T record.
     * @param bytes
     * @param classOfTemplate
     * @return
     */
    protected abstract T parse(Bytes bytes, Class<T> classOfTemplate);

    /**
     * get Class of T
     * @return Class<T>
     */
    protected abstract Class<T> getClassOfTemplate();
}
