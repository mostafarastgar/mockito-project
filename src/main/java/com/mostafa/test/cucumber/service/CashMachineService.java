package com.mostafa.test.cucumber.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class CashMachineService {
    @Autowired
    private Account account;

    private int machineCashAmount;
    private boolean lockCard;

    public void initCash(int machineCashAmount) {
        this.machineCashAmount = machineCashAmount;
    }

    public boolean insertCard(int accountBalance, String cardNo) {
        try {
            lockCard = true;
            account.init(accountBalance, cardNo);
            return true;
        } catch (Exception e) {
            lockCard = false;
            return false;
        }
    }

    public boolean requestForMoney(int requestAmount) {
        boolean finalResult = false;
        if (machineCashAmount > requestAmount) {
            if (account.withdraw(requestAmount)) {
                machineCashAmount = machineCashAmount - requestAmount;
                finalResult = true;
            } else {
                account.deposit(requestAmount);
            }
        }
        lockCard = false;
        return finalResult;
    }

    public int getMachineCashAmount() {
        return machineCashAmount;
    }

    public Account getAccount() {
        return account;
    }

    public boolean isLockCard() {
        return lockCard;
    }
}
