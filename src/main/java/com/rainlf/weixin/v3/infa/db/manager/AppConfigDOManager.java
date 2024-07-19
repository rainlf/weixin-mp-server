package com.rainlf.weixin.v3.infa.db.manager;

import com.rainlf.weixin.v3.infa.cache.CacheService;
import com.rainlf.weixin.v3.infa.db.entity.AppConfigDO;
import com.rainlf.weixin.v3.infa.db.repository.AppConfigDORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author rain
 * @date 7/19/2024 2:51 PM
 */
@Service
public class AppConfigDOManager {
    @Autowired
    private CacheService cacheService;
    @Autowired
    private AppConfigDORepository appConfigDORepository;

    public String getAppConfig(String key) {
        if (!cacheService.containsKey(key)) {
            AppConfigDO appConfigDO = appConfigDORepository.findByKey(key);
            if (appConfigDO != null && appConfigDO.getValue() != null) {
                cacheService.set(key, appConfigDO.getValue());
            }
        }
        return cacheService.get(key);
    }
}
