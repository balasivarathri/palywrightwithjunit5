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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class Hook {

    private static final String DATEFORMAT = "dd-MM-yyyy_hh-mm-ss";
    private static String executionDateTime;
    private static final AtomicBoolean isBeforeAllDone = new AtomicBoolean(false);
    private static final AtomicBoolean isAfterAllDone = new AtomicBoolean(false);

    public Hook() {}

    @Before(order = 0)
    public void beforeScenario(Scenario scenario) {
        TestBase.scenario = scenario;

        // Simulate BeforeAllCallback logic
        if (isBeforeAllDone.compareAndSet(false, true)) {
            log.info("------ CustomRunner Init START ------");
            runAnnotatedMethods(Initialize.class);
            executionDateTime = Processor.getDateAsString(DATEFORMAT);
            log.info("------ CustomRunner Init END ------");
        }

        log.info("------ Scenario: START ------");
        log.info("Scenario Name: {}", scenario.getName());
        System.out.println(">>> Running Scenario: " + scenario.getName() +
                " | Thread: " + Thread.currentThread().getName());
    }

    @After(order = 0)
    public void afterScenario(Scenario scenario) {
        log.info("Scenarios Result: {}", scenario.getStatus());
        log.info("------ Scenario: END ------");

        // Simulate AfterAllCallback logic (only after last scenario runs)
        // This logic assumes test teardown is triggered once per JVM shutdown
        // You could also do this with a shutdown hook or after last scenario flag
        if (isAfterAllDone.compareAndSet(false, true)) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    log.info("------ CustomRunner Teardown START (Simulated) ------");

                    runAnnotatedMethods(TearDown.class);
                    Processor.initializeTeardown(executionDateTime);

                    ReportHelper.generateCucumberReport();

                    log.info("------ CustomRunner Teardown END ------");
                } catch (Exception e) {
                    log.error("Error during simulated teardown: {}", e.getMessage(), e);
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
