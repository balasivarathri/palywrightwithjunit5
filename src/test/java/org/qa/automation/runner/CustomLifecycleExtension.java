package org.qa.automation.runner;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.qa.automation.processor.Initialize;
import org.qa.automation.processor.Processor;
import org.qa.automation.processor.TearDown;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Slf4j
public class CustomLifecycleExtension implements BeforeAllCallback, AfterAllCallback {

    private static final String DATEFORMAT = "dd-MM-yyyy_hh-mm-ss";
    private static String executionDateTime;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        log.info("------ CustomRunner Init START ------");
        runAnnotatedMethods(context.getRequiredTestClass(), Initialize.class);

        // Original logic (optional, if not in Initialize-annotated method)
        executionDateTime = Processor.getDateAsString(DATEFORMAT);
        Processor.setTeamFeatureDirectory();
        Processor.overrideFeature();

        log.info("------ CustomRunner Init END ------");
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        log.info("------ CustomRunner Teardown START ------");

        runAnnotatedMethods(context.getRequiredTestClass(), TearDown.class);
        Processor.initializeTeardown(executionDateTime);

// ✅ Trigger HTML report generation after teardown
        org.qa.automation.report.ReportHelper.generateCucumberReport();

    }

    private void runAnnotatedMethods(Class<?> clazz, Class<? extends Annotation> annotation) {
        for (Method method : clazz.getDeclaredMethods()) {
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
