import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/resources/restassured-cucumber-api-project"},
        glue = {"com.deloitte.qa"},
        plugin = {"pretty","json:target/cucumber-report/cucumber.json"}
)
public class RunCucumberTest {

}