package com.mostafa.test.mockito.spring;


import com.mostafa.test.mockito.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class TestUserServiceSpring {

    @TestConfiguration
    static class UserServiceTestContextConfiguration {

        @Bean
        public UserServiceSpring employeeService() {
            return new UserServiceSpring();
        }
    }

    @MockBean
    private UserDao userDao;

    @Autowired
    private UserServiceSpring userServiceSpring;

    @Captor
    private ArgumentCaptor<List<Long>> idsCaptor;

    @Before
    public void initMocks() {
        ArrayList<User> users = new ArrayList<>() {{
            add(new User(1l, "name1", "123", "code 1", LocalDate.now()));
            add(new User(2l, "name2", "123", "code 2", LocalDate.now()));
            add(new User(3l, "name3", "123", "code 3", LocalDate.now()));
            add(new User(4l, "name4", "123", "code 4", LocalDate.now()));
        }};

        when(userDao.findAll()).thenReturn(users);

        when(userDao.findById(anyLong())).thenAnswer(
                invocation -> users.stream().filter(user -> user.getId() == ((Long) invocation.getArguments()[0]))
                        .findAny());

        when(userDao.findAllById(anyList())).thenAnswer(invocation -> {
            List<Long> ids = (List<Long>) invocation.getArguments()[0];
            List<User> results = new ArrayList<>() {{
                for (Object argument : ids) {
                    userDao.findById((Long) argument).ifPresent(user -> add(user));
                }
            }};
            return results;
        });
    }

    @Test
    public void userServiceHealthCheck() {
        assertEquals(userServiceSpring.checkUserHealth(), true);

        verify(userDao, times(2)).findById(1l);
        verify(userDao, atLeastOnce()).findById(2l);
        verify(userDao, times(2)).findById(3l);
        verify(userDao, atLeast(1)).findById(4l);
        verify(userDao, atMost(1)).findAll();

        verify(userDao).findAllById(idsCaptor.capture());
        List<Long> ids = idsCaptor.getValue();
        assertThat(ids, hasItems(1l, 3l));

        verifyNoMoreInteractions(userDao);
    }
}
