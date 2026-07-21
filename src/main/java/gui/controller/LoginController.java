package gui.controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import gui.controller.ChatController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import client.ChatClient;
import util.MessageType;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private Button connectButton;

    private ChatClient client=new ChatClient();

    @FXML
    private void initialize(){
        connectButton.setOnAction(e->connect());
    }

    private void connect(){
        String username=usernameField.getText().trim();

        if(username.isEmpty()){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Username");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a username");
            alert.showAndWait();

            return;
        }
        
        String response=client.connect(username);

        if(response.equals(MessageType.OK)){
            try {
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/fxml/chat.fxml"));
                Parent root=loader.load();
                ChatController controller=loader.getController();
                controller.setClient(client);
                Stage stage=(Stage) connectButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("ChatSphere");
                stage.show();

                client.startListening();
            } catch (IOException e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
        else{
            String[] parts=response.split("\\|", 2);

            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection Failed");
            alert.setHeaderText(null);
            alert.setContentText(parts[1]);
            alert.showAndWait();
        }
    }    
}

