package com.rainlf.weixin.v3.infa.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author rain
 * @date 7/19/2024 2:37 PM
 */
@Slf4j
@Service
public class CacheServiceImpl implements CacheService {
    private final Map<String, String> cache = new ConcurrentHashMap<>();

    @Override
    public String set(String key, String value) {
        Assert.notNull(key, "cache service key must not be null");
        Assert.notNull(value, "cache service value must not be null");
        return cache.put(key, value);
    }

    @Override
    public String get(String key) {
        return cache.get(key);
    }
}
