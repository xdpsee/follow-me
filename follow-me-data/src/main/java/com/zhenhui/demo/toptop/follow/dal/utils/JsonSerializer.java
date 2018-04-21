package com.zhenhui.demo.toptop.follow.dal.utils;

import java.io.UnsupportedEncodingException;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class JsonSerializer<T> implements RedisSerializer<T> {

    private Class<T> clz = null;
    private TypeReference<T> ref = null;

    public JsonSerializer(Class<T> valueClz) {
        this.clz = valueClz;
    }

    public JsonSerializer(TypeReference<T> valueTypeRef) {
        this.ref = valueTypeRef;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        String json = JSONUtils.toJsonString(t);
        try {
            return json.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new SerializationException("UnsupportedEncodingException:UTF-8");
        }
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        try {
            String v = new String(bytes, "UTF-8");
            if (ref != null) {
                return JSONUtils.fromJsonString(v, ref);
            } else {
                return JSONUtils.fromJsonString(v, clz);
            }
        } catch (UnsupportedEncodingException e) {
            throw new SerializationException("UnsupportedEncodingException:UTF-8");
        }
    }

}
