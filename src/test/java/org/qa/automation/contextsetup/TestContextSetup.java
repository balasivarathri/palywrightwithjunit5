package org.qa.automation.contextsetup;

import org.qa.automation.base.TestBase;
import org.qa.automation.objectmangaer.PageObjectManager;

import java.io.IOException;

public class TestContextSetup {

    public PageObjectManager pageObjectManager;

    public TestContextSetup() throws IOException {
        pageObjectManager = new PageObjectManager(TestBase.browserInitialization());
    }
}
