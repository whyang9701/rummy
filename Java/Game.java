import java.io.*;
import java.util.*;

public class Game{
//this class designed for implementing rummikub game logic;

    ArrayList<Card> cardPile = new ArrayList<>();
    ArrayList<ArrayList<Card>> cardGroup = new ArrayList<>();
    ArrayList<GameSnapshot> gameHistory = new ArrayList<>();
    ArrayList<GamePlayer> players = new ArrayList<>();
    int playerNum = 0;
    public Game( ArrayList<GamePlayer> players){
        this.players = players;
        playerNum = players.size();
    }

    private void init() {
        for(int j = 0 ; j<2 ; j++){
            for(int i = 1 ; i<=13 ; i++){
                cardPile.add(new Card(i,CardColor.RED));
            }
            for(int i = 1 ; i<=13 ; i++){
                cardPile.add(new Card(i,CardColor.YELLOW));
            }
            for(int i = 1 ; i<=13 ; i++){
                cardPile.add(new Card(i,CardColor.BLUE));
            }
            for(int i = 1 ; i<=13 ; i++){
                cardPile.add(new Card(i,CardColor.GREEN));
            }
        }
        cardPile.add(new Card(0 , CardColor.SMILE));
        cardPile.add(new Card(0 , CardColor.SMILE));

        //Disturb cards
        Random random = new Random();
        for(int i = 0 ; i<106 ; i++){
            Card card1 = cardPile.get(random.nextInt(106));
            Card card2 = cardPile.get(random.nextInt(106));
            cardPile.remove(card1);
            cardPile.remove(card2);
            cardPile.add(card1);
            cardPile.add(card2);
        }

        
    }

    private void deal(){
        //deal
        for(int i = 0 ; i<14 ;i++){
            for(GamePlayer player : players){
                Card card = cardPile.get(0);
                cardPile.remove(card);
                player.hand.add(card);
            }
        }
    }
    
    public void start(){
        init();
          
        deal();
        
        GameSnapshot snapshot = new GameSnapshot(cardPile , cardGroup , players);
        gameHistory.add(snapshot);
        System.out.println("the game is start with the condition below \n");
        System.out.println(snapshot);
        
        //this loop represent the whole game proccess will go through
        outer:
        while(true){
            try{
                //this loop represent the game will switch term in game players
                for(GamePlayer player : players){
                    GameSnapshot nowGameSnapshot = (GameSnapshot)(gameHistory.get(gameHistory.size()-1).clone());
                    int numberOfThisPlayer = players.indexOf(player);
                    //send breadcast message to other client
                    for(int i = 0 ; i < players.size() ; i++){
                        if(i == numberOfThisPlayer){
                            continue;
                        }
                        else{
                            GameInfo gameInfo = new GameInfo();
                            gameInfo.sender = "server";
                            switch (numberOfThisPlayer) {
                                case 0:
                                    gameInfo.message = GameMessage.PLAYER1s_TURN;
                                    break;
                                case 1:
                                    gameInfo.message = GameMessage.PLAYER2s_TURN;
                                    break;
                                case 2:
                                    gameInfo.message = GameMessage.PLAYER3s_TURN;
                                    break;
                                case 3:
                                    gameInfo.message = GameMessage.PLAYER4s_TURN;
                                    break;

                                default:
                                    break;
                            }
                            players.get(i).oos.writeObject(gameInfo);
                        }
                    }
                    
                    // this loop represent every player has unlimited chance to move
                    myTerm:
                    while(true){
                        
                        //generate the data pack that will send to client
                        GameInfo gameInfo = new GameInfo();
                        gameInfo.sender = "server";
                        gameInfo.message = GameMessage.YOUR_TURN;
                        gameInfo.copy = nowGameSnapshot;
                        gameInfo.copy.cardPile = new ArrayList<Card>();
                        gameInfo.copy.hands = new ArrayList<ArrayList<Card>>();
                        gameInfo.remainCardPileNumber = cardPile.size();
                        gameInfo.isBreakedTheIce = player.isBreakedTheIce;
                        gameInfo.playersHand = (ArrayList<Card>)player.hand.clone();
                        player.oos.writeObject(gameInfo);
                        //Thread.sleep(5000);


                        GameInfo replygameInfo = (GameInfo)player.ois.readObject();
                        if(replygameInfo.playerMovement == PlayerMovement.Pick_One_Card){

                        }
                        else if(replygameInfo.playerMovement == PlayerMovement.Move_One_Card){

                        }
                        else if(replygameInfo.playerMovement == PlayerMovement.Out_Of_A_Card){

                        }
                        else if(replygameInfo.playerMovement == PlayerMovement.Confirm){

                        }
                        else if(replygameInfo.playerMovement == PlayerMovement.Roll_Back){

                        }
                        else{
                            throw new Exception();
                        }
                    }
                }
            }
            
            
            catch(IOException e){
                System.out.println(e);
                e.printStackTrace();
                this.shutdown();
                break outer;
            }
            catch(ClassNotFoundException e){
                System.out.println(e);
                e.printStackTrace();
                this.shutdown();
                break outer;
            }
            catch(Exception e){
                System.out.println(e);
                e.printStackTrace();
                this.shutdown();
                break outer;
            }
                
        }
    }

    public void shutdown(){
        for(GamePlayer player : players){
            try {
                player.oos.close();
                player.ois.close();
                player.clientConnection.close();
            } catch (IOException e) {
                //TODO: handle exception
                System.out.println("errer occured as game.shutdown()");
                System.out.println(e);
            }
            
        }
    }
}