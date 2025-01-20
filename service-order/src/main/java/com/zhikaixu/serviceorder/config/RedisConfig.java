package com.zhikaixu.serviceorder.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RedisConfig {

    private String protocol = "redis://";

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private String redisPort;

    @Bean("redissonBootYml")
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress(protocol + redisHost + ":" + redisPort).setDatabase(0);

        return Redisson.create(config);
    }

    @Bean("redissonYamlClient")
    public RedissonClient redissonYamlClient() {
        Config config = null;
        try {
            config = Config.fromYAML(new ClassPathResource("/redisson-config/single-server.yaml").getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Redisson.create(config);
    }

    @Bean("redissonMasterSlaveClient")
    public RedissonClient redissonMasterSlaveClient() {
        Config config = null;
        try {
            config = Config.fromYAML(new ClassPathResource("/redisson-config/master-slave-server.yaml").getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
//        config.useMasterSlaveServers();
        return Redisson.create(config);
    }

}
