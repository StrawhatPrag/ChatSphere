package gui.controller;

import client.ChatClient;
import client.MessageListener;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.application.Platform;

public class ChatController implements MessageListener{
    @FXML
    private TextArea chatArea;

    @FXML
    private TextField messageField;

    private ChatClient client;

    public void setClient(ChatClient client){
        this.client=client;
        client.getListener().setMessageListener(this);

        Platform.runLater(()->{

            javafx.stage.Stage stage=(javafx.stage.Stage) chatArea.getScene().getWindow();

            stage.setOnCloseRequest(event->{
                client.disconnect();
            });

        });
    }

    @FXML
    private void sendMessage(){
        String message=messageField.getText().trim();

        if(message.isEmpty()){
            return;
        }

        if (message.equals("/quit")) {
            client.disconnect();
            return;
        }

        client.sendMessage(message);
        messageField.clear();
    }

    @Override
    public void onMessageReceived(String message){
        Platform.runLater(()->{
            chatArea.appendText(message+"\n");
        });
    }
}
