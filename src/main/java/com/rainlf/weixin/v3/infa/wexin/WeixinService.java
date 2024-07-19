package com.rainlf.weixin.v3.infa.wexin;

/**
 * @author rain
 * @date 5/30/2024 10:56 AM
 */
public interface WeixinService {

    WeixinSession code2Session(String code);

}
