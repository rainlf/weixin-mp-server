package com.rainlf.weixin.infra.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rainlf.weixin.infra.db.model.UserAsset;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

/**
 * @author rain
 * @date 6/13/2024 10:34 PM
 */
@Mapper
public interface UserAssetMapper extends BaseMapper<UserAsset> {

    @Select("select * from weixin_user_asset where user_id = #{userId} ")
    Optional<UserAsset> findByUserId(@Param("userId") Integer userId);
}
