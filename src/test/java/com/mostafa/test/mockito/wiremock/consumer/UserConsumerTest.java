package com.mostafa.test.mockito.wiremock.consumer;

import com.mostafa.test.mockito.User;
import com.mostafa.test.mockito.spring.UserDao;
import com.mostafa.test.mockito.wiremock.client.UserManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.cloud.contract.verifier.assertion.SpringCloudContractAssertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
@AutoConfigureWireMock(httpsPort = 7443)
//@AutoConfigureWireMock(httpsPort = 7443, stubs = "classpath*:/META-INF/**/mappings/**/*.json")
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})

public class UserConsumerTest {
    @MockBean
    private UserDao userDao;

    @Autowired
    private UserManager userManager;

    @Test
    public void testAddUser() throws Exception {
        stubFor(post(urlEqualTo("/user/authenticate"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(containing("\"username\":\"name1\",\"password\":\"123\""))
                .willReturn(aResponse()
                        .withStatus(200).withBody("true")));
        assertThat(userManager.addUser("name1", "123", new User())).isEqualTo(true);
    }

}
