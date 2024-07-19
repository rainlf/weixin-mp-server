package com.rainlf.weixin.v3.infa.cache;

/**
 * @author rain
 * @date 7/19/2024 2:35 PM
 */
public interface CacheService {

    String set(String key, String value);

    String get(String key);
}
