package chatroom;

import javafx.scene.layout.VBox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.StringTokenizer;


public class Client {
    static private Socket s;
    static private DataInputStream dis;
    static private DataOutputStream dos;

    public Client() throws IOException{
        try{
            s = new Socket(InetAddress.getByName("localhost"),7777);
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
        }catch (IOException e){
            System.out.println("Server is out of service!\nRun server and Restart .");
            CloseEveryThing(s, dis, dos);
            System.exit(0);
        }
    }

    public static void SendMessageToServer(String MessageToServer,String username){
        try{
            dos.writeUTF(username+": "+MessageToServer);
            dos.flush();
        }catch (IOException e){
            System.out.println("Error Sending Message to server");
        }
    }

    public static Thread receiveMessageFromServer(VBox vBox,String username){
        return new Thread(new Runnable() {
            @Override
            public void run() {
                while (s.isConnected()) {
                    try {
                        String Receive = dis.readUTF();
                        StringTokenizer tokenizer = new StringTokenizer(Receive, ":");
                        String token = tokenizer.nextToken();
                        if (token.equals(username)) {
                        }else{
                        ChatController.AddLabel(Receive,vBox);
                        }
                    } catch (IOException e) {

                        try {
                            s.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }

        });


    }

    public void CloseEveryThing(Socket S , DataInputStream Dis ,DataOutputStream Dos){
        try{
            if (S!=null){
                S.close();
            }
            if (Dis!=null){
                Dis.close();
            }
            if (Dos!=null){
                Dos.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }



}
