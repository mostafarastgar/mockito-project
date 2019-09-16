package com.mostafa.test.mockito.spring;

import com.mostafa.test.mockito.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class UserServiceSpring {
    @Autowired
    private UserDao userDao;

    public boolean checkUserHealth() {
        List<User> all = userDao.findAll();
        if (all.size() == 0) {
            return false;
        }

        for (User user : all) {
            Optional<User> stOpt = userDao.findById(user.getId());
            if (stOpt.isEmpty()) {
                return false;
            }
            AtomicBoolean correct = new AtomicBoolean(true);
            stOpt.ifPresent(user1 -> correct.set(user1.getId() == user.getId()));
            if (!correct.get()) {
                return false;
            }
        }

        List<Long> ids = IntStream.range(0, all.size())
                .filter(i -> i % 2 == 0)
                .mapToObj(i -> all.get(i).getId())
                .collect(Collectors.toList());

        List<User> byIds = userDao.findAllById(ids);

        return all.size() / 2 == byIds.size();
    }

    public boolean authenticateUser(String username, String password) {
        Optional<User> byUsername = userDao.findByUsername(username);
        AtomicBoolean result = new AtomicBoolean(false);
        byUsername.ifPresent(user -> {
            result.set(user.getPassword().equals(password));
        });
        return result.get();
    }

}
