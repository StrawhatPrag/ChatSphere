package client;

import java.io.IOException;
import java.io.DataInputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import util.MessageType;

public class ClientListener extends Thread {
    private Socket socket;
    private DataInputStream input;
    private MessageListener messageListener;
    private UserListListener userListListener;

    public ClientListener(Socket socket) {
        this.socket=socket;
    }

    public void setMessageListener(MessageListener listener) {
        this.messageListener=listener;
    }

    public void setUserListListener(UserListListener listener) {
        this.userListListener=listener;
    }

    @Override
    public void run() {
        try {
            input=new DataInputStream(socket.getInputStream());
            while (true) {
                String packet=input.readUTF();
                String[] parts=packet.split("\\|", 3);
                String type=parts[0];

                switch (type) {
                    case MessageType.CHAT:
                        if (parts.length >= 3 && messageListener != null) {
                            messageListener.onMessageReceived(parts[1] + ": " + parts[2]);
                        }
                        break;
                    case MessageType.PRIVATE:
                        messageListener.onMessageReceived(
                            "[Private] " + parts[1] + ": " + parts[2]
                        );
                        break;
                    case MessageType.SYSTEM:
                        messageListener.onMessageReceived(
                            "*** " + parts[1] + " ***"
                        );
                        break;
                    case MessageType.USERS:
                        if(userListListener != null){
                            if (parts.length >= 2 && userListListener != null) {
                                userListListener.onUserListReceived(
                                    Arrays.asList(parts[1].split(","))
                                );
                            }
                        }
                        break;
                    default:
                        messageListener.onMessageReceived(packet);
                }
            }
        } catch (IOException e) {
            if (messageListener!=null) {
                messageListener.onMessageReceived("*** Disconnected from server ***");
            }
        }
    }
}
