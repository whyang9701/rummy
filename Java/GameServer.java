import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class GameServer {
//S
    public void Start(int playerNum) {
        System.out.println("game server start");
        int port = 8888;
        ServerSocket server = null;
        //start listening
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Server Start error");
        }
        

        //player connections comming
        ArrayList<GamePlayer> players = new ArrayList<>();
        for (int i = 0; i < playerNum; i++) {
            GamePlayer player = null;
            try {
                Socket clientConnection = server.accept();
                player = new GamePlayer(clientConnection, new ObjectOutputStream(clientConnection.getOutputStream()),
                        new ObjectInputStream(clientConnection.getInputStream()));
            } catch (IOException e) {
                //TODO: handle exception
                System.out.println(e);
            }
            players.add(player);
            System.out.println("client" + i + " is connecting");
        }

        //start the game
        Game game = new Game(players);
        System.out.println("game start with " + playerNum + " player");
        game.start();
        
        
    }
}