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

    private void handlePrivateMessage(String message) {
        String[] parts =message.split(" ",3);

        if (parts.length<3){
            sendMessage("Usage: /msg <username> <message>");
            return;
        }

        String receiverUsername=parts[1];
        String privateMessage=parts[2];

        ClientHandler receiver =clients.get(receiverUsername);

        if (receiver==null){
            sendMessage("User '"+receiverUsername+"' not found.");
            return;
        }

        receiver.sendMessage("[Private] "+username+": "+privateMessage);

        sendMessage("[Private to "+receiverUsername+"] "+privateMessage);
    }

    private void handleUsersCommand(){
        StringBuilder builder=new StringBuilder();
        builder.append("\n------ Online Users ------\n");

        for (String user:clients.keySet()){
            builder.append(user).append("\n");
        }

        builder.append("--------------------------");
        sendMessage(builder.toString());
    }

    private void handleHelpCommand(){
        StringBuilder help=new StringBuilder();

        help.append("\n==============ChatSphere Help==============\n");
        help.append("/help                          Show available commands\n");
        help.append("/users                         List online users\n");
        help.append("/msg <user> <message>          Send a private message\n");
        help.append("/quit                          Disconnect from server\n");
        help.append("============================================\n");

        sendMessage(help.toString());
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

    private boolean processPacket(String packet){
        String[] parts = packet.split("\\|", 2);
        String type = parts[0];

        if(type.equals(MessageType.QUIT)){
            return false;
        }
        String message = parts.length>1?parts[1]:"";
        if (type.equals(MessageType.CHAT)) {
            if(message.startsWith("/msg ")) {
                handlePrivateMessage(message);
            }
            else if (message.equals("/users")){
                handleUsersCommand();
            }
            else if (message.equals("/help")){
                handleHelpCommand();
            }
            else if (message.startsWith("/")) {
                sendMessage("Unknown command.\nType /help for available commands.");
            }
            else {
                String formattedMessage = username + ": " + message;
                System.out.println(formattedMessage);
                broadcast(formattedMessage);
            }
        }
        return true;
    }

    private void listenForMessages() throws IOException {
        while (true) {
            String packet = input.readUTF();
            if (!processPacket(packet)) {
                break;
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