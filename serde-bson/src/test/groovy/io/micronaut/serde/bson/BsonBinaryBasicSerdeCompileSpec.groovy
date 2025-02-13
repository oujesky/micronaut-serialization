package io.micronaut.serde.bson

import io.micronaut.json.JsonMapper
import io.micronaut.serde.AbstractBasicSerdeCompileSpec
import org.bson.BsonDocument

class BsonBinaryBasicSerdeCompileSpec extends AbstractBasicSerdeCompileSpec implements BsonBinarySpec {

    @Override
    Class<JsonMapper> getJsonMapperClass() {
        return BsonBinaryMapper
    }

    @Override
    BsonBinaryMapper getBsonBinaryMapper() {
        return jsonMapper
    }

    @Override
    String writeJson(JsonMapper jsonMapper, Object bean) {
        return encodeAsBinaryDecodeJson(bean)
    }

    @Override
    byte[] jsonAsBytes(String json) {
        def parse = BsonDocument.parse(json)
        return writeToByteArray(parse)
    }

}
