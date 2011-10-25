package com.akqa.test.henrikpettersen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads an input file, and creates a list of sessions (with actions) configured
 * with this input file.
 * 
 * The first line is the total cash held in the ATM followed by a blank line. 
 * The remaining input represents zero or more user sessions. Each session consists of: 
 *  - The user's account number, correct PIN and the PIN they actually entered. 
 *    These are separated by spaces.
 *  - Then, on a new line, the customer's current balance and overdraft facility.
 *  - Then, one or more transactions, each on a separate line. These can be 
 *    one of the following types: 
 *     + Balance inquiries, represented by the operation code B.
 *     + Cash withdrawals, represented by the operation code W followed by an amount.
 *  - A blank line marks the end of a user session.
 * 
 */
public class InputFileReader {
    private long atmBalance;
    private List<Session> sessions = new ArrayList<Session>();
    private String filename;
    
    public InputFileReader(String filename){
        this.filename = filename;
    }

    public long getAtmBalance(){
        return atmBalance;
    }
    
    public List<Session> getSessions(){
        return sessions;
    }
    
    /**
     * Parses the input file according to the rules above. We have also added support
     * for comments in the input file, so every line starting with '#' is ignored
     * @return A list of session instances configured with the data from the input file
     */
    public List<Session> readFile(){
        
        InputStream is = Atm.class.getResourceAsStream(filename);
        
        //File file = new File(filename);
        List<String> lines = new ArrayList<String>();
        
        //Read in the entire file, and create a list of all the lines in this file
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.trim());
              }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        //Assertion: the content of the configuration file is now mirrored in the 
        //'lines' list variable
        
        //Check to see if the file was empty
        if (lines.size() == 0){
            System.out.println("Error parsing input file");
            System.exit(-1);
        }
        
        //Skip any comments at the top of the file
        int lineIndex = skipComments(0, lines);
        
        //The first information in a file is the atm balance
        atmBalance = Long.parseLong(lines.get(lineIndex));
        
        //Skip to the next line, we should be blank, then skip onto the first 
        //session data
        lineIndex = increaseLineIndex(lineIndex, lines);
        lineIndex = increaseLineIndex(lineIndex, lines);
        
        //Loop around until we get to the end of the file
        while (lineIndex < lines.size()){
            Session session = new Session();
            
            //Account number and PIN
            String[] acctElements = lines.get(lineIndex).split("\\s");
            
            //Make sure that we have:
            //{account number} {expected pin} {entered pin}
            assert acctElements.length == 3;
            
            session.setAccountNumber(acctElements[0]);
            session.setExpectedPin(acctElements[1]);
            session.setEnteredPin(acctElements[2]);
            
            lineIndex = increaseLineIndex(lineIndex, lines);
            
            //Available Funds
            String[] fundsElements = lines.get(lineIndex).split("\\s");
            
            //Make sure that we have:
            //{account balance} {overdraft}
            assert fundsElements.length == 2;
            
            session.setBalance(Long.parseLong(fundsElements[0]));
            session.setOverdraft(Long.parseLong(fundsElements[1]));
            
            lineIndex = increaseLineIndex(lineIndex, lines);
            
            //A session can have multiple actions associated with it. 
            //Loop through all of these actions. Stop when we get to 
            //the end of the file, or a zero length line
            while ((lines.size() > lineIndex) && (!lines.get(lineIndex).isEmpty())){
                String[] wdrElements = lines.get(lineIndex).split("\\s");
                
                Action action = Action.createAction(wdrElements[0]);
                if (action.isWithdrawal()){
                    action.setAmount(Long.parseLong(wdrElements[1]));
                }
                session.addAction(action);
                
                lineIndex = increaseLineIndex(lineIndex, lines);
            }
            lineIndex = increaseLineIndex(lineIndex, lines);
            sessions.add(session);
        }
        return sessions;
    }
    
    /**
     * Increases the line index by one or more, skiping over any comments
     * @param lineIndex the current lineindex
     * @param lines what we are indexing
     * @return the new line index
     */
    private int increaseLineIndex(int lineIndex, List<String> lines){
        lineIndex++;
        lineIndex = skipComments(lineIndex, lines);
        return lineIndex;
    }

    private int skipComments(int lineIndex, List<String> lines) {
        while ((lineIndex < lines.size()) && (lines.get(lineIndex).length() > 0) && (lines.get(lineIndex).startsWith("#"))){
            lineIndex++;
        }
        return lineIndex;
    }
}
