package com.rainlf.weixin.v3.infa.auth;

import com.rainlf.weixin.v3.infa.db.entity.UserDO;
import com.rainlf.weixin.v3.infa.db.repository.UserDORepository;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * @author rain
 * @date 7/20/2024 5:45 AM
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserDORepository userDORepository;

    // 线程登录态，线程隔离
    private final ThreadLocal<UserDO> userDOThreadLocal = new ThreadLocal<>();

    @Override
    public void authToken(String token) {
        log.info("auth token: {}", token);
        Claims claims = JJwtUtils.claims(token);
        String openId = claims.getSubject();
        Assert.notNull(openId, "invalid jwt token, openId is null");

        Optional<UserDO> userDO = userDORepository.findByOpenId(openId);
        Assert.isTrue(userDO.isPresent(), "invalid jwt token, user not found, openId: " + openId);
        userDOThreadLocal.set(userDO.get());
    }

    @Override
    public UserDO getUser() {
        UserDO user = userDOThreadLocal.get();
        Assert.notNull(user, "user not login");
        return user;
    }

    @Override
    public void cleanUser() {
        userDOThreadLocal.remove();
    }

    @Override
    public String createToken(String openId) {
        return JJwtUtils.createToken(openId);
    }
}