package com.akqa.test.henrikpettersen;

/**
 * For each customer operation the solution should respond with the remaining 
 * customer balance, or ACCOUNT_ERR if the account details could not be validated. 
 * If funds aren't available for cash withdraw the required response is FUNDS_ERR. 
 * If the ATM is out of cash the required response is ATM_ERR.
 */
public enum ErrorCode {
    ACCOUNT_ERR, FUNDS_ERR, ATM_ERR
}
