package com.cdzg.xzshop;

import com.cdzg.xzshop.constant.RedisConst;
import com.cdzg.xzshop.filter.EnableBusinessLoginFilter;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAsync(proxyTargetClass = true)
@EnableBusinessLoginFilter
@EnableTransactionManagement
@ServletComponentScan(basePackages = {"com.cdzg.xzshop.filter"})
@MapperScan("com.cdzg.xzshop.mapper")
@EnableApolloConfig
@EnableScheduling
public class ServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }
    @EnableRedisHttpSession(redisNamespace = RedisConst.GLOBAL_PREFFIX+":spring:session", maxInactiveIntervalInSeconds = 86400)
    public class RedisSessionConfig {
        @Bean
        public CacheManager cacheManager(RedisConnectionFactory factory) {
            RedisCacheManager cacheManager = RedisCacheManager.create(factory);
            return cacheManager;
        }

            /**
             * RedisTemplate配置
             */
        @Bean
        public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {

            RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
            redisTemplate.setConnectionFactory(redisConnectionFactory);

            // 使用Jackson2JsonRedisSerialize 替换默认序列化
            Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

            jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

            StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

            // 设置value的序列化规则和 key的序列化规则
            redisTemplate.setKeySerializer(stringRedisSerializer);
            redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
            redisTemplate.setHashKeySerializer(stringRedisSerializer);
            redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

            redisTemplate.afterPropertiesSet();
            return redisTemplate;
        }
    }

}
