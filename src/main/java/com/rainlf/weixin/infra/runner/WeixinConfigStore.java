package com.rainlf.weixin.infra.runner;

import com.rainlf.weixin.infra.db.entity.AppConfig;
import com.rainlf.weixin.infra.db.repository.AppConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author rain
 * @date 6/23/2024 8:20 PM
 */
@Component
public class WeixinConfigStore implements ApplicationRunner {
    @Autowired
    private AppConfigRepository appConfigRepository;

    private final Map<String, String> configMap = new HashMap<>();

    public String getValue(String key) {
        return configMap.get(key);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<AppConfig> appConfigs = appConfigRepository.findAll();
        for (AppConfig appConfig : appConfigs) {
            configMap.put(appConfig.getKey(), appConfig.getValue());
        }
    }
}
