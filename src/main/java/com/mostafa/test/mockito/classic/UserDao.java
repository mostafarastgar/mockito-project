package com.mostafa.test.mockito.classic;

import com.mostafa.test.mockito.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    List<User> findAll();

    Optional<User> findById(long id);

    List<User> findByIds(List<Long> id);
}
