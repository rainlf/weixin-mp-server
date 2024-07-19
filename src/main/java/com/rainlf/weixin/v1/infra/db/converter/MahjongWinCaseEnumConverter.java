package com.rainlf.weixin.v1.infra.db.converter;

import com.rainlf.weixin.v1.domain.consts.MahjongWinCaseEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

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
