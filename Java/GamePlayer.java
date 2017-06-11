import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class GamePlayer {
    Socket clientConnection = null;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    ArrayList<Card> hand = new ArrayList<>();
    boolean isBreakedTheIce = false;
    public GamePlayer(Socket clientConnection, ObjectOutputStream oos, ObjectInputStream ois) {
        this.clientConnection = clientConnection;
        this.oos = oos;
        this.ois = ois;
    }
}