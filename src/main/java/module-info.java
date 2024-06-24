module chatroom {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.mail;
    exports chatroom to javafx.graphics, javafx.fxml;
    opens chatroom to javafx.fxml;

}