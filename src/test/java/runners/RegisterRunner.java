package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"steps"},
        tags = "@Register",
        monochrome = true,
        plugin = {
                "pretty",
                "html:target/cucumber-report-register.html",
                "json:target/cucumber-register.json",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        }
)
public class RegisterRunner extends AbstractTestNGCucumberTests {}
