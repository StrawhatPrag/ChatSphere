package server;

import java.io.IOException;
import java.io.DataInputStream;
import java.net.Socket;
import java.io.DataOutputStream;
import java.util.Scanner;

public class ClientHandler extends Thread{
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private Scanner scanner;

    public ClientHandler(Socket socket){
        this.socket=socket;
    }

    @Override
    public void run(){
        try {
            input=new DataInputStream(socket.getInputStream());
            output=new DataOutputStream(socket.getOutputStream());
            ServerSender sender=new ServerSender(output);
            sender.start();
            scanner=new Scanner(System.in);
            System.out.println("Handler started for "+ socket.getInetAddress());
            while (true) {
                String message=input.readUTF();
                System.out.println("Received: "+message);
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Connection lost with " + socket.getInetAddress());
        }
        finally {
            try {
                socket.close();
            } catch (IOException ignored) {}

            System.out.println("Client disconnected.");

        }
    }
}