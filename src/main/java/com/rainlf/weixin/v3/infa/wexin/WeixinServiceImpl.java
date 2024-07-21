package com.rainlf.weixin.v3.infa.wexin;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
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

    @Value("${weixin.api.code2Session}")
    private String code2SessionUrl;

    @Override
    public WeixinSession code2Session(String appId, String appSecret, String code) {
        log.info("code2Session, code: {}", code);
        String url = String.format("%s?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code", code2SessionUrl, appId, appSecret, code);

        log.debug("weixin be login url: {}", url);
        String respStr = restTemplate.getForObject(url, String.class);
        log.info("code2Session, resp: {}", respStr);
        WeixinSession resp = JSON.parseObject(respStr, WeixinSession.class);

        Assert.notNull(resp, "code2Session error, resp is null");
        Assert.isTrue(resp.valid(), "code2Session error, resp is invalid: " + respStr);
        return resp;
    }
}
