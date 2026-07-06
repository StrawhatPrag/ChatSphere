package server;

import java.io.IOException;
import java.io.DataInputStream;
import java.net.Socket;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientHandler extends Thread{
    private Socket socket;
    private ArrayList<ClientHandler> clients;
    private DataInputStream input;
    private DataOutputStream output;

    public ClientHandler(Socket socket,ArrayList<ClientHandler> clients){
        this.socket=socket;
        this.clients=clients;
    }

    public void sendMessage(String message){
        try {
            output.writeUTF(message);
            output.flush();
        } catch (IOException e) {
            // TODO: handle exception
            System.out.println("Unable to send message.");
        }
    }

    private void broadcast(String message){
        for (ClientHandler client:clients){
            if (client!=this){
                client.sendMessage(message);
            }
        }
    }

    @Override
    public void run(){
        try {
            input=new DataInputStream(socket.getInputStream());
            output=new DataOutputStream(socket.getOutputStream());
            ServerSender sender=new ServerSender(output);
            sender.start();
            System.out.println("Handler started for "+ socket.getInetAddress());
            while (true) {
                String message=input.readUTF();
                System.out.println("Received: "+message);
                broadcast(message);
            }
        } catch (IOException e) {
            // TODO: handle exception
            System.out.println("Connection lost with " + socket.getInetAddress());
        }
        finally {
            clients.remove(this);

            try {
                socket.close();
            } catch (IOException ignored) {}

            System.out.println("Client disconnected.");

        }
    }
}