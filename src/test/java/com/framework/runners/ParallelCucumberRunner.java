package com.framework.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Parallel Cucumber runner.
 * Activated via -Dsuite=testng-parallel.xml or mvn test -Pparallel.
 */
@CucumberOptions(
    features   = "src/test/resources/features",
    glue       = {"com.framework.stepdefinitions", "com.framework.hooks"},
    tags       = "@regression",
    plugin     = {
        "pretty",
        "html:reports/cucumber-parallel-report.html",
        "json:reports/cucumber-parallel-report.json",
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
    },
    monochrome = true
)
public class ParallelCucumberRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
