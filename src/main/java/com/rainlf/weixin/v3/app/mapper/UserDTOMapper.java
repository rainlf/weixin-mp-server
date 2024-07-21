package com.rainlf.weixin.v3.app.mapper;

import com.rainlf.weixin.v3.app.dto.UserDTO;
import com.rainlf.weixin.v3.infa.db.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * @author rain
 * @date 7/20/2024 7:20 AM
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserDTOMapper {

    UserDTO fromUserDO(User user);
}
