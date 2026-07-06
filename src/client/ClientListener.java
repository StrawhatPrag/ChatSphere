package client;

import java.io.IOException;
import java.io.DataInputStream;
import java.net.Socket;

public class ClientListener extends Thread {
    private Socket socket;
    private DataInputStream input;
    
    public ClientListener(Socket socket) {
        this.socket=socket;
    }

    @Override
    public void run() {
        try {
            input=new DataInputStream(socket.getInputStream());
            while (true) {
                String message=input.readUTF();
                System.out.println("\nServer: "+message);
            }
        } catch (IOException e) {
            System.out.println("Disconnected from server.");
        }
    }
}
