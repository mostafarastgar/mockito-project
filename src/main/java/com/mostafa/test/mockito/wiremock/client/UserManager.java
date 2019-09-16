package com.mostafa.test.mockito.wiremock.client;

import com.mostafa.test.mockito.User;
import com.mostafa.test.mockito.spring.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserManager {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserDao userDao;

    public boolean addUser(String username, String password, User newUser){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");

        ResponseEntity<String> exchange = restTemplate.exchange("https://localhost:7443/user/authenticate",
                HttpMethod.POST, new HttpEntity<User>(new User() {{
                    setUsername(username);
                    setPassword(password);
                }}, httpHeaders)
                , String.class);
        if(exchange.getBody().equals("true")){
            userDao.save(newUser);
            System.out.println("we are allowed to add user");
        }
        return true;
    }
}
