import java.io.*;

public class Card implements Serializable{
    int number; 
    CardColor color; // if color is smile ,the number will be ignored
    public Card(int num , CardColor color){
        this.number = num;
        this.color = color;
    }
    public String toString(){
        String s = "( " + this.number + " , " + this.color +" )";
        return s;
    }
}