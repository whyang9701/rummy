import java.io.*;
import java.net.*;

public class GameClient{
    public void start(){
        System.out.println("gc running");
        String host = "127.0.0.1";
        int port = 8888;
        Socket server = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            server= new Socket(host, port);
            oos = new ObjectOutputStream(server.getOutputStream());
            ois = new ObjectInputStream(server.getInputStream());
        } catch (IOException e) {
            System.out.println("client connection error");
        }
        try {
            System.out.println(((GameInfo)ois.readObject()).message);
            server.close();
        } catch (Exception e) {
            System.out.println("got something wrong");
        }
    }
}