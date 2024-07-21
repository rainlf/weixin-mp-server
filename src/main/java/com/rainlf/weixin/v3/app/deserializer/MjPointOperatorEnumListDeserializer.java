package com.rainlf.weixin.v3.app.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.rainlf.weixin.v3.domain.mahjong.consts.MjPointOperatorEnum;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author rain
 * @date 7/21/2024 6:57 PM
 */
public class MjPointOperatorEnumListDeserializer extends JsonDeserializer<List<MjPointOperatorEnum>> {
    @Override
    public List<MjPointOperatorEnum> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        List<MjPointOperatorEnum> result = new ArrayList<>();
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        if (node.isArray()) {
            node.forEach(jsonNode -> {
                int code = jsonNode.asInt();
                result.add(MjPointOperatorEnum.fromCode(code));
            });
        }
        return result;
    }
}
