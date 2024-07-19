package com.rainlf.weixin.v3.infa.wexin;

import com.rainlf.weixin.v1.infra.runner.WeixinConfigStore;
import com.rainlf.weixin.v1.infra.util.JsonUtils;
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

    @Value("${weixin.appid.key}")
    private String weixinAppIdKey;

    @Value("${weixin.secret.key}")
    private String weixinSecretKey;

    @Value("${weixin.api.code2Session}")
    private String code2SessionUrl;

    @Override
    public WeixinSession code2Session(String code) {
        log.info("code2Session, code: {}", code);
        StringBuilder sb = new StringBuilder();
        sb.append(code2SessionUrl)
                .append("?appid=").append(weixinConfigStore.getValue(weixinAppIdKey))
                .append("&secret=").append(weixinConfigStore.getValue(weixinSecretKey))
                .append("&js_code=").append(code)
                .append("&grant_type=authorization_code");

        log.debug("weixin be login url: {}", sb);
        String respStr = restTemplate.getForObject(sb.toString(), String.class);
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
