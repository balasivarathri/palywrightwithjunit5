package org.qa.automation.cucumberoptions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.extern.slf4j.Slf4j;
import org.qa.automation.base.TestBase;
import org.qa.automation.processor.Initialize;
import org.qa.automation.processor.Processor;
import org.qa.automation.processor.TearDown;
import org.qa.automation.report.ReportHelper;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class Hook {

    private static final String DATEFORMAT = "dd-MM-yyyy_hh-mm-ss";
    private static String executionDateTime;
    private static final AtomicBoolean isBeforeAllDone = new AtomicBoolean(false);
    private static final AtomicBoolean isAfterAllDone = new AtomicBoolean(false);

    @Before(order = 0)
    public void beforeScenario(Scenario scenario) throws IOException {
        TestBase.setScenario(scenario);

        if (isBeforeAllDone.compareAndSet(false, true)) {
            log.info("------ CustomRunner Init START ------");
            runAnnotatedMethods(Initialize.class);
            executionDateTime = Processor.getDateAsString(DATEFORMAT);
            log.info("------ CustomRunner Init END ------");
        }

        TestBase.browserInitialization();

        log.info("------ Scenario: START ------");
        log.info("Scenario Name: {}", scenario.getName());
        System.out.println(">>> Running Scenario: " + scenario.getName() +
                " | Thread: " + Thread.currentThread().getName());
    }

    @After(order = 0)
    public void afterScenario(Scenario scenario) {
        log.info("Scenarios Result: {}", scenario.getStatus());
        log.info("------ Scenario: END ------");

        if (scenario.isFailed()) {
            TestBase.attachScreenshotToScenario(scenario);
        }

        TestBase.cleanup();

        if (isAfterAllDone.compareAndSet(false, true)) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    log.info("------ CustomRunner Teardown START ------");
                    runAnnotatedMethods(TearDown.class);
                    Processor.initializeTeardown(executionDateTime);
                    ReportHelper.generateCucumberReport();
                    log.info("------ CustomRunner Teardown END ------");
                } catch (Exception e) {
                    log.error("Error during teardown: {}", e.getMessage(), e);
                }
            }));
        }
    }

    private void runAnnotatedMethods(Class<? extends Annotation> annotation) {
        for (Method method : Hook.class.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                try {
                    method.setAccessible(true);
                    method.invoke(null); // invoke static method
                } catch (Exception e) {
                    log.error("Error invoking method {}: {}", method.getName(), e.getMessage(), e);
                }
            }
        }
    }
}