package com.rainlf.weixin.v1.infra.db.converter;

import com.rainlf.weixin.v1.domain.consts.UserScoreTypeEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * @author rain
 * @date 6/25/2024 7:59 PM
 */
@Converter(autoApply = true)
public class UserGameScoreTypeEnumConverter implements AttributeConverter<UserScoreTypeEnum, String> {


    @Override
    public String convertToDatabaseColumn(UserScoreTypeEnum userScoreTypeEnum) {
        return userScoreTypeEnum.name();
    }

    @Override
    public UserScoreTypeEnum convertToEntityAttribute(String s) {
        return UserScoreTypeEnum.valueOf(s);
    }
}
