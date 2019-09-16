package com.mostafa.test.cucumber;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class StepDefs extends CucumberSpringContextConfiguration {

    private static ResponseEntity<String> latestResponse;

    @Given("^the application is up on port (\\d+)$")
    public void initApplication(int port){
        assertEquals(Integer.parseInt(environment.getProperty("local.server.port")), port);
    }

    @When("^the client calls /version$")
    public void the_client_issues_GET_version() throws Throwable{
        latestResponse = restTemplate.getForEntity("http://localhost:8080/version", String.class);
    }

    @Then("^the client receives status code of (\\d+)$")
    public void the_client_receives_status_code_of(int statusCode) {
        HttpStatus currentStatusCode = latestResponse.getStatusCode();
        assertThat("status code is incorrect : "+
                latestResponse.getBody(), currentStatusCode.value(), is(statusCode));
    }

    @And("^the client receives server version (.+)$")
    public void the_client_receives_server_version_body(String version) {
        assertThat(latestResponse.getBody(), is(version));
    }
}
