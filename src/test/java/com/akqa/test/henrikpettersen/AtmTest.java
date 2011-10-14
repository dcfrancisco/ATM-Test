package com.akqa.test.henrikpettersen;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class AtmTest {

    @Test
    public void testInsufficientAtmBalance() {
        Session session = new Session();
        Atm atm = new Atm();
        atm.setAtmBalance(1000);
        
        session.setAccountNumber("123456");
        session.setBalance(1500);
        session.setOverdraft(100);
        session.setEnteredPin("1234");
        session.setExpectedPin("1234");
        
        Action action = Action.createAction("W");
        action.setAmount(1500);
        List<Action> actions = new ArrayList<Action>();
        actions.add(action);
        
        session.setActions(actions);
        
        try {
            performActions(session, atm);
        } catch (AtmException e) {
            assertEquals(ErrorCode.ATM_ERR, e.getErrorCode());
        }
    }
    
    @Test
    public void testInsufficientFundsBalance() {
        Session session = new Session();
        Atm atm = new Atm();
        atm.setAtmBalance(1000);
        
        session.setAccountNumber("123456");
        session.setBalance(500);
        session.setOverdraft(100);
        session.setEnteredPin("1234");
        session.setExpectedPin("1234");
        
        Action action = Action.createAction("W");
        action.setAmount(700);
        List<Action> actions = new ArrayList<Action>();
        actions.add(action);
        
        session.setActions(actions);
        
        try {
            performActions(session, atm);
        } catch (AtmException e) {
            assertEquals(ErrorCode.FUNDS_ERR, e.getErrorCode());
        }
    }
    
    @Test
    public void testNonMatchingPin() {
        Session session = new Session();
        Atm atm = new Atm();
        atm.setAtmBalance(1000);
        
        session.setAccountNumber("123456");
        session.setBalance(500);
        session.setOverdraft(100);
        session.setEnteredPin("1235");
        session.setExpectedPin("1234");
        
        Action action = Action.createAction("W");
        action.setAmount(700);
        List<Action> actions = new ArrayList<Action>();
        actions.add(action);
        
        session.setActions(actions);
        
        try {
            performActions(session, atm);
        } catch (AtmException e) {
            assertEquals(ErrorCode.ACCOUNT_ERR, e.getErrorCode());
        }
    }
    
    private void performActions(Session session, Atm atm) throws AtmException{
        atm.verifyPin(session);
        for (Action action : session.getActions()){
            if (action.isBalanceEnquiry()){
                atm.displayBalance(session);
            }
            else if (action.isWithdrawal()){
                atm.withdrawFundsAndDisplayBalance(session, action);
            }
            else{
                throw new RuntimeException("Action is not supported in ATM! This should never happen.");
            }
        }        
    }
}
