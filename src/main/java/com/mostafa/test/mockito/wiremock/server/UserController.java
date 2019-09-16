package com.mostafa.test.mockito.wiremock.server;

import com.mostafa.test.mockito.User;
import com.mostafa.test.mockito.spring.UserServiceSpring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Qualifier("userServiceSpring")
    @Autowired
    private UserServiceSpring userService;

    @PostMapping("/user/authenticate")
    @ResponseStatus(HttpStatus.OK)
    public String authenticateUser(@RequestBody User user) {
        return String.valueOf(userService.authenticateUser(user.getUsername(), user.getPassword()));
    }
}
