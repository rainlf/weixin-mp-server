package com.rainlf.weixin.infra.db.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.rainlf.weixin.domain.consts.MahjongFanEnum;
import com.rainlf.weixin.domain.consts.MahjongWinCaseEnum;
import com.rainlf.weixin.infra.util.JsonUtils;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;

/**
 * @author rain
 * @date 6/25/2024 8:08 PM
 */
@Converter(autoApply = true)
public class MahjongWinCaseEnumConverter implements AttributeConverter<MahjongWinCaseEnum, String> {

    @Override
    public String convertToDatabaseColumn(MahjongWinCaseEnum mahjongWinCaseEnum) {
        return mahjongWinCaseEnum.name();
    }

    @Override
    public MahjongWinCaseEnum convertToEntityAttribute(String s) {
        return MahjongWinCaseEnum.valueOf(s);
    }
}
