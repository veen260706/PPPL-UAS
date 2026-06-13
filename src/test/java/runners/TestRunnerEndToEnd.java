package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features/EndToEnd.feature",
        glue = {"steps"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports/e2e-report.html",
                "json:target/cucumber-reports/e2e.json"
        }
)
public class TestRunnerEndToEnd extends AbstractTestNGCucumberTests {
}