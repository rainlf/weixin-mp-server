package com.rainlf.weixin.v3.infa.wexin;

import com.rainlf.weixin.v1.infra.util.JsonUtils;
import com.rainlf.weixin.v3.infa.db.manager.AppConfigDOManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
    private AppConfigDOManager appConfigDOManager;

    @Value("${weixin.appid.key}")
    private String weixinAppIdKey;
    @Value("${weixin.secret.key}")
    private String weixinSecretKey;
    @Value("${weixin.api.code2Session}")
    private String code2SessionUrl;

    @Override
    public WeixinSession code2Session(String code) {
        log.info("code2Session, code: {}", code);
        String appId = appConfigDOManager.getAppConfig(weixinAppIdKey);
        String appSecret = appConfigDOManager.getAppConfig(weixinSecretKey);
        String url = String.format("%s?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                code2SessionUrl, appId, appSecret, code);

        log.debug("weixin be login url: {}", url);
        String respStr = restTemplate.getForObject(url, String.class);
        log.info("code2Session, resp: {}", respStr);
        WeixinSession resp = JsonUtils.toObject(respStr, WeixinSession.class);

        if (resp == null) {
            throw new RuntimeException("code2Session error, resp is null");
        }
        if (!resp.valid()) {
            throw new RuntimeException("code2Session error, resp is invalid: " + resp);
        }

        return resp;
    }
}
