package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import util.Constants;
import java.util.ArrayList;

public class ChatServer {
    private static final int PORT = Constants.PORT;
    private static ArrayList<ClientHandler> clients=new ArrayList<>();
    public static void main(String args[]){
        try {
            ServerSocket serverSocket=new ServerSocket(PORT);
            
            String message="Chat Server Started\nListening on port ";
            System.out.println("*******************************");
            System.out.println(message+Constants.PORT);
            System.out.println("*******************************");

            while(true){
                Socket clientSocket=serverSocket.accept();
                System.out.println("New client connected: "+clientSocket.getInetAddress());
                ClientHandler clientHandler=new ClientHandler(clientSocket,clients);
                clients.add(clientHandler);
                clientHandler.start();
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
    
}
