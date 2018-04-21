package com.zhenhui.demo.toptop.follow.dal.cache;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.zhenhui.demo.toptop.follow.api.domain.FollowTarget;
import com.zhenhui.demo.toptop.follow.api.domain.Follower;
import com.zhenhui.demo.toptop.follow.api.domain.TargetType;
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
public class FollowerCache implements InitializingBean {

    @Autowired
    private JedisConnectionFactory connectionFactory;

    private RedisTemplate<FollowTarget, Long> redisTemplate;

    public int countFollowers(FollowTarget target) {
        Long count = redisTemplate.opsForZSet().count(target, 0, Double.MAX_VALUE);
        return count != null ? count.intValue() : 0;
    }

    public List<Follower> query(FollowTarget target, Date gmtActionOffset, boolean ascending, int limit) {

        Set<TypedTuple<Long>> tuples;
        if (ascending) {
            tuples = redisTemplate.opsForZSet().rangeByScoreWithScores(target
                , gmtActionOffset != null ? gmtActionOffset.getTime() : 0
                , System.currentTimeMillis()
                , 0
                , limit);
        } else {
            tuples = redisTemplate.opsForZSet().reverseRangeByScoreWithScores(target
                , 0
                , gmtActionOffset != null ? gmtActionOffset.getTime() : System.currentTimeMillis()
                , 0
                , limit);
        }

        return tuples.stream()
            .map(tuple -> new Follower(tuple.getValue(), tuple.getScore().longValue()))
            .collect(Collectors.toList());
    }

    public void save(FollowTarget target, long userId, Date gmtAction) {
        redisTemplate.opsForZSet().add(target, userId, gmtAction.getTime());
    }

    public void save(FollowTarget target, List<Long> userIds, Date gmtAction) {
        Set<TypedTuple<Long>> tuples = userIds.stream()
            .map(userId -> new DefaultTypedTuple<>(userId, (double)gmtAction.getTime()))
            .collect(toSet());

        redisTemplate.opsForZSet().add(target, tuples);
    }

    public void remove(FollowTarget target, long userId) {
        redisTemplate.opsForZSet().remove(target, userId);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setEnableTransactionSupport(true);

        final KeySerializer keySerializer = new KeySerializer();
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(new RedisSerializer<Long>() {
            @Override
            public byte[] serialize(Long aLong) throws SerializationException {
                try {
                    return String.valueOf(aLong).getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    throw new SerializationException("UnsupportedEncodingException");
                }
            }

            @Override
            public Long deserialize(byte[] bytes) throws SerializationException {
                try {
                    return Long.parseLong(new String(bytes, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    throw new SerializationException("UnsupportedEncodingException");
                }
            }
        });
        redisTemplate.setEnableDefaultSerializer(false);
        redisTemplate.afterPropertiesSet();
    }

    private class KeySerializer implements RedisSerializer<FollowTarget> {

        private final String prefix = "follower";

        @Override
        public byte[] serialize(FollowTarget target) throws SerializationException {
            try {
                return String.format("%s|%d|%d", prefix, target.getType().code, target.getTargetId()).getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new SerializationException("UnsupportedEncodingException");
            }
        }

        @Override
        public FollowTarget deserialize(byte[] bytes) throws SerializationException {
            try {
                String rawKey = new String(bytes, "UTF-8");
                String[] components = rawKey.split("\\|");
                TargetType targetType = TargetType.valueOf(Integer.parseInt(components[1]));
                Long targetId = Long.parseLong(components[2]);
                return new FollowTarget(targetType, targetId);
            } catch (UnsupportedEncodingException e) {
                throw new SerializationException("UnsupportedEncodingException");
            }
        }

    }
}




