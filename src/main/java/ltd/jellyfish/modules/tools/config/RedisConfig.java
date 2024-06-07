package ltd.jellyfish.modules.tools.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

import ltd.jellyfish.modules.tools.models.TokenOutMessage;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, TokenOutMessage> getTokenOutMessageTemp(RedisConnectionFactory factory){
        RedisTemplate<String, TokenOutMessage> outTemplate = new RedisTemplate<>();
        outTemplate.setConnectionFactory(factory);
         // 设置objectMapper:转换java对象的时候使用
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping( LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL,  JsonTypeInfo.As.WRAPPER_ARRAY);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // 设置key/value值的序列化方式
        outTemplate.setKeySerializer(stringRedisSerializer);
        outTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        outTemplate.setHashKeySerializer(stringRedisSerializer);
        outTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        outTemplate.afterPropertiesSet();

        return outTemplate;
  }

}
