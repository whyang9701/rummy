import java.io.*;
import java.net.*;

public class GameClient {
    public void start(String IP) {
        System.out.println("gc running");
        String host = "127.0.0.1";//todo
        int port = 8888;
        Socket server = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;

        try {
            server = new Socket(host, port);
            oos = new ObjectOutputStream(server.getOutputStream());
            ois = new ObjectInputStream(server.getInputStream());
        } catch (IOException e) {
            System.out.println("client connection error");
        }

        while (true) {
            try {
                System.out.println(((GameInfo) ois.readObject()).toString());

            } catch (Exception e) {
                System.out.println("got something wrong at client");
                e.printStackTrace();
                try {
                    server.close();
                    break;
                } catch (Exception ee) {
                    //TODO: handle exception
                }
                    
                   
            }
            
        }
        
    }
}