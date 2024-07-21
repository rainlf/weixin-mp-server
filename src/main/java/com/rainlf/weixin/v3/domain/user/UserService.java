package com.rainlf.weixin.v3.domain.user;

import com.rainlf.weixin.v3.infa.db.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author rain
 * @date 7/21/2024 10:12 AM
 */
public interface UserService {

    String login(String code);

    void updateUser(User user, String nickname, MultipartFile file);

    User findById(Integer id);

    List<User> findByIdIn(List<Integer> ids);

    List<User> findAll();
}
