package com.rainlf.weixin.infra.db.converter;

import com.rainlf.weixin.domain.consts.UserGameScoreTypeEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * @author rain
 * @date 6/25/2024 7:59 PM
 */
@Converter(autoApply = true)
public class UserGameScoreTypeEnumConverter implements AttributeConverter<UserGameScoreTypeEnum, String> {


    @Override
    public String convertToDatabaseColumn(UserGameScoreTypeEnum userGameScoreTypeEnum) {
        return userGameScoreTypeEnum.name();
    }

    @Override
    public UserGameScoreTypeEnum convertToEntityAttribute(String s) {
        return UserGameScoreTypeEnum.valueOf(s);
    }
}
