package com.akqa.test.henrikpettersen;

import java.util.List;

/**
 * You are tasked with developing software for an ATM machine. The software is responsible 
 * for validating customer account details and performing basic operations including balance 
 * inquiries and cash withdraws.
 * 
 * A third party is developing the UI and will provide data to the application in an agreed 
 * format defined below. The application should receive the data, process the operations and 
 * then output the results in the required format also defined below. For the purposes of the 
 * test you are free to implement any mechanism for feeding input into your solution. You 
 * should provide sufficient evidence that your solution is complete by, as a minimum, 
 * indicating that it works correctly against the supplied test data.
 * 
 * The solution should meet the following business requirements:
 *  - The ATM cannot dispense more money than it holds.
 *  - The customer cannot withdraw more funds then they have access to.
 *  - The ATM should not dispense funds if the pin is incorrect.
 *  - The ATM should not expose the customer balance if the pin is incorrect.
 *  - The ATM should only dispense the exact amounts requested.
 *
 * @author henrikpettersen
 */
public class Atm {
    
    //Contains the information on the atm session we want to run through this atm
    private static final String INPUT_FILE_VAR_NAME = "input.file";
    
    //The amount of money available to the ATM
    private static long atmBalance;
    
    public long getAtmBalance(){
        return atmBalance;
    }
    
    public void setAtmBalance(long atmBalance){
        this.atmBalance = atmBalance;
    }
    
    public static void main( String[] args )
    {
        //Read in all the session configurations first
        InputFileReader myFileReader = new InputFileReader(System.getenv(INPUT_FILE_VAR_NAME));
        myFileReader.readFile();
        
        atmBalance = myFileReader.getAtmBalance();        
        //printDebug(myFileReader.getSessions());
        
        //Run each session from the input file through the ATM
        for (Session session : myFileReader.getSessions()){
            performActions(session);
        }
    }
    
    /**
     * Each session has a set of actions (Withdrawal, Balance Inquiry) that needs
     * to be run on this ATM. For a single session, this method will execute all
     * its associated actions
     * @param session One ATM operation by a single customer
     */
    private static void performActions(Session session){
        try {
            verifyPin(session);
            for (Action action : session.getActions()){
                try {
                    if (action.isBalanceEnquiry()){
                        displayBalance(session);
                    }
                    else if (action.isWithdrawal()){
                        withdrawFundsAndDisplayBalance(session, action);
                    }
                    else{
                        throw new RuntimeException("Action is not supported in ATM! This should never happen.");
                    }
                } catch (AtmException e) {
                    System.out.println(e.getErrorCode());
                }
            }
        } catch (AtmException e) {
            System.out.println(e.getErrorCode());
        }
    }
    
    /**
     * Available funds = current account balance + overdraft facility
     * @param session One ATM operation by a single customer
     */
    private static long getAvailableFunds(Session session){
        return session.getBalance() + session.getOverdraft();
    }
    
    /**
     * Compares the user entered pin, to the pin associated with the account
     * @param session One ATM operation by a single customer
     * @throws AtmException If expected pin != entered pin
     */
    public static void verifyPin(Session session) throws AtmException{
        if (!session.getExpectedPin().equals(session.getEnteredPin())){
            throw new AtmException(ErrorCode.ACCOUNT_ERR);
        }
    }

    /**
     * Withdraws money from the session's account, and displays the new balance.
     * First checks to see if the funds are available in the account, using both the 
     * account balance and the overdraft facility. Also checks that the atm has 
     * enough funds available to dispense. 
     *   
     * @param session One ATM operation by a single customer
     * @param action A withdrawal action, contains the amount to withdraw
     * @throws AtmException If the ATM or account does not have enough funds available
     */
    public static void withdrawFundsAndDisplayBalance(Session session, Action action) throws AtmException{
        //Check to see if the ATM has enough funds to dispense
        if(action.getAmount() > atmBalance){
            throw new AtmException(ErrorCode.ATM_ERR);
        }
        
        //Check to see if the session account balance has enough funds available
        if (getAvailableFunds(session) < action.getAmount()){
            throw new AtmException(ErrorCode.FUNDS_ERR);
        }
        
        //Assertion: We have enough money in the ATM and the session account to perform
        //this withdrawal
     
        //Update the balance of the ATM
        atmBalance -= action.getAmount();
        
        //Get the new session account balance
        long newBalance = session.getBalance() - action.getAmount();
        
        //If the new session account balance is now negative, 
        //we need to dip into the overdraft
        if (newBalance < 0){
            session.setOverdraft(session.getOverdraft() + newBalance);
        }
        
        //Update the account balance
        session.setBalance(newBalance);
        
        //Print the new account balance
        displayBalance(session);
    }
    
    /**
     * Prints the account balance
     * @param session One ATM operation by a single customer
     */
    public static void displayBalance(Session session){
        System.out.println(session.getBalance());
    }
    
    /**
     * Prints the information on the sessions that have been read in from the
     * input file. Only used for debugging purposes
     * @param myFileReader Contains the sessions allready read in
     */
    private static void printDebug(List<Session> sessions) {
        System.out.println("**************************");
        for (Session session : sessions){
            System.out.println(session);
            System.out.println("**************************");
        }   
        System.out.println("Output:");
    }
}