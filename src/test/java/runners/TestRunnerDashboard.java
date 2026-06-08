package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features/Dashboard.feature",
        glue = {"stepsdashboard"},
        monochrome = true,
        plugin = {
                "pretty",
                "html:target/cucumber-report-dashboard.html",
                "json:target/cucumber-dashboard.json",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        }
)
public class TestRunnerDashboard extends AbstractTestNGCucumberTests {}