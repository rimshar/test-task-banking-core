package com.testtask.bankingcore.common.matchers;

import com.jayway.jsonpath.JsonPath;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.springframework.test.util.JsonExpectationsHelper;

public final class JsonMatcher extends BaseMatcher<String> {

    private final String expected;
    private final boolean strict;

    private JsonMatcher(String json, boolean strict) {
        super();
        this.expected = JsonPath.parse(json).jsonString();
        this.strict = strict;
    }

    public static JsonMatcher isEqualTo(String expected) {
        return new JsonMatcher(expected, false);
    }

    public static JsonMatcher isStrictlyEqualTo(String expected) {
        return new JsonMatcher(expected, true);
    }

    @Override
    public boolean matches(Object actual) {
        try {
            new JsonExpectationsHelper().assertJsonEqual(expected, (String) actual, strict);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public void describeTo(Description description) {}
}
