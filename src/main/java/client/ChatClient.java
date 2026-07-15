package client;

import java.io.IOException;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.Socket;
import util.Constants;
import util.MessageType;

public class ChatClient {
    private Socket socket;
    private DataOutputStream output;
    private DataInputStream input;
    private ClientListener listener;
    
    public ChatClient(){
        
    }

    public String connect(String username){
        try {
            socket=new Socket("localhost",Constants.PORT);
            input=new DataInputStream(socket.getInputStream());
            output=new DataOutputStream(socket.getOutputStream());

            output.writeUTF(MessageType.LOGIN+"|"+username);
            output.flush();

            String response=input.readUTF();

            if(response.equals(MessageType.OK)){
                listener=new ClientListener(socket);
                listener.start();
            }
            return response;

        } catch (IOException e) {
            // TODO: handle exception
            return MessageType.ERROR+"|Unable to connect to server";
        }
    }

    public boolean sendMessage(String message){
        try {
            output.writeUTF(MessageType.CHAT+"|"+message);
            output.flush();
            return true;
        } catch (IOException e) {
            // TODO: handle exception
            return false;
        }
    }
    
    public void disconnect(){

        try {
            if(output != null){
                output.writeUTF(MessageType.QUIT);
                output.flush();
            }

            if(socket != null && !socket.isClosed()){
                socket.close();
            }
        } catch (IOException ignored) {
            // TODO: handle exception
        }
    }

    public DataInputStream getInputStream(){
        return input;
    }

    public Socket getSocket() {
        return socket;
    }

    public ClientListener getListener() {
        return listener;
    }
}
