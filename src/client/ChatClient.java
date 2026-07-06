package client;

import java.io.IOException;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.Socket;
import java.util.Scanner;
import util.Constants;
import util.MessageType;

public class ChatClient {
    private Socket socket;
    private DataOutputStream output;
    private DataInputStream input;
    private Scanner scanner;
    
    public ChatClient(){
        try {
            socket=new Socket("localhost",Constants.PORT);
            output=new DataOutputStream(socket.getOutputStream());
            input=new DataInputStream(socket.getInputStream());
            scanner=new Scanner(System.in);
            
            while (true) {
                System.out.print("Enter username: ");
                String username=scanner.nextLine();

                output.writeUTF(MessageType.LOGIN + "|" + username);
                output.flush();

                String response = input.readUTF();
                if (response.equals(MessageType.OK)) {
                    break;
                }

                String[] parts = response.split("\\|", 2);
                System.out.println(parts[1]);
            }

            System.out.println("Connected to ChatSphere Server!");
            
            ClientListener listener = new ClientListener(socket);
            listener.start();
            
            System.out.println("Type a message: ");

            while (true) {
                String message=scanner.nextLine();
                output.writeUTF(MessageType.CHAT + "|" + message);
                output.flush();
            }
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new ChatClient();
    }
}
