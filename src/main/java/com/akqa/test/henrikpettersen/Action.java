package com.akqa.test.henrikpettersen;

/**
 * Represents an action taken during a single atm session. There are 2 types of actions:
 * W: Withdrawal
 * B: Balance Inquiry
 */
public class Action {

    /**
     * There are 2 types of actions, represented by this enum:
     * W: Withdrawal
     * B: Balance Inquiry
     */
    private enum Type{
        WIHTDRAWAL ("W"), 
        BALANCE_ENQUIRY("B");
        
        private String code;
        public String getCode(){return code;}
        
        private Type(String code){
            this.code = code;
        }
        
        public String toString(){
            return code;
        }
        
        /**
         * Returns an action type based on a key:
         * W: Withdrawal
         * B: Balance Inquiry
         * @param text 'W' or 'B'
         * @return An action type corresponding to the text passed in
         */
        public static Type fromString(String text) {
            if (text != null) {
              for (Type t : Type.values()) {
                if (text.equalsIgnoreCase(t.code)) {
                  return t;
                }
              }
            }
            return null;
        }
    }
    
    private Type type;
    private long amount;

    public Action(Action.Type type){
        this.type = type;
    }
    
    /**
     * Only used for withdrawal, not a balance inquiry
     * @param amount
     */
    public void setAmount(long amount){
        this.amount = amount;
    }
    
    /**
     * Returns a new Action instance, with the type set by the key
     * W: Withdrawal
     * B: Balance Inquiry
     * @param key The action type, 'W' or 'B'
     * @return an Action instance with the given action type
     */
    public static Action createAction(String key){
        Action.Type type = Action.Type.fromString(key);
        return new Action(type);
    }
    
    public Type getType() {
        return type;
    }
    public long getAmount() {
        return amount;
    }
    
    public boolean isWithdrawal(){
        return type == Type.WIHTDRAWAL;
    }
    
    public boolean isBalanceEnquiry(){
        return type == Type.BALANCE_ENQUIRY;
    }
    
    public String toString(){
        if (type == Type.BALANCE_ENQUIRY){
            return type.toString();
        }
        else if (type == Type.WIHTDRAWAL){
            return type + " " + amount;
        }
        else{
            return "Error: Type not recognised";
        }
    }
}
