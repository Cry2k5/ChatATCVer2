package chatroom;

import chatroom.TextMessages.TextMessages;
import chatroom.TextMessages.TextMessagesService;
import chatroom.database.DatabaseHandler;
import chatroom.database.DatabaseQuery;
import chatroom.users.User;
import chatroom.users.UserService;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;

public class ChatController implements Initializable {


    @FXML
    private Button button_send;

    @FXML
    private ScrollPane sp_main;

    @FXML
    private TextField tf_message;

    @FXML
    private VBox vbox_messages;



    private User user;
    private UserService userService;
    private TextMessages textMessages = new TextMessages();


    DatabaseQuery databaseQuery = new DatabaseQuery(new DatabaseHandler());
    TextMessagesService textMessagesService = new TextMessagesService(databaseQuery);

    public ChatController() throws SQLException, IOException {
        try {
            Client client = new Client();
            userService=new UserService(databaseQuery);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void setUser(User user) {
        this.user = user;
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            if (sp_main != null && sp_main.getScene() != null) {
                Stage stage = (Stage) sp_main.getScene().getWindow();
                stage.setOnCloseRequest(event -> {
                    System.out.println("Close Request Event Triggered");
                    Platform.exit();
                    System.exit(0);
                });
            }
        });



        vbox_messages.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,Number oldValue,Number newValue){
                sp_main.setVvalue((Double) newValue);
            }
        });

        try {
            List<TextMessages> messages = textMessagesService.getAllMessages();
            User senderUsername;
            for (TextMessages message : messages) {
                if(user.getId()!=message.getSenderId()) {
                    senderUsername = userService.getUserById(message.getSenderId());
                    AddLabel(senderUsername.getname()+":"+message.getMessage(), vbox_messages);
                }else{
                    HBox hbox = new HBox();
                    hbox.setAlignment(Pos.CENTER_RIGHT);
                    hbox.setPadding(new Insets(5,5,5,10));
                    Text text = new Text(message.getMessage());
                    TextFlow textFlow = new TextFlow(text);

                    textFlow.setStyle("-fx-color: rgb(239,242,255);" +
                            "-fx-background-color: rgb(15,125,242);" +
                            "-fx-background-radius: 20px;");

                    textFlow.setPadding(new Insets(5,10, 5,10));
                    text.setFill(Color.color(0.934,0.945,0.996));
                    hbox.getChildren().add(textFlow);
                    vbox_messages.getChildren().add(hbox);
                }
            };
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Client.receiveMessageFromServer(vbox_messages, user.getname()).start();
        button_send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String Message = tf_message.getText();
                if(!(Message.isEmpty())){
                    HBox hbox = new HBox();
                    hbox.setAlignment(Pos.CENTER_RIGHT);
                    hbox.setPadding(new Insets(5,5,5,10));
                    Text text = new Text(Message);
                    TextFlow textFlow = new TextFlow(text);

                    textFlow.setStyle("-fx-color: rgb(239,242,255);" +
                            "-fx-background-color: rgb(15,125,242);" +
                            "-fx-background-radius: 20px;");

                    textFlow.setPadding(new Insets(5,10, 5,10));
                    text.setFill(Color.color(0.934,0.945,0.996));
                    hbox.getChildren().add(textFlow);
                    vbox_messages.getChildren().add(hbox);
                    tf_message.clear();
                    textMessages = new TextMessages(0,user.getId(),new Timestamp(System.currentTimeMillis()),Message);
                    try {
                        textMessagesService.addMessage(textMessages);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    Client.SendMessageToServer(Message,user.getname());

                    System.out.println(text);
                }
            }
        });

    }
    public static void AddLabel(String Message , VBox vBox){
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(5,5,5,10));
        Text text = new Text(Message);
        TextFlow textFlow = new TextFlow(text);

        textFlow.setStyle("-fx-background-color: rgb(233,233,235);" +
                "-fx-background-radius: 20px;");


        textFlow.setPadding(new Insets(5,10, 5,10));
        text.setFill(Color.color(0,0,0));
        hbox.getChildren().add(textFlow);
        Platform.runLater(new Runnable() {
            @Override
            public void run(){
                vBox.getChildren().add(hbox);
            }
        });

    }
}
