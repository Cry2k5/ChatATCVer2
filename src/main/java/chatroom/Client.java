package chatroom;

import javafx.application.Platform;
import javafx.scene.layout.VBox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.StringTokenizer;

public class Client {
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    public Client() {
        try {
            socket = new Socket(InetAddress.getByName("localhost"), 7777);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Server is out of service!\nPlease run the server and restart.");
            closeEverything();
            System.exit(0);
        }
    }

    public void SendMessageToServer(String message, String username) {
        try {
            dos.writeUTF(username + ": " + message);
            dos.flush();
        } catch (IOException e) {
            System.out.println("Error sending message to server");
        }
    }

    public Thread receiveMessageFromServer(VBox vBox, String username) {
        return new Thread(() -> {
            try {
                while (true) {
                    String receivedMessage = dis.readUTF();
                    StringTokenizer tokenizer = new StringTokenizer(receivedMessage, ":");
                    String sender = tokenizer.nextToken();
                    if (!sender.equals(username)) {
                        Platform.runLater(() -> ChatController.AddLabel(receivedMessage, vBox)); // Sử dụng Platform.runLater để update UI
                    }
                }
            } catch (IOException e) {
                System.out.println("Connection to server lost.");
                closeEverything();
            }
        });
    }

    public void closeEverything() {
        try {
            if (socket != null) {
                socket.close();
            }
            if (dis != null) {
                dis.close();
            }
            if (dos != null) {
                dos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
