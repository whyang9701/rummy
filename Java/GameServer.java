import java.io.*;
import java.net.*;

public class GameServer{
    public void Start(){
        System.out.println("game server start");
        int port = 8888;
        ServerSocket server = null;
        Socket client1 = null;
        ObjectOutputStream oos1 = null;
        ObjectInputStream ois1 = null;
        Socket client2 = null;
        ObjectOutputStream oos2 = null;
        ObjectInputStream ois2 = null;
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Server Start error");
        }
        try {
                client1 = server.accept();
                oos1 = new ObjectOutputStream(client1.getOutputStream());
                ois1 = new ObjectInputStream(client1.getInputStream());
                
                System.out.println("client1 is connecting");
            } catch (IOException e) {
                System.out.println("client1 connecting Error");
                
            }
        try {
                client2 = server.accept();
                oos2 = new ObjectOutputStream(client2.getOutputStream());
                ois2 = new ObjectInputStream(client2.getInputStream());
                System.out.println("client2 is connecting");
            } catch (IOException e) {
                System.out.println("client2 connecting Error");
                
            }

            try {
                GameInfo gameInfo = new GameInfo();
                gameInfo.sender = "server";
                gameInfo.message = "hi";
                oos1.writeObject(gameInfo);
                oos2.writeObject(gameInfo);
            } catch (IOException e) {
                System.out.println("something wrong ");
                System.out.println(e);;
            }
    }
}