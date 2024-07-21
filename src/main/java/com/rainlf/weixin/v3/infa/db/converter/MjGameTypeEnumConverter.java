package com.rainlf.weixin.v3.infa.db.converter;

import com.rainlf.weixin.v3.domain.mahjong.consts.MjGameTypeEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * @author rain
 * @date 7/21/2024 10:01 AM
 */
@Converter(autoApply = true)
public class MjGameTypeEnumConverter implements AttributeConverter<MjGameTypeEnum, Integer> {
    @Override
    public Integer convertToDatabaseColumn(MjGameTypeEnum attribute) {
        return attribute.getCode();
    }

    @Override
    public MjGameTypeEnum convertToEntityAttribute(Integer dbData) {
        return MjGameTypeEnum.fromCode(dbData);
    }
}
