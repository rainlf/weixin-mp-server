package com.rainlf.weixin.v3.app.assembler;

import com.rainlf.weixin.v3.app.dto.UserDTO;
import com.rainlf.weixin.v3.infa.db.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * @author rain
 * @date 7/20/2024 7:20 AM
 */
@Component
public class UserDTOAssembler {

    public UserDTO fromUserDO(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }
}
