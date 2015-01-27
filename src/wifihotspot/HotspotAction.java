/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//For adding resetting features, go to "http://www.makeuseof.com/answers/share-wireless-internet-connection-windows-7/"


package wifihotspot;

import java.io.BufferedInputStream;
import java.io.*;


/**
 *
 * @author Manash
 */
public class HotspotAction {
    
    public String password;
    public String SSID;
    
    
    public String outputString;
    
    public String executeCommand(String command)
    {
        StringBuffer output = new StringBuffer();
        Process processCommand;
        
        try
        {
           
           
            
            processCommand = Runtime.getRuntime().exec(command);
            processCommand.waitFor();
            String line = "";
     
            BufferedReader reader = new BufferedReader(new InputStreamReader(processCommand.getInputStream()));
            while ((line = reader.readLine()) != null){
                output.append(line + "\n");
                
            }
            
            outputString = output.toString();
            
        } catch (Exception ex){
            ex.printStackTrace();
        }
        
        return outputString;
        
    }
    
}
