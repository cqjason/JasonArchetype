package com.jason.archetype.general.common.hdfs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jasonCQ
 * @version 1.0
 * @date 2020/3/11 16:45
 */
@Configuration
public class HdfsConfig {
    @Value("${jason.image.hdfsUrl}")
    private String hdfsUri;

    @Bean
    public org.apache.hadoop.conf.Configuration setHdfsConfiguration() {
        org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
        //要以root方式登录，默认是本机用户名 sk-qianxiao， 权限不足
        System.setProperty("HADOOP_USER_NAME", "hdfs");


        conf.addResource("core-site.xml");
        conf.addResource("hdfs-site.xml");
//        conf.set("fs.hdfs.impl","org.apache.hadoop.hdfs.DistributedFileSystem");
        //使用主机名进行访问，要不然hdfs返回的就是内网ip
//        conf.set("dfs.client.use.datanode.hostname", "true");
//        conf.set("fs.defaultFS", hdfsUri);
//        //设置失败重试次数, 默认3次
//        conf.set("dfs.client.max.block.acquire.failures", "1");
        return conf;
    }
}
