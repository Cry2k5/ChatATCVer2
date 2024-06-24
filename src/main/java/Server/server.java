package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.sql.SQLException;


public class server{

    
    static ServerSocket ss;
    static ArrayList <ClientHandler> Clients = new ArrayList<>();
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException{

        
        ss=new ServerSocket(7777);
        while (true){
            Socket s =ss.accept();
            DataInputStream dis=new DataInputStream(s.getInputStream());
            DataOutputStream dos=new DataOutputStream(s.getOutputStream());
            ClientHandler Client = new ClientHandler(s, dis, dos);
            Clients.add(Client);
            Client.start();

        }

    }
}

class ClientHandler extends Thread{

    Socket s;
    public DataInputStream dis;
    public DataOutputStream dos;
    public String Received;
    public boolean LoggedIn;

    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos){
        this.s=s;
        this.dis=dis;
        this.dos=dos;
    }
    

    public void run(){
        while(true){


            try {
                String Recieved = dis.readUTF();
                for (ClientHandler x : server.Clients) {
                    try {
                        x.dos.flush();
                        x.dos.writeUTF(Recieved);
                        x.dos.flush();
                    } catch (IOException e) {
                    }
                }
            } catch (IOException e) {
                try {
                    this.s.close();
                } catch (IOException e1) {
                }
            }
        }

    }
}
