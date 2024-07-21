package com.rainlf.weixin.v3.infa.db.converter;

import com.alibaba.fastjson2.JSON;
import com.rainlf.weixin.v3.domain.mahjong.consts.MjPointOperatorEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;

/**
 * @author rain
 * @date 7/21/2024 10:04 AM
 */
@Converter
public class MjPointOperatorEnumsConverter implements AttributeConverter<List<MjPointOperatorEnum>, String> {
    @Override
    public String convertToDatabaseColumn(List<MjPointOperatorEnum> attribute) {
        return JSON.toJSONString(attribute);
    }

    @Override
    public List<MjPointOperatorEnum> convertToEntityAttribute(String dbData) {
        return JSON.parseArray(dbData, MjPointOperatorEnum.class);
    }
}
