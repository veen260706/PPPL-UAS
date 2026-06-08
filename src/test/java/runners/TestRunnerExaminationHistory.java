package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features/ExaminationHistory.feature",
        glue = {"stepsdashboard"},
        monochrome = true,
        plugin = {
                "pretty",
                "html:target/cucumber-report-examhistory.html",
                "json:target/cucumber-examhistory.json",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        }
)
public class TestRunnerExaminationHistory extends AbstractTestNGCucumberTests {}