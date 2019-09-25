package com.mostafa.test.cucumber;

import com.mostafa.test.cucumber.repository.CardRepository;
import com.mostafa.test.cucumber.service.CashMachineService;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class CashMachineStepsDefs {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CashMachineService cashMachineService;

    private int accountBalance;
    private int lastMachineCashAmount;

    public CashMachineStepsDefs() {
        MockitoAnnotations.initMocks(this);
    }

    @Given("^the account balance is (\\d+)$")
    public void initAccountBalance(int accountBalance) {
        this.accountBalance = accountBalance;
    }

    @And("^the card (.+) is valid$")
    public void initCardNo(String cardNumber) throws Exception {
        when(cardRepository.getAllValidCards()).thenReturn(new HashSet<>(){{
            add(cardNumber);
        }});
        assertTrue(this.cashMachineService.insertCard(accountBalance, cardNumber));
        assertTrue(this.cashMachineService.isLockCard());
    }

    @And("^the machine has (\\d+) money$")
    public void initMachineCashAmount(int machineCashAmount) {
        this.cashMachineService.initCash(machineCashAmount);
    }

    @When("^the account holder requests (\\d+)$")
    public void requestForWithdraw(int requestAmount) throws Throwable {
        this.lastMachineCashAmount = cashMachineService.getMachineCashAmount();
        assertTrue(cashMachineService.requestForMoney(requestAmount));
    }

    @Then("^the cashpoint should dispense (\\d+)$")
    public void checkDispenseAmount(int dispenseAmount) {
        assertEquals(lastMachineCashAmount, cashMachineService.getMachineCashAmount() + dispenseAmount);
    }

    @And("^the account balance should be (\\d+)$")
    public void checkAccountBalance(int finalAccountBalance) {
        assertEquals(finalAccountBalance, cashMachineService.getAccount().getAccountBalance());
    }

    @And("^the card should be returned$")
    public void checkCard4BeingReleased() {
        assertFalse(cashMachineService.isLockCard());
    }
}
