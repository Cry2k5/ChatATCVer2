package chatroom;

import chatroom.TextMessages.TextMessages;
import chatroom.users.User;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.Optional;
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
    @FXML
    private Button logout;


    private User user;

    public ChatController() throws Exception {
    }

    public void setUser(User user) {
        this.user = user;
    }
    Client client = new Client();

    @FXML
    private ImageView yourAvatar;

    @FXML
    private Label yourName;

    @FXML
    private VBox friendView;

    @FXML
    private Button refresh;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    public void setyourName(){
        yourName.setText(user.getname());
    }

    public Image urlToImage(String imagePath) {
        try {
            File file = new File(imagePath);
            String localUrl = file.toURI().toURL().toString();
            Image image = new Image(localUrl);
            return image;
        } catch (Exception e) {
            // Xử lý nếu không thể tạo được Image từ URL
            e.printStackTrace();
            return null;
        }
    }
    public void setAvatar(){
        yourAvatar.setImage(urlToImage(user.getimage()));
    }

    public ObservableList<User> getUser() throws Exception {

        ObservableList<User> datalist = FXCollections.observableArrayList();

        connect = JDBCUtil.getConnection();
        prepare = connect.prepareStatement("SELECT name, avatar FROM users WHERE active = 'online' AND name !=?");
        prepare.setString(1, user.getname()); // Tránh lấy user hiện tại

        result = prepare.executeQuery();


        while (result.next()) {
            User user = new User(result.getString("name"),result.getString("avatar") );
            datalist.add(user);

        }
        JDBCUtil.closeConnect(connect);
        return datalist;
    }
    public AnchorPane createFriendField(User user){
        AnchorPane pane = new AnchorPane();
        Label friendName = new Label();
        ImageView friendAvatar = new ImageView();
        pane.setPrefHeight(60.0);
        pane.setPrefWidth(216.0);

        friendAvatar.setFitHeight(45.0);
        friendAvatar.setFitWidth(45.0);
        friendAvatar.setLayoutX(14.0);
        friendAvatar.setLayoutY(8.0);
        friendAvatar.setPickOnBounds(true);
        friendAvatar.setPreserveRatio(true);

        friendName.setLayoutX(68.0);
        friendName.setLayoutY(6.0);
        friendName.setPrefHeight(46.0);
        friendName.setPrefWidth(139.0);

        friendName.setText(user.getname());
        String path = user.getimage();
        friendAvatar.setImage((urlToImage(path)));

        pane.getChildren().add(friendName);
        pane.getChildren().add(friendAvatar);

        return  pane;
    }
    public void showFriendView() throws Exception {
        friendView.getChildren().clear();

        ObservableList<User> lístUser = getUser();
        for(User user : lístUser){
            if(user.getname()!=this.user.getname()){
                AnchorPane pane = createFriendField(user);
                friendView.getChildren().add(pane);
            }

        }

    }

    private Alert alert;



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
        refresh.setOnAction(event -> {
            try {
                showFriendView();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        try {
            logout.setOnAction(event -> {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Do you want to log out?");

                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    connect =JDBCUtil.getConnection();
                    String updateQuery = "UPDATE users SET active = 'offline' WHERE email = ?";
                    try {
                        PreparedStatement updateStatement = connect.prepareStatement(updateQuery);

                        updateStatement.setString(1, user.getEmail());
                        updateStatement.executeUpdate();

                        Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
                        Stage stage = new Stage();
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();

                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    // Khi xác nhận thoát, sẽ ẩn đi giao diện chính.
                    logout.getScene().getWindow().hide();


                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        setAvatar();
        setyourName();
        vbox_messages.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sp_main.setVvalue((Double) newValue);
            }
        });

        try {
            List<TextMessages> messages = XmlMessageHandler.readMessagesFromXml();
            for (TextMessages message : messages) {
                if (!user.getname().equals(message.getSenderName())) {
                    AddLabel(message.getSenderName() + ":" + message.getMessage(), vbox_messages);
                } else {
                    HBox hbox = new HBox();
                    hbox.setAlignment(Pos.CENTER_RIGHT);
                    hbox.setPadding(new Insets(5, 5, 5, 10));
                    Text text = new Text(message.getMessage());
                    TextFlow textFlow = new TextFlow(text);

                    textFlow.setStyle("-fx-color: rgb(239,242,255);" +
                            "-fx-background-color: rgb(15,125,242);" +
                            "-fx-background-radius: 20px;");

                    textFlow.setPadding(new Insets(5, 10, 5, 10));
                    text.setFill(Color.color(0.934, 0.945, 0.996));
                    hbox.getChildren().add(textFlow);
                    vbox_messages.getChildren().add(hbox);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        client.receiveMessageFromServer(vbox_messages, user.getname()).start();
        button_send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String Message = tf_message.getText();
                if (!(Message.isEmpty())) {
                    HBox hbox = new HBox();
                    hbox.setAlignment(Pos.CENTER_RIGHT);
                    hbox.setPadding(new Insets(5, 5, 5, 10));
                    Text text = new Text(Message);
                    TextFlow textFlow = new TextFlow(text);

                    textFlow.setStyle("-fx-color: rgb(239,242,255);" +
                            "-fx-background-color: rgb(15,125,242);" +
                            "-fx-background-radius: 20px;");

                    textFlow.setPadding(new Insets(5, 10, 5, 10));
                    text.setFill(Color.color(0.934, 0.945, 0.996));
                    hbox.getChildren().add(textFlow);
                    vbox_messages.getChildren().add(hbox);
                    tf_message.clear();
                    TextMessages textMessages = new TextMessages(user.getname(), "receiverName", new Timestamp(System.currentTimeMillis()), Message);
                    XmlMessageHandler.writeMessageToXml(textMessages);
                    client.SendMessageToServer(Message, user.getname());
                }
            }
        });
    }


    public static void AddLabel(String Message, VBox vBox) {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(5, 5, 5, 10));
        Text text = new Text(Message);
        TextFlow textFlow = new TextFlow(text);

        textFlow.setStyle("-fx-background-color: rgb(233,233,235);" +
                "-fx-background-radius: 20px;");

        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.color(0, 0, 0));
        hbox.getChildren().add(textFlow);
        Platform.runLater(() -> vBox.getChildren().add(hbox));
    }
}
