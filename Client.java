/* The Client Class - Written by Derek Molloy for the EE402 Module
 * See: ee402.eeng.dcu.ie
 * 
 * 
 */



import java.net.*;
import java.io.*;
import java.util.*;
/*
 Client is a class that represents the client (the user on the PC) in the context of a Client/Server application. 
 This class manages the connections to the server, the different sending and reception of the datas.
 */
public class Client {
	
	private static int portNumber = 5050;
    private Socket socket = null;
    private ObjectOutputStream os = null;
    private ObjectInputStream is = null;
    
    private ClientGUI cgui;
    private Requests clientCommand;
    private ArrayList<Interactions> historical;
    private boolean isConnected = false;
    private ClientLoop lc =null;
    
 /*The constructor expects the IP address of the server - the port is fixed.
   There are 2 parameters:
   -serverIP String which is the IP of the server (i.e XXX.XXX.X.X)
   -cgui representing the ClientGUI 
     */
    public Client(String serverIP, ClientGUI cgui) {
    	this(serverIP);
    	this.cgui = cgui;
    }
	
    // Second constructor of the previous class that receives only one parameter (the IP address)

    public Client(String serverIP) {
    	this.cgui = cgui;
    	this.clientCommand = new Requests();
    	this.historical = new ArrayList<Interactions>();
    	if (!connectToServer(serverIP)) {
    		System.out.println("XX. Failed to open socket connection to: " + serverIP);            
    	} else this.isConnected = true;
    }
    
    //The following method creates the connection between the client and the server.
     
    private boolean connectToServer(String serverIP) {
    	try {  
    		System.out.println("Please wait for connection...");
    		this.socket = new Socket(serverIP,portNumber);
    		this.os = new ObjectOutputStream(this.socket.getOutputStream());
    		this.is = new ObjectInputStream(this.socket.getInputStream());
    		System.out.println("00. -> Connected to Server:" + this.socket.getInetAddress() + " on port: " + this.socket.getPort());
    		System.out.println("    -> from local address: " + this.socket.getLocalAddress() + " and port: " + this.socket.getLocalPort());
    	} 
        catch (Exception e) {
        	System.out.println("XX. Failed to Connect to the Server at port: " + portNumber);
        	System.out.println("    Exception: " + e.toString());	
        	return false;
        }
		return true;
    }
    
    /*
      Method to send a request to server and receive a response
      If a Command object is passed to the method it means that the request
      wants to send data to the server
*/
   public synchronized Requests request(String str, Requests c) {
    	Requests cx;
    	if (c == null)
    		cx = new Requests(str);
    	else {
    		cx = c;
    		cx.setcomLine(str);
    	}
    	
    	System.out.println("01. -> Sending Command (" + cx + ") to the server...");
    	
    	this.send(cx);
    	try {
    		cx = (Requests)receive();
    		
    		System.out.println("05. <- The Server responded with: ");
    		System.out.println("    <- " + cx);
    	} catch (Exception e) {
    		System.out.println("XX. There was an invalid object sent back from the server");
    	}	

    	return cx;
    }
	
    //Override of the request function 
    public Requests request(String str) {
    	return request(str,null);
    }
    
    //To send generic objects
    private void send(Object o) {
		try {
		    System.out.println("02. -> Sending an object...");
		    os.writeObject(o);
		    os.flush();
		} 
	    catch (Exception e) {
		    System.out.println("XX. Exception Occurred on Sending:" +  e.toString());
		}
    }

    // Method to receive generic objects
 
    private Object receive() 
    {
		Object o = null;
		try {
			System.out.println("03. -- About to receive an object...");
		    o = is.readObject();
		    System.out.println("04. <- Object received...");
		} 
	    catch (Exception e) {
		    System.out.println("XX. Exception Occurred on Receiving:" + e.toString());
		}
		return o;
    }
    
    //To get the command attribute
    public Requests getClientCommand()
    {
    	return this.clientCommand;
    }
    
    public void setClientCommand(Requests command)
    {
    	this.clientCommand = command;
    }
    
    //To get the clientGUI
    public ClientGUI getClientGUI()
    {
    	return this.cgui;
    }
    
    //To get the LoopClient
    public ClientLoop getClientLoop()
    {
    	return this.lc;
    }
    
    //To set the LoopCLient
    public void setClientLoop(ClientLoop lc)
    {
    	this.lc = lc;
    }
    
    //To check if the connection went well
    public boolean isConnected()
    {
    	return isConnected;
    }

    //To get the Arraylist with the last 10 samples
    public ArrayList<Interactions> getHistorical()
    {
    	return this.historical;
    }
    
    //To get the Arraylist size
    public int getSizeHistorical()
    {
    	return historical.size();
    }
}
