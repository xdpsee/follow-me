package com.zhenhui.demo.toptop.follow.dal.cache;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zhenhui.demo.toptop.follow.api.domain.FollowTarget;
import com.zhenhui.demo.toptop.follow.api.domain.Following;
import com.zhenhui.demo.toptop.follow.dal.utils.JsonSerializer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.toSet;

@SuppressWarnings("unused")
@Component
@Configuration
public class FollowingCache implements InitializingBean {

    @Autowired
    private JedisConnectionFactory connectionFactory;

    private RedisTemplate<Long/*userId*/, FollowTarget/*follow target*/> redisTemplate;

    public int countFollowings(long userId) {
        Long count = redisTemplate.opsForZSet().count(userId, 0, Double.MAX_VALUE);
        return count != null ? count.intValue() : 0;
    }

    public List<Following> query(long userId, Date gmtActionOffset, boolean ascending, int limit) {

        Set<TypedTuple<FollowTarget>> tuples;

        if (ascending) {
            tuples = redisTemplate.opsForZSet().rangeByScoreWithScores(userId
                , gmtActionOffset != null ? gmtActionOffset.getTime() : 0
                , System.currentTimeMillis()
                , 0
                , limit);
        } else {
            tuples = redisTemplate.opsForZSet().reverseRangeByScoreWithScores(userId
                , 0
                , gmtActionOffset != null ? gmtActionOffset.getTime() : System.currentTimeMillis()
                , 0
                , limit);
        }

        return tuples.stream()
            .map(tuple -> new Following(tuple.getValue(), tuple.getScore().longValue()))
            .collect(Collectors.toList());
    }

    public void save(long userId, FollowTarget target, Date gmtAction) {
        redisTemplate.opsForZSet().add(userId, target, gmtAction.getTime());
    }

    public void save(long userId, List<FollowTarget> targets, Date gmtAction) {

        Set<TypedTuple<FollowTarget>> tuples = targets.stream()
            .map(target -> new DefaultTypedTuple<>(target, (double)gmtAction.getTime()))
            .collect(toSet());

        redisTemplate.opsForZSet().add(userId, tuples);
    }

    public void remove(long userId, FollowTarget target) {
        redisTemplate.opsForZSet().remove(userId, target);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setEnableTransactionSupport(true);

        final KeySerializer keySerializer = new KeySerializer();
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(new JsonSerializer<>(new TypeReference<FollowTarget>() {}));
        redisTemplate.setEnableDefaultSerializer(false);
        redisTemplate.afterPropertiesSet();
    }

    private class KeySerializer implements RedisSerializer<Long> {

        private final String prefix = "following";

        @Override
        public byte[] serialize(Long aLong) throws SerializationException {
            try {
                return String.format("%s|%d", prefix, aLong).getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new SerializationException("UnsupportedEncodingException");
            }
        }

        @Override
        public Long deserialize(byte[] bytes) throws SerializationException {

            try {
                String rawKey = new String(bytes, "UTF-8");
                String[] components = rawKey.split("\\|");
                return Long.parseLong(components[1]);
            } catch (UnsupportedEncodingException e) {
                throw new SerializationException("UnsupportedEncodingException");
            }
        }

    }

}







