package org.qa.automation.report;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;

import java.io.File;
import java.util.ArrayList;

public class ReportHelper {
    public static void generateCucumberReport(){
        System.out.println("Inside generateCucumberReport");

        // Use a separate folder to avoid conflicts
        File reportOutputDirectory = new File("target/cucumber-html-reports");
        ArrayList<String> jsonFiles = new ArrayList<>();
        jsonFiles.add("target/cucumber-reports/cucumber.json"); // ✅ Correct path
        String projectname = "HTML-Status-Report";

        Configuration configuration = new Configuration(reportOutputDirectory, projectname);
        configuration.addClassifications("Platform", "Windows11");
        configuration.addClassifications("Browser", "Chrome");
        configuration.addClassifications("Environment", "UAT");
        configuration.addClassifications("Test Team", "APPLICATION_UAT");

        try {
            ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
            reportBuilder.generateReports();
            System.out.println("Report generated at: target/html-report/index.html");
        } catch (Exception e) {
            System.err.println("Failed to generate report: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
