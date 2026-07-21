package gui.controller;

import java.util.List;

import client.ChatClient;
import client.MessageListener;
import client.UserListListener;
import gui.model.ChatMessage;
import gui.util.ChatMessageCell;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;
import javafx.scene.control.ListView;


public class ChatController implements MessageListener,UserListListener{
    @FXML
    private ListView<ChatMessage> chatList;

    @FXML
    private ListView<String> usersList;

    @FXML
    private TextField messageField;

    private ChatClient client;

    @FXML
    private void initialize() {
        chatList.setCellFactory(list -> new ChatMessageCell());
        usersList.setItems(FXCollections.observableArrayList(
                "Alice",
                "Bob",
                "Charlie"
        ));

    }

    public void setClient(ChatClient client){
        this.client=client;
        ChatMessageCell.setCurrentUser(client.getUsername());
        client.getListener().setMessageListener(this);
        client.getListener().setUserListListener(this);

        Platform.runLater(()->{

            javafx.stage.Stage stage=(javafx.stage.Stage) chatList.getScene().getWindow();

            stage.setOnCloseRequest(event->{
                client.disconnect();
            });

        });
    }

    @Override
    public void onUserListReceived(List<String> users) {

        Platform.runLater(() -> {
            usersList.getItems().setAll(users);
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
    public void onMessageReceived(String message) {
        Platform.runLater(() -> {
            ChatMessage chatMessage;

            if (message.startsWith("***")) {
                chatMessage = new ChatMessage(
                        "",
                        message.replace("*", "").trim(),
                        java.time.LocalTime.now(),
                        ChatMessage.Type.SYSTEM
                );

            } else if (message.contains(": ")) {
                String[] parts = message.split(": ", 2);
                chatMessage = new ChatMessage(
                        parts[0],
                        parts[1],
                        java.time.LocalTime.now(),
                        ChatMessage.Type.CHAT
                );
            } else {
                chatMessage = new ChatMessage(
                        "",
                        message,
                        java.time.LocalTime.now(),
                        ChatMessage.Type.SYSTEM
                );
            }
            chatList.getItems().add(chatMessage);
            chatList.scrollTo(chatList.getItems().size() - 1);
        });
    }
}
