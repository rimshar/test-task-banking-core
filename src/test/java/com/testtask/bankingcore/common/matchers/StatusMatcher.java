package com.testtask.bankingcore.common.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.springframework.http.HttpStatus;

public final class StatusMatcher extends BaseMatcher<Integer> {

    private final int expected;

    private StatusMatcher(int expected) {
        super();
        this.expected = expected;
    }

    public static StatusMatcher isOk() {
        return new StatusMatcher(HttpStatus.OK.value());
    }

    public static StatusMatcher isBadRequest() {
        return new StatusMatcher(HttpStatus.BAD_REQUEST.value());
    }

    public static StatusMatcher notFound() {
        return new StatusMatcher(HttpStatus.NOT_FOUND.value());
    }

    @Override
    public boolean matches(Object actual) {
        return expected == (Integer) actual;
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expected);
    }
}
