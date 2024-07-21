package com.rainlf.weixin.v3.infa.db.converter;

import com.rainlf.weixin.v3.domain.mahjong.consts.MjWinTypeEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * @author rain
 * @date 7/21/2024 10:01 AM
 */
@Converter(autoApply = true)
public class MjWinTypeEnumConverter implements AttributeConverter<MjWinTypeEnum, Integer> {
    @Override
    public Integer convertToDatabaseColumn(MjWinTypeEnum attribute) {
        return attribute.getCode();
    }

    @Override
    public MjWinTypeEnum convertToEntityAttribute(Integer dbData) {
        return MjWinTypeEnum.fromCode(dbData);
    }
}
