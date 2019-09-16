package com.mostafa.test.mockito;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String username;
    private String password;
    private String code;
    private LocalDate registerDate;

    public User() {
    }

    public User(long id, String username, String password, String code, LocalDate registerDate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.code = code;
        this.registerDate = registerDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDate getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDate registerDate) {
        this.registerDate = registerDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='****'" +
                ", code='" + code + '\'' +
                ", registerDate=" + registerDate +
                '}';
    }
}
