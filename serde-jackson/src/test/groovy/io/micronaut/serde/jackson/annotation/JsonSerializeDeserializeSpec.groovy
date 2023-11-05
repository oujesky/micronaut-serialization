package io.micronaut.serde.jackson.annotation

import io.micronaut.serde.jackson.JsonCompileSpec

class JsonSerializeDeserializeSpec extends JsonCompileSpec {

    void 'test json serialize/deserialize as'() {
        given:
        def context = buildContext('test.Test', """
package test;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.micronaut.serde.annotation.Serdeable;

@JsonSerialize(as = TestImpl.class)
@JsonDeserialize(as = TestImpl.class)
interface Test {
    String getValue();
}

@Serdeable
class TestImpl implements Test {
    private final String value;
    TestImpl(String value) {
        this.value = value;
    }
    
    @Override
    public String getValue() {
        return value;
    }
}
""")

        when:
        def result = jsonMapper.readValue('{"value":"test"}', typeUnderTest)

        then:
        result.getClass().name == 'test.TestImpl'
        result.value == 'test'

        when:
        def json = writeJson(jsonMapper, result)

        then:
        json == '{"value":"test"}'
    }

    void 'test json serialize/deserialize as different impls'() {
        given:
        def context = buildContext("""
package test;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.micronaut.serde.annotation.Serdeable;

@JsonSerialize(as = ServerAuthentication.class)
@JsonDeserialize(as = ClientAuthentication.class)
interface Authentication {
    String getValue();
}

@Serdeable
class ClientAuthentication extends ServerAuthentication implements Authentication {
    ClientAuthentication(String value) {
        super(value);
    }
    
    public String getAnother() {
        return "Shouldn't appear in serialization output";
    }
}

@Serdeable
class ServerAuthentication implements Authentication {
    private final String value;
    ServerAuthentication(String value) {
        this.value = value;
    }
    
    @Override
    public String getValue() {
        return value;
    }
}
""")

        when:
        def authType = argumentOf(context, "test.Authentication")
        def result = jsonMapper.readValue('{"value":"test"}', authType)

        then:
        result.getClass().name == 'test.ClientAuthentication'
        result.value == 'test'

        when:
        def json = jsonMapper.writeValueAsString(
                authType,
                result
        )

        then:
        json == '{"value":"test"}'

        cleanup:
        context.close()
    }

    void "test json serialize/deserialize as different impl overridden on field"() {
        given:
        def context = buildContext("""
package test;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.micronaut.serde.annotation.Serdeable;

import java.util.List;
import java.util.LinkedList;

@JsonSerialize(as = DefaultTestImpl.class)
@JsonDeserialize(as = DefaultTestImpl.class)
interface TestInterface {
    String getValue();
}

@Serdeable
class DefaultTestImpl implements TestInterface {
    private final String value;
    DefaultTestImpl(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}

@Serdeable
class CustomTestImpl implements TestInterface {
    private final String value;
    CustomTestImpl(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    public boolean isCustom() {
        return true;
    }
}

@Serdeable
class Test {

    @JsonSerialize(as = CustomTestImpl.class)
    @JsonDeserialize(as = CustomTestImpl.class)
    private final TestInterface object;

    @JsonDeserialize(as = LinkedList.class)
    private final List<Integer> list;


    public Test(TestInterface object, List<Integer> list) {
        this.object = object;
        this.list = list;
    }

    public TestInterface getObject() {
        return object;
    }

    public List<Integer> getList() {
        return list;
    }
}
""")
        when:
        def result = jsonMapper.readValue('{"object":{"value":"test"},"list":[1,2,3]}', typeUnderTest)

        then:
        result.object.getClass().name == 'test.CustomTestImpl'
        result.object.value == "test"

        result.list == [1, 2, 3]
        result.list.getClass().name == 'java.util.LinkedList'

        when:
        def json = writeJson(jsonMapper, result)

        then:
        json == '{"object":{"value":"test","custom":true},"list":[1,2,3]}'

        cleanup:
        context.close()
    }
}
