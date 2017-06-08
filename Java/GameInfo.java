import java.io.*;

public class GameInfo implements Serializable {
    String sender = "";
    PlayerMovement PlayerMovement = null;
    String message = "";
    GameSnapshot copy = null;

    public String toString() {
        String s = "sender : ";
        s += sender + "\n";
        s += "message : ";
        s += message + "\n";
        s += "PlayerMovement : ";
        s += PlayerMovement.toString() + "\n";
        return s;
    }
}