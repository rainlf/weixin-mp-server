package com.rainlf.weixin.v1.infra.sso;

import com.rainlf.weixin.v1.infra.db.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author rain
 * @date 6/14/2024 2:09 PM
 */
@Slf4j
@Component
public class SsoService {
    private final ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    public User getCurrentUser() {
        User user = userThreadLocal.get();
        if (user == null) {
            throw new RuntimeException("no login status");
        }
        return user;
    }

    public void setCurrentUser(User user) {
        userThreadLocal.set(user);
    }

    public void cleanCurrentUser() {
        userThreadLocal.remove();
    }
}
