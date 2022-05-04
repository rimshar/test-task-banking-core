package com.testtask.bankingcore.common.assertions;

import com.jayway.jsonpath.InvalidJsonException;
import com.jayway.jsonpath.JsonPath;
import org.assertj.core.api.AbstractAssert;
import org.springframework.test.util.JsonExpectationsHelper;

public final class JsonAssert extends AbstractAssert<JsonAssert, String> {

    private JsonAssert(String json) {
        super(JsonPath.parse(json).jsonString(), JsonAssert.class);
    }

    public static JsonAssert assertThatJson(String actual) {
        return new JsonAssert(actual);
    }

    public JsonAssert isEqualTo(String json) {
        try {
            new JsonExpectationsHelper().assertJsonEqual(json, actual);
        } catch (Exception ex) {
            throw new InvalidJsonException(json, ex);
        }
        return this;
    }

    public JsonAssert isStrictlyEqualTo(String json) {
        try {
            new JsonExpectationsHelper().assertJsonEqual(json, actual, true);
        } catch (Exception ex) {
            throw new InvalidJsonException(json, ex);
        }
        return this;
    }
}
