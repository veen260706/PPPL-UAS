package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"steps"},
        tags = "@Login",
        monochrome = true,
        plugin = {
                "pretty",
                "html:target/cucumber-report-login.html",
                "json:target/cucumber-login.json",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        }
)
public class LoginRunner extends AbstractTestNGCucumberTests {}
