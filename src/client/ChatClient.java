package client;

import java.io.IOException;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;
import util.Constants;

public class ChatClient {
    private Socket socket;
    private DataOutputStream output;
    private Scanner scanner;
    
    public ChatClient(){
        try {
            socket=new Socket("localhost",Constants.PORT);
            ClientListener listener =new ClientListener(socket);
            listener.start();
            output=new DataOutputStream(socket.getOutputStream());
            scanner=new Scanner(System.in);
            
            System.out.println("Connected to ChatSphere Server!");
            System.out.println("Type a message: ");

            while (true) {
                String message=scanner.nextLine();
                output.writeUTF(message);
                output.flush();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new ChatClient();
    }
}
