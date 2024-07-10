package com.rainlf.weixin.infra.runner;

import com.rainlf.weixin.infra.db.entity.AppConfig;
import com.rainlf.weixin.infra.db.repository.AppConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author rain
 * @date 6/23/2024 8:20 PM
 */
@Slf4j
@Service
public class WeixinConfigStore {
    @Autowired
    private AppConfigRepository appConfigRepository;

    private final Map<String, String> configMap = new HashMap<>();

    public String getValue(String key) {
        if (configMap.isEmpty()) {
            List<AppConfig> appConfigs = appConfigRepository.findAll();
            log.info("WeixinConfigStore, load config, appConfigs size: {}", appConfigs.size());
            for (AppConfig appConfig : appConfigs) {
                log.debug("WeixinConfigStore, key: {}, value: {}", appConfig.getKey(), appConfig.getValue());
                configMap.put(appConfig.getKey(), appConfig.getValue());
            }
        }
        return configMap.get(key);
    }
}
