package com.framework.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Default Cucumber runner — sequential execution.
 * Central execution is triggered via pom.xml → surefire → testng.xml.
 */
@CucumberOptions(
    features   = "src/test/resources/features",
    glue       = {"com.framework.stepdefinitions", "com.framework.hooks"},
    tags       = "@regression",
    plugin     = {
        "pretty",
        "html:reports/cucumber-report.html",
        "json:reports/cucumber-report.json",
        "junit:reports/cucumber-report.xml",
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
    },
    monochrome = true,
    publish    = false
)
public class CucumberRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
