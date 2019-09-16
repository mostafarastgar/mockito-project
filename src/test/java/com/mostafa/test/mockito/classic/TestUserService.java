package com.mostafa.test.mockito.classic;

import com.mostafa.test.mockito.FinalClass;
import com.mostafa.test.mockito.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TestUserService {
    private FinalClass instanceFinalClass = new FinalClass();

    @Mock
    private UserDao userDao;

    @Mock
    FinalClass mockFinalClass;

    @Spy
    List<User> fakeUserList = new ArrayList<>();

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<List<Long>> idsCaptor;

    @Before
    public void initMocks(){
        initFakeUserList();

        initUserDao();

        initFinalClass();
    }

    private void initFinalClass() {
        when(mockFinalClass.finalMethod()).thenReturn("that other thing");
    }

    private void initUserDao() {
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

        when(userDao.findByIds(anyList())).thenAnswer(invocation -> {
            List<Long> ids = (List<Long>) invocation.getArguments()[0];
            List<User> results = new ArrayList<>() {{
                for (Object argument : ids) {
                    userDao.findById((Long) argument).ifPresent(user -> add(user));
                }
            }};
            return results;
        });
    }

    private void initFakeUserList() {
        User user = new User(1l, "name1", "123", "code 1", LocalDate.now());

        doReturn(user).when(fakeUserList).get(anyInt());
        doReturn(1).when(fakeUserList).size();
//        lenient().doReturn(user).when(fakeUserList).get(anyInt());
//        lenient().doReturn(1).when(fakeUserList).size();
        doReturn(Stream.of(user)).when(fakeUserList).stream();
    }

    @Test
    public void userServiceAdminCheck() {
        assertTrue(userService.checkAdminCode(fakeUserList));
//        comment below line and check the exception
        assertTrue(userService.checkAdminCodeV2(fakeUserList));

    }

    @Test
    public void userServiceHealthCheck() {
        assertTrue(userService.checkHealth());

        verify(userDao, times(2)).findById(1);
        verify(userDao, atLeastOnce()).findById(2);
        verify(userDao, times(2)).findById(3);
        verify(userDao, atLeast(1)).findById(4);
        verify(userDao, atMost(1)).findAll();

        verify(userDao).findByIds(idsCaptor.capture());
        List<Long> ids = idsCaptor.getValue();
        assertThat(ids, hasItems(1l, 3l));

        verifyNoMoreInteractions(userDao);
    }

    @Test
    public final void mockFinalClassTest() {
        assertNotEquals(mockFinalClass.finalMethod(), instanceFinalClass.finalMethod());
    }
}
