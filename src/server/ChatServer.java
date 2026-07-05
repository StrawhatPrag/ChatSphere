package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import util.Constants;

public class ChatServer {
    public static void main(String args[]){
        try {
            ServerSocket serverSocket=new ServerSocket(Constants.PORT);
            
            String message="Chat Server Started\nListening on port ";
            System.out.println("*******************************");
            System.out.println(message+Constants.PORT);
            System.out.println("*******************************");

            while(true){
                Socket clientSocket=serverSocket.accept();
                System.out.println("New client connected: "+clientSocket.getInetAddress());
                ClientHandler clientHandler=new ClientHandler(clientSocket);
                clientHandler.start();
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
    
}
