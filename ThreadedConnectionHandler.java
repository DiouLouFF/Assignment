
// The Connection Handler Class - Written by Derek Molloy for the EE402 Module

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/*Class which is linking the server (on the BeagleBone) and the client (the user on the PC). 
  One ConnectionHandler represents one client.The ConnectionHandler is called by the server 
  to send and receive messages to/from the client. 
 */
public class ThreadedConnectionHandler extends Thread
{
    private Socket clientSocket = null;				// Client socket object
    private ObjectInputStream is = null;			// Input stream
    private ObjectOutputStream os = null;			// Output stream
    private Requests command;
    private TimeDateService theDateService;
    private TemperatureService theTemperatureService;
   
  
    
    /* The constructor for the connection handler
       It receives different parameters:
       -clientSocket --> passed from the server
	   -theDateService --> to get the date of the samples
	   -theTemperatureService --> to get the temperature
     */
    public ThreadedConnectionHandler(Socket clientSocket,TimeDateService theDateService,TemperatureService theTemperatureService) {
    	 
    	this.clientSocket = clientSocket;
        this.theDateService = theDateService;
        this.theTemperatureService = theTemperatureService;
       
      
    }

 // Will eventually be the thread execution method - can't pass the exception back
    public void run() 
    {
         try {
            this.is = new ObjectInputStream(clientSocket.getInputStream());
            this.os = new ObjectOutputStream(clientSocket.getOutputStream());
            while (this.readCommand()) {}
         } 
         catch (IOException e) 
         {
        	System.out.println("XX. There was a problem with " + "the Input/Output Communication:");
            e.printStackTrace();
         }
         
    }

    // Receive and process incoming string commands from client socket 
    private boolean readCommand() 
    {
    	try {
            this.command = (Requests)is.readObject();
        } 
        catch (Exception e) {    
        	this.closeSocket();
            return false;    //boolean to stop the reading if false is returned
        }
    	
        System.out.println("01. <- Received a String object from"
        					+ " the client (" + command + ").");

        switch (this.command.getcomLine())
        {
        	case "getTemp": this.getTemp();
        					send(this.command);
        					break;
        	
        	default: this.sendError("Invalid command: " + command);
	        		break;
        }
      
        return true;
    }
    
    
    // Permit to get the date and the temperature. Assign it to this command.
    private void getTemp() {	
    	this.command.getSample().setTemperature(theTemperatureService.getTemperature());
    	this.command.getSample().setDate(this.theDateService.getDateAndTime());
    }
    
    // Send a generic object back to the client 
     private void send(Object o) {
        try {
            System.out.println("02. -> Sending (" + o +") to the client.");
            this.os.writeObject(o);
            this.os.flush();
        } 
        catch (Exception e) {
            System.out.println("XX." + e.getStackTrace());
        }
    }

  // Send a pre-formatted error message to the client 
    public void sendError(String message) { 
        this.send("Error:" + message);	
    }
    
    //Close the client socket 
 
    public void closeSocket() { 
        try {
            this.os.close();
            this.is.close();
            this.clientSocket.close();
        } 
        catch (Exception e) {
            System.out.println("XX. " + e.getStackTrace());
        }
    }
    
}
