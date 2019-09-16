package com.mostafa.test.mockito.wiremock.producer;

import com.mostafa.test.mockito.User;
import com.mostafa.test.mockito.wiremock.server.UserController;
import com.mostafa.test.mockito.spring.UserDao;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration", webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext
@AutoConfigureMessageVerifier
public abstract class UserProducerTest {
    @Autowired
    private UserController userController;

    @MockBean
    private UserDao userDao;

    @Before
    public void setup() {
        StandaloneMockMvcBuilder standaloneMockMvcBuilder
                = MockMvcBuilders.standaloneSetup(userController);
        RestAssuredMockMvc.standaloneSetup(standaloneMockMvcBuilder);
        when(userDao.findByUsername("name1")).thenReturn(Optional.of(new User(1, "name1", "123",
                "code 1", LocalDate.now())));
    }
}
