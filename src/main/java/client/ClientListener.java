package client;

import java.io.IOException;
import java.io.DataInputStream;
import java.net.Socket;

public class ClientListener extends Thread {
    private Socket socket;
    private DataInputStream input;
    private MessageListener messageListener;

    public ClientListener(Socket socket) {
        this.socket=socket;
    }

    public void setMessageListener(MessageListener listener) {
        this.messageListener=listener;
    }

    @Override
    public void run() {
        try {
            input=new DataInputStream(socket.getInputStream());
            while (true) {
                String message=input.readUTF();
                if (messageListener!=null) {
                    messageListener.onMessageReceived(message);
                }
            }
        } catch (IOException e) {
            if (messageListener!=null) {
                messageListener.onMessageReceived("*** Disconnected from server ***");
            }
        }
    }
}
