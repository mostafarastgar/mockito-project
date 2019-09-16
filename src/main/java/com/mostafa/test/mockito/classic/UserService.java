package com.mostafa.test.mockito.classic;

import com.mostafa.test.mockito.User;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UserService {
    private UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean checkAdminCode(List<User> users) {
        return users.stream().anyMatch(user -> user.getCode().equals("code 1"));
    }

    public boolean checkAdminCodeV2(List<User> users) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getCode().equals("code 1")) {
                return true;
            }
        }
        return false;
    }

    public boolean checkHealth() {
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

        List<User> byIds = userDao.findByIds(ids);
        if (!checkAdminCode(byIds)) {
            return false;
        }

        return all.size() / 2 == byIds.size();
    }
}
