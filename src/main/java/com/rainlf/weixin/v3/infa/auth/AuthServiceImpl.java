package com.rainlf.weixin.v3.infa.auth;

import com.rainlf.weixin.v3.infa.db.entity.User;
import com.rainlf.weixin.v3.infa.db.repository.UserRepository;
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
    private UserRepository userRepository;

    // 线程登录态，线程隔离
    private final ThreadLocal<User> userDOThreadLocal = new ThreadLocal<>();

    @Override
    public void authToken(String token) {
        log.info("auth token: {}", token);
        Claims claims = JJwtUtils.claims(token);
        String openId = claims.getSubject();
        Assert.notNull(openId, "invalid jwt token, openId is null");

        Optional<User> userOptional = userRepository.findByOpenId(openId);
        Assert.isTrue(userOptional.isPresent(), "invalid jwt token, user not found, openId: " + openId);
        userDOThreadLocal.set(userOptional.get());
    }

    @Override
    public User getUser() {
        User user = userDOThreadLocal.get();
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
