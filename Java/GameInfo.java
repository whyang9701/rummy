import java.io.*;
import java.util.*;



public class GameInfo implements Serializable {
//this class is designed for exchange infomation between client and server
    String sender = "";
    PlayerMovement playerMovement = null;
    GameMessage message = null;
    GameSnapshot copy = null;
    ArrayList<Card> playersHand = new ArrayList<>();
    boolean isBreakedTheIce = false;
    int remainCardPileNumber = 0;
    public String toString() {
        String s = "sender : ";
        s += sender + "\n";
        s += "message : ";
        s += message + "\n";
        s += "PlayerMovement : ";
        s += playerMovement + "\n";
        s += "hand : ";
        for(Card c:playersHand){
            s+= c.toString();
        }
        s += "\n";
        s += "isBreakedTheIce : ";
        s += isBreakedTheIce + "\n";
        s += "playerMovement : ";
        s += playerMovement + "\n";
        s += "copy\n";
        s += copy;
        // s += hand + "\n";
        return s;
    }
}