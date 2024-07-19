package com.rainlf.weixin.v1.infra.db.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.rainlf.weixin.v1.domain.consts.MahjongFanEnum;
import com.rainlf.weixin.v1.infra.util.JsonUtils;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;

/**
 * @author rain
 * @date 6/25/2024 8:08 PM
 */
@Converter(autoApply = true)
public class ListMahjongFanEnumConverter implements AttributeConverter<List<MahjongFanEnum>, String> {
    @Override
    public String convertToDatabaseColumn(List<MahjongFanEnum> mahjongFanEnums) {
        List<String> strList = mahjongFanEnums.stream().map(Enum::name).toList();
        return JsonUtils.toJsonStr(strList);
    }

    @Override
    public List<MahjongFanEnum> convertToEntityAttribute(String s) {
        List<String> strList = JsonUtils.toObjectList(s, new TypeReference<>() {
        });
        return strList.stream().map(MahjongFanEnum::valueOf).toList();
    }
}
