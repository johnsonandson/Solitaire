package resources;

//John Speer
//4/10/26
// Server to handle messages sent by clients and send them to every other client.
import java.util.ArrayList;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.EOFException;

/**
 * This program is a server that takes connection requests on
 * the port specified by the constant LISTENING_PORT.  When a
 * connection is opened, the program should allow the client to send it messages. The messages should then 
 * become visible to all other clients.  The program will continue to receive
 * and process connections until it is killed (by a CONTROL-C,
 * for example). 
 * 
 * This version of the program creates a new thread for
 * every connection request.
 */



//accepts connection then listen for a message from the client any time
    //you get one you swap hands and send the state to the other client.

public class ChatServerWithThreads {

    public static final int LISTENING_PORT = 9876;
//pre condition: chat server is initialized
//post condition: server listens to port

    //pre condition: chat is initialized
    //Post condition: server records the port and shuts down when hit with an error or disconnect
    public static void main(String[] args) {

        ServerSocket listener;
        Socket connection;
        try{
            listener=new ServerSocket(LISTENING_PORT);
            System.out.println("Listening on port "+LISTENING_PORT);
            while(true){
                connection=listener.accept();
                ConnectionHandler h=new ConnectionHandler(connection);
                h.start();

            }
        }
        catch(Exception e){
            System.out.println("Server down"+"\n"+"error"+e);
            return;
        }
    }  // end main()
    


    /**
     *  Defines a thread that handles the connection with one
     *  client.
     */

    //pre conditon: socket is initialized
    //post condition: Initialize ConnectionHandler to set streams and add handlers
    private static class ConnectionHandler extends Thread {
        private static volatile ArrayList<ConnectionHandler> handlers;
        Socket client;
        ObjectInputStream ois;
        ObjectOutputStream oos;
        
        
        ConnectionHandler(Socket socket) {
            client = socket;
            if (handlers==null){
                handlers=new ArrayList<ConnectionHandler>();
            }
            handlers.add(this);
            try {
                oos=new ObjectOutputStream(client.getOutputStream());
                ois=new ObjectInputStream(client.getInputStream());
            }
            catch(Exception e){
                
            }

        }

        //pre condition: server is initialized
        //post condition: sends client messages back and forth until disconnection
        public void run() {
            String clientAddress = client.getInetAddress().toString();
            while(true) {
	            try {
	            	//your code to send messages goes here.
                    String message=(String)ois.readObject();
                    if(!(message.equals("disconnect")))
                        System.out.println(message);
                    else{
                        System.out.println("closing");
                        break;
                    }
                    for(ConnectionHandler h:handlers){
                        h.oos.writeObject(message);
                        h.oos.flush();
                    }
	            }
	            catch (EOFException e){
	                System.out.println("Disconnected client");
                    handlers.remove(this);
                    break;
	            }
                catch(Exception e){
                    System.out.println("Error on connection with: " + clientAddress + ": " + e);
                }
            }
        }
    }


}
