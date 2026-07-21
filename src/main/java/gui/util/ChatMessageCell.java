package gui.util;

import gui.model.ChatMessage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.time.format.DateTimeFormatter;

public class ChatMessageCell extends ListCell<ChatMessage> {

    private final VBox box = new VBox(3);
    private final HBox container = new HBox();

    private final Label senderLabel = new Label();
    private final Label messageLabel = new Label();
    private final Label timeLabel = new Label();
    private final Label systemLabel = new Label();

    private final Circle avatarCircle = new Circle(12);
    private final Label avatarLabel = new Label();
    private final StackPane avatar = new StackPane();
    private final HBox header = new HBox(6);

    private static String currentUser;

    public static void setCurrentUser(String username) {
        currentUser = username;
    }

    @Override
    protected void updateItem(ChatMessage message, boolean empty) {

        super.updateItem(message, empty);

        if (empty || message == null) {
            setGraphic(null);
            setText(null);
            return;
        }

        // Reset reused controls
        box.getChildren().clear();
        container.getChildren().clear();

        setPadding(new Insets(5, 0, 5, 0));

        senderLabel.setText(message.getSender());
        messageLabel.setText(message.getText());

        timeLabel.setText(
                message.getTime().format(
                        DateTimeFormatter.ofPattern("hh:mm a")
                )
        );

        senderLabel.setStyle("-fx-font-weight:bold;");
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(240);
        timeLabel.setStyle("-fx-font-size:10; -fx-text-fill:#777777;");


        if (message.getType() == ChatMessage.Type.SYSTEM) {

            systemLabel.setText("──────── " + message.getText() + " ────────");
            systemLabel.setStyle(
                    "-fx-text-fill:#777777;" +
                    "-fx-font-style:italic;"
            );

            container.setAlignment(Pos.CENTER);
            container.getChildren().setAll(systemLabel);

            setGraphic(container);
            return;
        }

        avatarCircle.setFill(Color.web("#2196F3"));

        if (!message.getSender().isEmpty()) {
            avatarLabel.setText(
                    message.getSender()
                            .substring(0, 1)
                            .toUpperCase()
            );
        } else {
            avatarLabel.setText("?");
        }

        avatarLabel.setStyle(
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;"
        );

        avatar.getChildren().setAll(
                avatarCircle,
                avatarLabel
        );

        header.getChildren().setAll(
                avatar,
                senderLabel
        );

        header.setAlignment(Pos.CENTER_LEFT);

        if (message.getSender().equals(currentUser)) {

            box.getChildren().addAll(
                    messageLabel,
                    timeLabel
            );

            box.setStyle(
                "-fx-background-color:#CFE8FF;" +
                "-fx-background-radius:12;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 5,0,0,2);"
            );

            container.setAlignment(Pos.CENTER_RIGHT);

        } else {

            box.getChildren().addAll(
                    header,
                    messageLabel,
                    timeLabel
            );

            box.setStyle(
                    "-fx-background-color:white;" +
                    "-fx-background-radius:12;" +
                    "-fx-border-color:#DDDDDD;" +
                    "-fx-border-radius:12;"
            );

            container.setAlignment(Pos.CENTER_LEFT);
        }

        box.setPadding(new Insets(8));
        box.setMaxWidth(260);
        box.setPrefWidth(javafx.scene.layout.Region.USE_COMPUTED_SIZE);
        box.setMinWidth(javafx.scene.layout.Region.USE_PREF_SIZE);
        box.setFillWidth(false);

        container.getChildren().setAll(box);

        setGraphic(container);
    }
}