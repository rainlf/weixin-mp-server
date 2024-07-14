package com.rainlf.weixin.domain.service.impl;

import com.rainlf.weixin.app.dto.UserInfoDto;
import com.rainlf.weixin.domain.service.UserService;
import com.rainlf.weixin.infra.db.entity.User;
import com.rainlf.weixin.infra.db.entity.UserAsset;
import com.rainlf.weixin.infra.db.repository.UserAssetRepository;
import com.rainlf.weixin.infra.db.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author rain
 * @date 6/14/2024 6:57 PM
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAssetRepository userAssetRepository;

    @Override
    public UserInfoDto getUser(User user) {
        Optional<UserAsset> userAssetOptional = userAssetRepository.findByUserId(user.getId());
        if (userAssetOptional.isEmpty()) {
            throw new RuntimeException("user asset not found, id: " + user.getId());
        }

        return createUserInfo(user, userAssetOptional.get());
    }

    @Override
    public List<UserInfoDto> getAllInitedUser() {
        List<User> userList = userRepository.findAll();
        userList = userList.stream().filter(this::isUserInited).toList();
        List<UserAsset> userAssetList = userAssetRepository.findByUserIdIn(userList.stream().map(User::getId).toList());
        Map<Integer, UserAsset> userAssetMap = userAssetList.stream().collect(Collectors.toMap(UserAsset::getId, x -> x));

        return userList.stream()
                .map(user -> {
                    UserAsset userAsset = userAssetMap.get(user.getId());
                    return createUserInfo(user, userAsset);
                })
                .sorted(Comparator.comparing(UserInfoDto::getCopperCoin))
                .toList();
    }

    private boolean isUserInited(User user) {
        return StringUtils.hasText(user.getNickname()) && StringUtils.hasText(user.getAvatar());
    }

    @Override
    public UserInfoDto updateUser(User user, String nickname, String avatar) {
        user.setNickname(nickname);
        user.setAvatar(avatar);
        userRepository.save(user);

        Optional<UserAsset> userAssetOptional = userAssetRepository.findByUserId(user.getId());
        if (userAssetOptional.isEmpty()) {
            throw new RuntimeException("user asset not found, id: " + user.getId());
        }

        return createUserInfo(user, userAssetOptional.get());
    }

    private UserInfoDto createUserInfo(User user, UserAsset userAsset) {
        UserInfoDto userInfoDto = new UserInfoDto();
        if (user != null) {
            userInfoDto.setId(user.getId());
            userInfoDto.setNickname(user.getNickname());
//            userInfoDto.setAvatar(user.getAvatar());
        }
        if (userAsset != null) {
            userInfoDto.setCopperCoin(userAsset.getCopperCoin());
            userInfoDto.setSilverCoin(userAsset.getSilverCoin());
            userInfoDto.setGoldCoin(userAsset.getGoldCoin());
        }
        return userInfoDto;
    }
}
