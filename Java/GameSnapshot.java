import java.util.*;
import java.io.*;

public class GameSnapshot implements Cloneable, Serializable {
    //this class designed for record every change of game
    ArrayList<Card> cardPile = new ArrayList<>();
    ArrayList<ArrayList<Card>> cardGroups = new ArrayList<>();
    ArrayList<ArrayList<Card>> hands = new ArrayList<>();
    ArrayList<Boolean> isBreakedTheIces = new ArrayList<>();

    public GameSnapshot(ArrayList<Card> cardPile, ArrayList<ArrayList<Card>> cardGroups,
            ArrayList<GamePlayer> players) {
        this.cardPile = (ArrayList<Card>) cardPile.clone();
        this.cardGroups = (ArrayList<ArrayList<Card>>) cardGroups.clone();
        for (GamePlayer player : players) {
            this.hands.add((ArrayList<Card>) player.hand.clone());
            isBreakedTheIces.add(player.isBreakedTheIce);
        }

    }

    public String toString() {
        String s = "";
        s += "cardPile remain : " + cardPile.size() + " cards" + "\n";
        s += "cardPile cards\n";
        for (Card card : getSortedCards(cardPile)) {
            s += card +",";

        }
        s+= "\n";
        s += "cardGroups are list below" + "\n";
        for (ArrayList<Card> group : cardGroups) {
            for (Card card : group) {
                s += card.toString() + ",";
            }
            s += "\n";
        }
        s += "\n";
        for (int i = 0; i < hands.size(); i++) {
            s += "player" + i + "'s hand has" + "\n";
            for (Card card : getSortedCards(hands.get(i))) {
                s += card.toString() + ",";
            }
            s += "\n";
        }
        s += "\n";

        for (int i = 0; i < isBreakedTheIces.size(); i++) {
            s += "player" + i;
            if (isBreakedTheIces.get(i)) {
                s += " is break the ice";
            } else {
                s += " is not break the ice";
            }
            s += "\n";

        }
        s += "\n";
        return s;
    }

    public Object clone() {
        Object o = new Object();
        try {
            o = super.clone();
        } catch (CloneNotSupportedException e) {
            //TODO: handle exception
            System.out.println("clone error");
            System.out.println(e.getStackTrace());
        }
        return o;
    }

    private ArrayList<Card> getSortedCards(ArrayList<Card> cards) {
        ArrayList<Card> newCards = (ArrayList<Card>) cards.clone();
        for (int i = 0; i < newCards.size(); i++) {
            for (int j = 0; j < newCards.size() - i - 1; j++) {
                if (newCards.get(j).number > newCards.get(j + 1).number) {
                    Collections.swap(newCards, j, j + 1);
                }
            }
        }

        for (CardColor color : CardColor.values()) {
            //System.out.println(color);
            for (int i = 0; i < newCards.size(); i++) {
                if (newCards.get(i).color == color) {
                    Card c = newCards.get(i);
                    newCards.remove(c);
                    newCards.add(0,c);
                    
                }
            }
        }
        return newCards;
    }
}