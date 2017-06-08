import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class GameServer {
    public void Start(int playerNum) {
        System.out.println("game server start");
        int port = 8888;
        GamePlayer player1 = null;
        GamePlayer player2 = null;
        ServerSocket server = null;

        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Server Start error");
        }
        // try {
        //     Socket clientConnection = server.accept();
        //     player1 = new GamePlayer(clientConnection, new ObjectOutputStream(clientConnection.getOutputStream()),
        //             new ObjectInputStream(clientConnection.getInputStream()));

        //     System.out.println("client1 is connecting");
        // } catch (IOException e) {
        //     System.out.println("client1 connecting Error");
        // 
        // }
        // try {
        //     Socket clientConnection = server.accept();
        //     player2 = new GamePlayer(clientConnection, new ObjectOutputStream(clientConnection.getOutputStream()),
        //             new ObjectInputStream(clientConnection.getInputStream()));

        //     System.out.println("client2 is connecting");
        // } catch (IOException e) {
        //     System.out.println("client2 connecting Error");

        // }
        //GamePlayer players[] = { player1, player2 };
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
        GamePlayer players2[] = {};
        Game game = new Game(players.toArray(players2));
        game.start();
        System.out.println("game start");

    }
}