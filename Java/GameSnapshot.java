import java.util.*;
import java.io.*;
public class GameSnapshot implements Cloneable,Serializable{
//this class designed for record every change of game
    ArrayList<Card> cardPile = new ArrayList<>();
    ArrayList<ArrayList<Card>> cardGroups = new ArrayList<>();
    ArrayList<ArrayList<Card>> hands = new ArrayList<>();
    ArrayList<Boolean> isBreakedTheIces = new ArrayList<>();
    public GameSnapshot(ArrayList<Card> cardPile , ArrayList<ArrayList<Card>> cardGroups, ArrayList<GamePlayer> players ){
        this.cardPile = (ArrayList<Card>)cardPile.clone();
        this.cardGroups = (ArrayList<ArrayList<Card>>)cardGroups.clone();
        for(GamePlayer player : players){
            this.hands.add((ArrayList<Card>)player.hand.clone());
            isBreakedTheIces.add(player.isBreakedTheIce);
        }
        
    }
    public String toString(){
        String s = "";
        s += "cardPile remain : " + cardPile.size() + " cards" + "\n";
        s += "cardGroups are list below" + "\n";
        for(ArrayList<Card> group : cardGroups){
            for(Card card : group){
                s += card.toString() + ",";
            }
            s+= "\n";
        }   
        s+= "\n";
        for(int i = 0 ; i < hands.size() ; i++){
            s += "player"+i+"'s hand has" +"\n";
            for(Card card : hands.get(i)){
                s += card.toString() + ",";
            }
            s+= "\n";
        }
        s+= "\n";
        for(int i = 0 ; i < isBreakedTheIces.size() ; i++){
            s += "player"+i ;
            if(isBreakedTheIces.get(i)){
                s+= " is break the ice";
            }
            else{
                s+= " is not break the ice";
            }
            s+="\n";
            
        }
        s+= "\n";
        return s;
    }
    public Object clone(){
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
}