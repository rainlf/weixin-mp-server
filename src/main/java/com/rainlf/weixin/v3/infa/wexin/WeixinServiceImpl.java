package com.rainlf.weixin.v3.infa.wexin;

import com.rainlf.weixin.v1.infra.util.JsonUtils;
import com.rainlf.weixin.v3.infa.db.entity.AppConfigDO;
import com.rainlf.weixin.v3.infa.db.repository.AppConfigDORepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author rain
 * @date 5/30/2024 11:53 AM
 */
@Slf4j
@Service
public class WeixinServiceImpl implements WeixinService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AppConfigDORepository appConfigDORepository;

    @Value("${weixin.appid.key}")
    private String weixinAppIdKey;
    @Value("${weixin.secret.key}")
    private String weixinSecretKey;
    @Value("${weixin.api.code2Session}")
    private String code2SessionUrl;

    // app config cache map
    private final Map<String, String> appConfigCache = new ConcurrentHashMap<>();

    @Override
    public WeixinSession code2Session(String code) {
        log.info("code2Session, code: {}", code);
        String appId = getAppConfig(weixinAppIdKey);
        String appSecret = getAppConfig(weixinSecretKey);
        String url = String.format("%s?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code", code2SessionUrl, appId, appSecret, code);

        log.debug("weixin be login url: {}", url);
        String respStr = restTemplate.getForObject(url, String.class);
        log.info("code2Session, resp: {}", respStr);
        WeixinSession resp = JsonUtils.toObject(respStr, WeixinSession.class);

        Assert.notNull(resp, "code2Session error, resp is null");
        Assert.isTrue(resp.valid(), "code2Session error, resp is invalid: " + respStr);
        return resp;
    }

    private String getAppConfig(String key) {
        Assert.hasText(key, "getAppConfig key can't be empty");
        if (!appConfigCache.containsKey(key)) {
            Optional<AppConfigDO> appConfigDOOptional = appConfigDORepository.findByKey(key);
            if (appConfigDOOptional.isPresent()) {
                AppConfigDO appConfigDO = appConfigDOOptional.get();
                Assert.hasText(appConfigDO.getValue(), "getAppConfig value can't be empty");
                appConfigCache.put(key, appConfigDO.getValue());
            }
        }
        return appConfigCache.get(key);
    }
}
