package com.akqa.test.henrikpettersen;

import java.util.ArrayList;
import java.util.List;

/**
 * POJO representing a single user session with the ATM. Contains:
 * - Accout number
 * - Expected PIN
 * - Entered PIN
 * - Account balance
 * - Overdraft facility
 * - A set of actions to perform on the account (withdrawal or balance inquiry)
 */
public class Session {
    private String accountNumber;
    private String expectedPin;
    private String enteredPin;
    private long balance;
    private long overdraft;
    private List<Action> actions = new ArrayList<Action>();
    
    public String getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    public String getExpectedPin() {
        return expectedPin;
    }
    public void setExpectedPin(String expectedPin) {
        this.expectedPin = expectedPin;
    }
    public String getEnteredPin() {
        return enteredPin;
    }
    public void setEnteredPin(String enteredPin) {
        this.enteredPin = enteredPin;
    }
    public long getBalance() {
        return balance;
    }
    public void setBalance(long balance) {
        this.balance = balance;
    }
    public long getOverdraft() {
        return overdraft;
    }
    public void setOverdraft(long overdraft) {
        this.overdraft = overdraft;
    }
    public List<Action> getActions() {
        return actions;
    }
    public void setActions(List<Action> actions) {
        this.actions = actions;
    }
    public void addAction(Action action){
        actions.add(action);
    }
    
    public String toString(){
        return "Account Number:\t" + accountNumber + "\n" +
                "Expected Pin:\t" + expectedPin + "\n" +
                "Entered Pin:\t" + enteredPin + "\n" +
                "Balance:\t\t" + balance + "\n" +
                "Overdraft:\t" + overdraft + "\n" +
                "Actions:\t\t" + actions.toString();
    }
    
}
