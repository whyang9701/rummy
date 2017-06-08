import java.io.*;
import java.net.*;

public class GamePlayer {
    Socket clientConnection = null;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    public GamePlayer(Socket clientConnection, ObjectOutputStream oos, ObjectInputStream ois) {
        this.clientConnection = clientConnection;
        this.oos = oos;
        this.ois = ois;
    }
}