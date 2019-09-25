package com.mostafa.test.cucumber.service;

import com.mostafa.test.cucumber.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Scope("prototype")
public class Account {
    private int accountBalance;
    private String cardNo;

    @Autowired
    CardRepository cardRepository;

    public void init(int accountBalance, String cardNo) throws Exception {
        this.accountBalance = accountBalance;
        this.cardNo = cardNo;
        if (!check4validCardNo()) {
            throw new Exception("invalid card number");
        }
    }

    public int getAccountBalance() {
        return accountBalance;
    }

    public boolean withdraw(int amount) {
        if (accountBalance > amount) {
            accountBalance = accountBalance - amount;
            return true;
        }
        return false;
    }

    public void deposit(int amount) {
        accountBalance = accountBalance + amount;
    }

    private boolean check4validCardNo() {
        return cardRepository.getAllValidCards().contains(cardNo);
    }
}
