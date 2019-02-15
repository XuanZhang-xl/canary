package com.xl.canary.config.redis;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by xzhang on 2018/8/24.
 * springboot 在  RedisAutoConfiguration 中已自动配置, 自己不用配置了
 */
//@Configuration
public class RedisConfig {

    /**
     * Redis操作模板
     */
    //@Bean(value = "canaryRedis")
    public RedisTemplate<String, Object> gluttonRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        setTemplateParams(template);
        return template;
    }

    private void setTemplateParams(RedisTemplate<String, Object> template){
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(template.getKeySerializer());
        template.setHashValueSerializer(template.getValueSerializer());
    }
}
