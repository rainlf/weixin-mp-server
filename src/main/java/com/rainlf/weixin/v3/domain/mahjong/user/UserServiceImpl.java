package com.rainlf.weixin.v3.main.mahjong.user;

import com.rainlf.weixin.v3.domain.mahjong.user.UserService;
import com.rainlf.weixin.v3.infa.auth.JJwtUtils;
import com.rainlf.weixin.v3.infa.db.entity.AppConfig;
import com.rainlf.weixin.v3.infa.db.entity.User;
import com.rainlf.weixin.v3.infa.db.repository.AppConfigRepository;
import com.rainlf.weixin.v3.infa.db.repository.UserRepository;
import com.rainlf.weixin.v3.infa.wexin.WeixinService;
import com.rainlf.weixin.v3.infa.wexin.WeixinSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author rain
 * @date 7/21/2024 10:13 AM
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private WeixinService weixinService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AppConfigRepository appConfigRepository;

    @Value("${weixin.appid.key}")
    private String weixinAppIdKey;
    @Value("${weixin.secret.key}")
    private String weixinSecretKey;

    private final Map<String, String> appConfigCache = new ConcurrentHashMap<>();

    @Override
    public String login(String code) {
        WeixinSession weixinSession = weixinService.code2Session(getAppConfig(weixinAppIdKey), getAppConfig(weixinSecretKey), code);
        Optional<User> userOptional = userRepository.findByOpenId(weixinSession.getOpenId());
        User user = userOptional.orElseGet(() -> {
            User newUser = new User();
            newUser.setOpenId(weixinSession.getOpenId());
            return userRepository.save(newUser);
        });

        return JJwtUtils.createToken(user.getOpenId());
    }

    @Override
    public void updateUser(User user, String nickname, MultipartFile file) {
        byte[] bytes = null;
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        user.setNickname(nickname);
        user.setAvatar(bytes);
        userRepository.save(user);
    }

    private String getAppConfig(String key) {
        Assert.hasText(key, "getAppConfig key can't be empty");
        if (!appConfigCache.containsKey(key)) {
            Optional<AppConfig> appConfigOptional = appConfigRepository.findByKey(key);
            if (appConfigOptional.isPresent()) {
                AppConfig appConfig = appConfigOptional.get();
                Assert.hasText(appConfig.getValue(), "getAppConfig value can't be empty");
                appConfigCache.put(key, appConfig.getValue());
            }
        }
        return appConfigCache.get(key);
    }
}
