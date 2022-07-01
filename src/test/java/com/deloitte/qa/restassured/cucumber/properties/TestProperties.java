package com.deloitte.qa.restassured.cucumber.properties;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;

import java.io.File;
import java.util.List;
import java.util.Random;

public class TestProperties {
    private static final String TEST_PROPERTIES = "/test.properties";
    private static final String INVALID_TEST_PROPERTIES = "/invalid_test.properties";

    private Configuration testConfiguration;
    private Configuration invalidTestConfiguration;

    public TestProperties() {
        setConfigurations();
    }

    private void setConfigurations() {
        Configurations configurations = new Configurations();
        try {
            this.testConfiguration = configurations.properties(new File("src/test/resources/properties" + TEST_PROPERTIES));
            this.invalidTestConfiguration = configurations.properties(new File("src/test/resources/properties") + INVALID_TEST_PROPERTIES);
        } catch (Exception e) {
            System.out.println("Encountered an exception while processing one of the property configurations.");
        }
    }

    public String getTestProperty(String key) {
        return testConfiguration.getString(key);
    }

    public String getRandomTestProperty(String key) {
        return getRandomFromList(testConfiguration.getList(key));
    }

    public String getRandomInvalidTestProperty(String key) {
        return getRandomFromList(invalidTestConfiguration.getList(key));
    }

    private String getRandomFromList(List<Object> list) {
        return list.get(new Random().nextInt(list.size())).toString();
    }
}
