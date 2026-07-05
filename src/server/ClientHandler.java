package server;

import java.io.IOException;
import java.io.DataInputStream;
import java.net.Socket;

public class ClientHandler extends Thread{
    private Socket socket;
    private DataInputStream input;

    public ClientHandler(Socket socket){
        this.socket=socket;
    }

    @Override
    public void run(){
        try {
            input=new DataInputStream(socket.getInputStream());
            System.out.println("Handler started for "+ socket.getInetAddress());
            while (true) {
                String message=input.readUTF();
                System.out.println("Received: "+message);
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Client disconnected.");
        }
    }
}