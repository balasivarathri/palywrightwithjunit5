package org.qa.automation.base;

import com.microsoft.playwright.*;
import io.cucumber.java.Scenario;
import lombok.extern.slf4j.Slf4j;
import org.qa.automation.urls.Url;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

@Slf4j
public class TestBase {

    private static final ThreadLocal<Playwright> playwrightThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Browser> browserThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> browserContextThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Page> pageThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<APIRequestContext> apiRequestContextThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Scenario> scenarioThreadLocal = new ThreadLocal<>();
    private static Properties prop;

    public static Page browserInitialization() throws IOException {
        loadProperties();

        Playwright playwright = getOrCreatePlaywright();

        String browserName = System.getProperty("browser", prop.getProperty("browser", "chromium")).toLowerCase();
        Browser browser = switch (browserName) {
            case "chrome" -> playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(false).setArgs(List.of("--start-maximized")));
            case "edge" -> playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("msedge").setHeadless(false).setArgs(List.of("--start-maximized")));
            case "firefox" -> playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false).setArgs(List.of("--start-maximized")));
            case "safari" -> playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false).setArgs(List.of("--start-maximized")));
            default -> playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setArgs(List.of("--start-maximized")));
        };

        browserThreadLocal.set(browser);

        BrowserContext context = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));
        context.clearCookies();
        browserContextThreadLocal.set(context);

        Page page = context.newPage();
        context.tracing().start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true).setSources(true));
        pageThreadLocal.set(page);

        log.info("Browser initialized: {}", browserName);
        return page;
    }

    public static APIRequestContext apiInitialization() {
        Playwright playwright = getOrCreatePlaywright();

        APIRequestContext apiRequestContext = playwright.request().newContext();
        apiRequestContextThreadLocal.set(apiRequestContext);

        log.info("APIRequestContext initialized.");
        return apiRequestContext;
    }

    public static void userUrl(String url) {
        page().navigate(Url.getUrl(url));
    }

    public static Page page() {
        return pageThreadLocal.get();
    }

    public static BrowserContext browserContext() {
        return browserContextThreadLocal.get();
    }

    public static Browser browser() {
        return browserThreadLocal.get();
    }

    public static APIRequestContext apiRequestContext() {
        return apiRequestContextThreadLocal.get();
    }

    public static void setScenario(Scenario scenario) {
        scenarioThreadLocal.set(scenario);
    }

    public static Scenario getScenario() {
        return scenarioThreadLocal.get();
    }

    public static void cleanup() {
        try {
            if (pageThreadLocal.get() != null && !pageThreadLocal.get().isClosed()) {
                pageThreadLocal.get().close();
                log.info("Page closed.");
            }
        } catch (Exception e) {
            log.error("Failed to close Page: {}", e.getMessage(), e);
        } finally {
            pageThreadLocal.remove();
        }

        try {
            if (browserContextThreadLocal.get() != null) {
                browserContextThreadLocal.get().close();
                log.info("BrowserContext closed.");
            }
        } catch (Exception e) {
            log.error("Failed to close BrowserContext: {}", e.getMessage(), e);
        } finally {
            browserContextThreadLocal.remove();
        }

        try {
            if (browserThreadLocal.get() != null) {
                browserThreadLocal.get().close();
                log.info("Browser closed.");
            }
        } catch (Exception e) {
            log.error("Failed to close Browser: {}", e.getMessage(), e);
        } finally {
            browserThreadLocal.remove();
        }

        try {
            if (apiRequestContextThreadLocal.get() != null) {
                apiRequestContextThreadLocal.get().dispose();
                log.info("APIRequestContext disposed.");
            }
        } catch (Exception e) {
            log.error("Failed to dispose APIRequestContext: {}", e.getMessage(), e);
        } finally {
            apiRequestContextThreadLocal.remove();
        }

        scenarioThreadLocal.remove();
    }

    private static Playwright getOrCreatePlaywright() {
        if (playwrightThreadLocal.get() == null) {
            playwrightThreadLocal.set(Playwright.create());
        }
        return playwrightThreadLocal.get();
    }

    private static void loadProperties() throws IOException {
        if (prop == null) {
            prop = new Properties();
            try (FileInputStream fileInputStream = new FileInputStream("src/main/java/org/qa/automation/config/config.properties")) {
                prop.load(fileInputStream);
            }
            log.info("Config properties loaded.");
        }
    }
    public static void attachScreenshotToScenario(Scenario scenario) {
        try {
            if (page() != null) {
                byte[] screenshot = page().screenshot(new Page.ScreenshotOptions().setFullPage(true));
                scenario.attach(screenshot, "image/png", "Failed Scenario Screenshot");
                log.info("Screenshot attached for failed scenario: {}", scenario.getName());
            } else {
                log.warn("Page is null. Cannot capture screenshot for scenario: {}", scenario.getName());
            }
        } catch (Exception e) {
            log.error("Failed to capture screenshot for scenario: {}. Error: {}", scenario.getName(), e.getMessage(), e);
        }
    }

}
