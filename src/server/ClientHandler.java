package server;

import java.io.IOException;
import java.io.DataInputStream;
import java.net.Socket;
import java.io.DataOutputStream;
import java.util.concurrent.ConcurrentHashMap;

import util.MessageType;

public class ClientHandler extends Thread{
    private Socket socket;
    private ConcurrentHashMap<String, ClientHandler> clients;

    private DataInputStream input;
    private DataOutputStream output;

    private String username;

    public ClientHandler(Socket socket,ConcurrentHashMap<String, ClientHandler> clients){
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
        for (ClientHandler client:clients.values()){
            if (client!=this){
                client.sendMessage(message);
            }
        }
    }

    private void initializeStreams() throws IOException {
        input=new DataInputStream(socket.getInputStream());
        output=new DataOutputStream(socket.getOutputStream());
    }

    private void performLogin() throws IOException {
        while (true) {
            String loginPacket=input.readUTF();
            String[] parts=loginPacket.split("\\|", 2);
            String requestedUsername=parts[1];

            ClientHandler existing=clients.putIfAbsent(requestedUsername, this);

            if (existing==null) {
                username = requestedUsername;
                sendMessage(MessageType.OK);
                break;
            }
            sendMessage(MessageType.ERROR + "|Username already exists.");
        }
        System.out.println("Handler started for " + socket.getInetAddress());
        System.out.println(username + " joined the chat.");

        broadcast("*** " + username + " joined the chat ***");
    }

    private void listenForMessages() throws IOException {
        while (true) {
            String packet = input.readUTF();
            String[] parts = packet.split("\\|", 2);
            String type = parts[0];
            String message = parts[1];

            if (type.equals(MessageType.CHAT)) {
                String formattedMessage = username + ": " + message;
                System.out.println(formattedMessage);
                broadcast(formattedMessage);
            }
        }
    }

    private void cleanup() {
        if (username != null) {
            clients.remove(username);
            broadcast("*** " + username + " left the chat ***");
        }

        try {
            socket.close();
        } catch (IOException ignored) {}

        System.out.println("Client disconnected.");
    }

    @Override
    public void run() {
        try {

            initializeStreams();
            performLogin();
            listenForMessages();

        } catch (IOException e) {
            
            System.out.println("Connection lost with " + socket.getInetAddress());

        } finally {
            
            cleanup();
            
        }
    }
}