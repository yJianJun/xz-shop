package com.cdzg.xzshop.config;

import com.cdzg.xzshop.componet.SnowflakeIdWorker;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SnowflakeIdWorkerConfig {

    @Bean
    @ConditionalOnMissingBean
    SnowflakeIdWorker snowflakeIdWorker(){
        return new SnowflakeIdWorker(0,0);
    }
}
