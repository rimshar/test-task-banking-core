package com.testtask.bankingcore.common.listeners;

import io.restassured.RestAssured;
import lombok.val;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

import static java.util.Objects.requireNonNull;

public class RestAssuredTestExecutionListener implements TestExecutionListener {

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        TestExecutionListener.super.beforeTestClass(testContext);
        RestAssured.port = localServerPort(testContext);
    }

    private int localServerPort(TestContext testContext) {
        val environment = testContext.getApplicationContext().getBean(Environment.class);
        val port = requireNonNull(environment.getProperty("local.server.port"));
        return Integer.parseInt(port);
    }
}
