package com.rainlf.weixin.v3.infa.wexin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * @author rain
 * @date 5/30/2024 10:57 AM
 */
@Data
public class WeixinSession {
    @JsonProperty("openid")
    private String openId;
    @JsonProperty("unionid")
    private String unionId;
    @JsonProperty("session_key")
    private String sessionKey;
    @JsonProperty("errcode")
    private Integer errcode;
    @JsonProperty("errmsg")
    private String errmsg;

    public boolean valid() {
        return StringUtils.hasText(openId)
                && StringUtils.hasText(sessionKey);
    }
}
