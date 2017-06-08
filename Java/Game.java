import java.io.*;

public class Game{
    GamePlayer players[] = null;
    public Game( GamePlayer players[]){
        this.players = players;
    }
    public void start(){
        int playerNum = players.length;
        while(true){
            for(GamePlayer player : players){
                try{
                    Card card = new Card(3,CardColor.YELLOW);
                    GameInfo gameInfo = new GameInfo();
                    gameInfo.sender = "server";
                    gameInfo.message = "hi";
                    gameInfo.PlayerMovement = PlayerMovement.Roll_Back;
                    player.oos.writeObject(gameInfo);
                    Thread.sleep(1000);
                }
                catch(IOException e){

                }
                catch(InterruptedException e){
                    
                }
                
            }
        }
    }
    public void shutdown(){

    }
}