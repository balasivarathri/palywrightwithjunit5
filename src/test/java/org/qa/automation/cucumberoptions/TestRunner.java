package org.qa.automation.cucumberoptions;
import org.junit.platform.suite.api.*;
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")  // folder inside src/test/resources
public class TestRunner {
}
