package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"steps"},
        tags = "@Profile",
        monochrome = true,
        plugin = {
                "pretty",
                "html:target/cucumber-report-profile.html",
                "json:target/cucumber-profile.json",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        }
)
public class ProfileRunner extends AbstractTestNGCucumberTests {}
