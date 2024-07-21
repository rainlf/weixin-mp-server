package com.rainlf.weixin.v3.infa.auth;

import com.rainlf.weixin.v3.infa.db.entity.User;

/**
 * @author rain
 * @date 7/20/2024 5:34 AM
 */
public interface AuthService {

    void authToken(String token);

    User getUser();

    void cleanUser();

    String createToken(String openId);
}
