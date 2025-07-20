package org.qa.automation.report;

import com.microsoft.playwright.Page;
import io.cucumber.java.Scenario;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.qa.automation.base.TestBase;
import org.qa.automation.exceptions.GenericException;

@Slf4j
public class Report {

    private Report() {
    }

    public static void log(Scenario scenario, String description) {
        scenarioLogSafe(scenario, description);
    }

    public static void log(Scenario scenario, String description, String attachment) {
        scenarioLogSafe(scenario, description + ":\n" + attachment);
    }

    public static void fail(Scenario scenario, String failureReason, String exceptionCaughtMessage) {
        String message = "Failure Reason: " + failureReason + "\n" + exceptionCaughtMessage;
        scenarioLogSafe(scenario, message);
        throw new GenericException(message);
    }

    public static void validate(Scenario scenario, String description, String failureReason, String expected, String actual) {
        try {
            Assertions.assertThat(actual).isEqualTo(expected);
            scenarioLogSafe(scenario, description + "\nExpected [" + expected + "] and found [" + actual + "]");
        } catch (AssertionError e) {
            String failureMessage = description + "\nFailure Reason: " + failureReason + "\nExpected [" + expected + "] but found [" + actual + "]";
            scenarioLogSafe(scenario, failureMessage);
            throw new AssertionError(failureMessage + "\n" + e.getMessage());
        }
    }

    public static void validate(Scenario scenario, String description, String failureReason, int expected, int actual) {
        try {
            Assertions.assertThat(actual).isEqualTo(expected);
            scenarioLogSafe(scenario, description + "\nExpected [" + expected + "] and found [" + actual + "]");
        } catch (AssertionError e) {
            String failureMessage = description + "\nFailure Reason: " + failureReason + "\nExpected [" + expected + "] but found [" + actual + "]";
            scenarioLogSafe(scenario, failureMessage);
            throw new AssertionError(failureMessage + "\n" + e.getMessage());
        }
    }

    public static void screenshot(Scenario scenario) {
        Scenario currentScenario = scenario != null ? scenario : TestBase.getScenario();
        if (currentScenario != null) {
            try {
                byte[] screenshot = TestBase.page().screenshot(new Page.ScreenshotOptions().setFullPage(true));
                currentScenario.attach(screenshot, "image/png", "Screenshot");
            } catch (IllegalStateException e) {
                log.warn("Skipping scenario.attach() due to state issue: {}", e.getMessage());
            } catch (Exception ex) {
                log.error("Failed to capture screenshot: {}", ex.getMessage(), ex);
            }
        } else {
            log.warn("Cannot attach screenshot: Scenario is null.");
        }
    }

    private static void scenarioLogSafe(Scenario scenario, String message) {
        Scenario currentScenario = scenario != null ? scenario : TestBase.getScenario();
        if (currentScenario != null) {
            try {
                currentScenario.log(message);
            } catch (IllegalStateException e) {
                log.warn("Skipping scenario.log() due to state issue: {}", e.getMessage());
            }
        } else {
            log.warn("Scenario is null. Log message: {}", message);
        }
    }
}