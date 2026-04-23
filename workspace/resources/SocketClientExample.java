//John Speer
//4/10/26
//Client to send messages to and from the server and client
package resources;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.*;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent; 
public class SocketClientExample {
	
	
	/*
	 * Modify this example so that it opens a dialogue window using java swing, 
	 * takes in a user message and sends it
	 * to the server. The server should output the message back to all connected clients
	 * (you should see your own message pop up in your client as well when you send it!).
	 *  We will build on this project in the future to make a full fledged server based game,
	 *  so make sure you can read your code later! Use good programming practices.
	 *  ****HINT**** you may wish to have a thread be in charge of sending information 
	 *  and another thread in charge of receiving information.
	*/
    //pre condition : socket client is initialized
    //post condition: shows the GUI and connects to a port to recieve messages
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{
        
        InetAddress host = InetAddress.getLocalHost();
        Socket socket = new Socket(host.getHostName(),9876);
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());


            
            System.out.println("Sending request to Socket Server");
            ois = new ObjectInputStream(socket.getInputStream());
            
            //close resources
            ois.close();
            oos.close();
            Thread.sleep(100);
        
        JFrame frame=new JFrame("TextBoxes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300,300);
        frame.setLayout(new FlowLayout());
        JTextField text1=new JTextField(100);
        JTextField text2=new JTextField(100);
        text1.addActionListener(new ActionListener() {

            //pre condition: the enter key is inputted
            //post condition: text of text1 is sent to the server
            @Override
            public void actionPerformed(ActionEvent e){
            String line=text1.getText();
            //establish socket connection to server
            try{
                oos.writeObject(line);
                oos.flush();}
            catch(IOException e1){
                e1.printStackTrace();
            }
                
            }
            
        });
        frame.add(text1);
        frame.add(text2);
        
         // Your logic here
        frame.setVisible(true);
        //get the localhost IP address, if server is running on some other IP, you need to use that
        
        Scanner input=new Scanner(System.in);
  
        while(true){
            text2.setText(text2.getText()+"\n\n"+ois.readObject());

        }
            
        
    }
}
