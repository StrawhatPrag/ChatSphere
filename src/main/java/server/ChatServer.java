package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import util.Constants;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {
    private static final int PORT = Constants.PORT;
    private static ConcurrentHashMap<String, ClientHandler> clients=new ConcurrentHashMap<>();
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
                clientHandler.start();
            }

        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
    
}
