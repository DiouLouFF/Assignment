
/* The Date Server Class - Written by Derek Molloy for the EE402 Module
 * See: ee402.eeng.dcu.ie
 */



import java.net.*;
import java.io.*;


/*
The ThreadedServer class is the main class of the server. It calls the ThreadedConnectionHandler that handle one client as 
a thread which means that it can handle multiple clients.
*/
public class ThreadedServer 
{
	private static int portNumber = 5050;
	
	/* The main method declares the Services needed (i.e. Temperature,...)
	   It permits to connect the different clients and then redirects them to a thread (the server continues to wait for other clients).
	 */
	public static void main(String args[]) {
		
		boolean listening = true;
        ServerSocket serverSocket = null;
    

		
        
        // Set up the Server Socket
        try 
        {
            serverSocket = new ServerSocket(portNumber);
            System.out.println("New Server has started listening on port: " + portNumber );
        } 
        catch (IOException e) 
        {
            System.out.println("Cannot listen on port: " + portNumber + ", Exception: " + e);
            System.exit(1);
        }
        
        TimeDateService theDateService = new TimeDateService();
		TemperatureService theTemperatureService = new TemperatureService();
		
		//In that loop, we search to check the temperature.
	    Loop loop = new Loop(10000, theTemperatureService);
	    loop.start();
		
        // Server is now listening for connections or would not get to this point
        while (listening) // almost infinite loop - loop once for each client request
        {
            Socket clientSocket = null;
            try {
            	System.out.println("**. Listening for a connection...");
                clientSocket = serverSocket.accept();
                System.out.println("00. <- Accepted socket connection from a client: ");
                System.out.println("    <- with address: " + clientSocket.getInetAddress().toString());
                System.out.println("    <- and port number: " + clientSocket.getPort());
            } 
            catch (IOException e) {
                System.out.println("XX. Accept failed: " + portNumber + e);
                listening = false;   
            }	
    
	        ThreadedConnectionHandler newcli = new ThreadedConnectionHandler(clientSocket, theDateService, theTemperatureService);
	        newcli.start();

            System.out.println("02. -- Finished communicating with client:" + clientSocket.getInetAddress().toString());
        }
        // Server is no longer listening for client connections - time to shut down.
        try 
        {
            System.out.println("04. -- Closing down the server socket gracefully.");
            serverSocket.close();
        } 
        catch (IOException e) 
        {
            System.err.println("XX. Could not close server socket. " + e.getMessage());
        }
    }
}
