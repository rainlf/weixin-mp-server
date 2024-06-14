package com.rainlf.weixin.infra.wexin.service.impl;

import com.google.gson.Gson;
import com.rainlf.weixin.infra.util.JsonUtils;
import com.rainlf.weixin.infra.wexin.model.WeixinSession;
import com.rainlf.weixin.infra.wexin.service.WeixinService;
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

    @Value("${weixin.appid}")
    private String appId;

    @Value("${weixin.secret}")
    private String secret;

    @Value("${weixin.api.code2Session}")
    private String code2SessionUrl;

    @Override
    public WeixinSession code2Session(String code) {
        log.info("code2Session, code: {}", code);
        StringBuilder sb = new StringBuilder();
        sb.append(code2SessionUrl)
                .append("?").append("appid").append("=").append(appId)
                .append("&").append("secret").append("=").append(secret)
                .append("&").append("js_code").append("=").append(code)
                .append("&").append("grant_type").append("=").append("authorization_code");

        String respStr = restTemplate.getForObject(sb.toString(), String.class);
        log.info("code2Session, resp: {}", respStr);
        WeixinSession resp = JsonUtils.fromJson(respStr, WeixinSession.class);

        if (resp == null) {
            throw new RuntimeException("code2Session error, resp is null");
        }

        if (!resp.valid()) {
            throw new RuntimeException("code2Session error, resp is invalid: " + resp);
        }

        return resp;
    }
}
