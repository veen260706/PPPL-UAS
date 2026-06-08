package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features/PatientList.feature",
        glue = {"stepsdashboard"},
        monochrome = true,
        plugin = {
                "pretty",
                "html:target/cucumber-report-patientlist.html",
                "json:target/cucumber-patientlist.json",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        }
)
public class TestRunnerPatientList extends AbstractTestNGCucumberTests {}