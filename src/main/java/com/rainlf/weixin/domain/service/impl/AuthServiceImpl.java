package com.rainlf.weixin.domain.service.impl;

import com.rainlf.weixin.domain.service.AuthService;
import com.rainlf.weixin.infra.db.entity.User;
import com.rainlf.weixin.infra.db.entity.UserAsset;
import com.rainlf.weixin.infra.db.repository.UserAssetRepository;
import com.rainlf.weixin.infra.db.repository.UserRepository;
import com.rainlf.weixin.infra.util.JwtUtils;
import com.rainlf.weixin.infra.wexin.model.WeixinSession;
import com.rainlf.weixin.infra.wexin.service.WeixinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author rain
 * @date 6/14/2024 11:23 AM
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private WeixinService weixinService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAssetRepository userAssetRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String login(String code) {
        WeixinSession weixinSession = weixinService.code2Session(code);

        Optional<User> userOptional = userRepository.findByOpenId(weixinSession.getOpenId());
        User user = saveUser(userOptional.orElse(null), weixinSession);
        return JwtUtils.generateToken(user.getOpenId());
    }

    @Override
    public String mockLogin() {
        User user = userRepository.findByOpenId("mock-open-id").orElse(null);
        if (user != null) {
            return JwtUtils.generateToken(user.getOpenId());
        }
        WeixinSession mockSession = new WeixinSession();
        mockSession.setOpenId("mock-open-id");
        mockSession.setUnionId("mock-union-id");
        mockSession.setSessionKey("mock, I'm a mock session key");
        user = saveUser(null, mockSession);
        return JwtUtils.generateToken(user.getOpenId());
    }

    private User saveUser(User user, WeixinSession weixinSession) {
        if (user == null) {
            user = new User();
            user.setOpenId(weixinSession.getOpenId());
            user.setUnionId(weixinSession.getUnionId());
            user.setSessionKey(weixinSession.getSessionKey());
            userRepository.save(user);

            UserAsset userAsset = new UserAsset();
            userAsset.setUserId(user.getId());
            userAssetRepository.save(userAsset);
        } else {
            user.setSessionKey(weixinSession.getSessionKey());
            userRepository.save(user);
        }
        return user;
    }
}
