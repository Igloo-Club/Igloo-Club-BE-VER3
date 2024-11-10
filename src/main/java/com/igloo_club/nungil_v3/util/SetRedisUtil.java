package com.igloo_club.nungil_v3.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class SetRedisUtil {
    private final StringRedisTemplate template;

    public Set<String> getAll(String key) {
        return template.opsForSet().members(key);
    }

    public boolean exists(String key) {
        return Boolean.TRUE.equals(template.hasKey(key));
    }

    public void add(String key, String value, Duration timeout) {
        template.opsForSet().add(key, value);
        template.expire(key, timeout);
    }

    public void delete(String key) {
        template.delete(key);
    }

    public void remove(String key, String value) {
        template.opsForSet().remove(key, value);
    }
}
