package app;

import ocsf.client.*;
import java.io.*;
import java.net.SocketException;

/**
 * class responsible for establishing the connection and recieving the response first.
 * @author Amir
 *
 */
public class HSClient extends AbstractClient
{

  //Constructors ****************************************************


/**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   */
  
  public HSClient(String host, int port) throws IOException 
  {
    super(host, port); //Call the superclass constructor
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
	  Main.onResponse(msg);
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
    	sendToServer(message);
    }
    catch(SocketException e){
    	Main.onResponse("Could not send message to server. Terminating client.");
    	Main.signalDisconnection();
    }
    catch(IOException e)
    {
    	Main.onResponse("Could not send message to server. Terminating client.");
    	Main.signalDisconnection();
    }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  
  
  
}
//End of ChatClient class
