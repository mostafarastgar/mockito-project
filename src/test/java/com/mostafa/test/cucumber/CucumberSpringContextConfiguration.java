package com.mostafa.test.cucumber;

import com.mostafa.test.mockito.spring.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(properties = "spring.autoconfigure.exclude=" +
        "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"
        , webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext
@AutoConfigureMessageVerifier
public class CucumberSpringContextConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(CucumberSpringContextConfiguration.class);

    @MockBean
    private UserDao userDao;

    @Autowired
    protected RestTemplate restTemplate;

    @Autowired
    protected Environment environment;
}