package com.rainlf.weixin.v3.app.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.rainlf.weixin.v3.domain.mahjong.consts.MjGameTypeEnum;

import java.io.IOException;

/**
 * @author rain
 * @date 7/21/2024 6:54 PM
 */
public class MjGameTypeEnumDeserializer extends JsonDeserializer<MjGameTypeEnum> {
    @Override
    public MjGameTypeEnum deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        int code = jsonParser.getIntValue();
        return MjGameTypeEnum.fromCode(code);
    }
}
