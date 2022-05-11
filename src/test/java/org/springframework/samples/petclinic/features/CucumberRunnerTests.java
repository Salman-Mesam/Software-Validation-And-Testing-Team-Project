package org.springframework.samples.petclinic.features;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = { "pretty", "html:target/cucumber/cucumber", "json:target/cucumber/cucumber.json" },
		features = "src/test/resources/features", glue = "org.springframework.samples.petclinic.features")
public class CucumberRunnerTests {

}
