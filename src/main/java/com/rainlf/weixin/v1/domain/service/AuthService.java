package com.rainlf.weixin.v1.domain.service;

/**
 * @author rain
 * @date 6/14/2024 11:23 AM
 */
public interface AuthService {

    /**
     * 登录接口
     * @param code wx mp login code
     * @return token
     */
    String login(String code);

    /**
     * mock 登录接口，仅测试使用，无需与微信服务器通信，直接在数据库中插入openId = mock-open-id 的用户，返回其token
     * @return token
     */
    String mockLogin();
}
