package com.rainlf.weixin.v3.infa.db.converter;

import com.rainlf.weixin.v3.domain.mahjong.consts.MjUserTypeEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * @author rain
 * @date 7/21/2024 9:58 AM
 */
@Converter(autoApply = true)
public class MjUserTypeEnumConverter implements AttributeConverter<MjUserTypeEnum, Integer> {
    @Override
    public Integer convertToDatabaseColumn(MjUserTypeEnum attribute) {
        return attribute.getCode();
    }

    @Override
    public MjUserTypeEnum convertToEntityAttribute(Integer dbData) {
        return MjUserTypeEnum.fromCode(dbData);
    }
}
