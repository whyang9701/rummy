import java.io.*;
import java.net.*;
import java.util.*;


public class GameClient {

    GameInfo gameInfo = null;
    String input = "";
    Scanner scanner = new Scanner(System.in);

    public void start(String IP) {
        System.out.println("gc running");
        String host = IP;
        int port = 8888;
        Socket server = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        

        try {
            server = new Socket(host, port);
            System.out.println("The connection is successful.");

            oos = new ObjectOutputStream(server.getOutputStream());
            ois = new ObjectInputStream(server.getInputStream());

            } catch (IOException e) {
                System.out.println("Client connection error.");
            }

        while (true){
            try {
                gameInfo = (GameInfo) ois.readObject();

                if(gameInfo.message == GameMessage.YOUR_TURN){
                    System.out.println("It's your turn =D"); //your turn

                    System.out.println(">>Number of cards in the card pile : " + gameInfo.remainCardPileNumber); //cardpile

                    
                    System.out.println(">>Card groups : ");  //cardgroup
                    int cardgroupnumbering=0;
                    String s="";
                    for(ArrayList<Card> group : gameInfo.copy.cardGroups)
                    {
                        s+="(" + Integer.toString(cardgroupnumbering)+ ") " ;
                        for(Card card : group)
                        {
                            s += card.toString() + ",";
                            
                        }
                        cardgroupnumbering++;
                        s+= "\n";
                    }   
                    System.out.println(s+"\n");

                    System.out.println(">>Your hands : "); //playerhands
                    s="";
                    int handsnumbering=0;
                    for(Card card : gameInfo.playersHand)
                    {
                        s += "(" + Integer.toString(handsnumbering)+ ") "+card.toString()+",";
                        handsnumbering++;
                        if(handsnumbering%5==0 || handsnumbering!=0){
                            s+="\n";
                        }
                    }
                    s+= "\n";
                    System.out.println(s);


                    if(!gameInfo.isBreakedTheIce) //ice
                                        {
                                            System.out.println("You have NOT breaked the ice.");
                                        }

                    boolean complete = false;
                    PlayerMovement movement = PlayerMovement.Roll_Back;
                    boolean inputck1=false;
                    boolean inputck2=false;
                    boolean inputck3=false;

                   
                    System.out.println("Please enter your movement : "); //first movement choice
                    System.out.println("0=Draw a card,1=Play cards");
                    in();
                    while(!complete)
                    {

                        if(input.compareToIgnoreCase("0") == 0){  //draw a card
                            complete=true;
                            gameInfo.playerMovement=PlayerMovement.Pick_One_Card;
                            try{
                                oos.writeObject(gameInfo);
                            }
                            catch(Exception e){
                            System.out.println("Got something wrong QAQ");
                            }

                        }
                        
                        if(input.compareToIgnoreCase("1") == 0){ //play cards
                            boolean donesth=false;
                            int cardplace;
                            int cardgroup;
                            while(!complete)
                            {
                                System.out.println("Please enter your movement : "); //second movement choice
                                System.out.println("0=play a card,1=Move a card,2=End your movement,3=roll back");
                                in();

                                if(input.compareToIgnoreCase("0") == 0) //play a card
                                {
                                    boolean rightcardtoplay=false;
                                    
                                    while(!rightcardtoplay) //right card?
                                    {
                                        System.out.println("Which card would you play? ");
                                        System.out.println("(The one in the first row on the far left is card 0,and so on.");
                                        input=scanner.next();
                                        cardplace = Integer.parseInt(input);

                                        while(cardplace<0 || cardplace>=gameInfo.playersHand.size()) //QvQ
                                        {
                                            plz();
                                            in();
                                            cardplace = Integer.parseInt(input);
                                        }

                                        System.out.println("Is"+gameInfo.playersHand.get(cardplace).toString()+"the card you want to play?");
                                        System.out.println("0=No,1=Yes.");
                                        in();
                                        while(true)
                                        {
                                            if(input.compareToIgnoreCase("0") == 1){
                                                rightcardtoplay=true;
                                                break;
                                            } 
                                            else if(input.compareToIgnoreCase("0") == 0){
                                                rightcardtoplay=false;
                                                System.out.println("Please select the card again.");
                                                break;
                                            } 
                                            else plz();
                                        }
                                    }  //right card end

                                    boolean rightgroup=false;     //right place?
                                    while(!rightgroup){
                                        System.out.println("Which group do you want to place this card?");
                                        System.out.println("(-1=a new gruop)");
                                        in();
                                        cardgroup = Integer.parseInt(input);
                                        while(cardgroup<-1 || cardgroup>=gameInfo.copy.cardGroups.size())
                                        {
                                            plz();
                                            in();
                                            cardgroup = Integer.parseInt(input);
                                        }

                                        System.out.println("Is"+"("+cardgroup+")"+"the group you want to place the card?");
                                        System.out.println("0=No,1=Yes.");
                                        in();
                                        while(true)
                                        {
                                            if(input.compareToIgnoreCase("0") == 1){
                                                rightgroup=true;
                                                break;
                                            } 
                                            else if(input.compareToIgnoreCase("0") == 0){
                                                rightgroup=false;
                                                System.out.println("Please select the gruop again.");
                                                break;
                                            } 
                                            else plz();
                                        }
                                    }


                                }
                                if(input.compareToIgnoreCase("1") == 0){ //move a card

                                    if(!gameInfo.isBreakedTheIce){ //check ice
                                        System.out.println("Sorry,you can't move cards before you had breaked the ice.");
                                        break;
                                    } //check ice end


                                }
                            }
                        }
                        else plz();

                    }
                    if(complete){

                    }
                }
                else if(gameInfo.message==GameMessage.PLAYER1s_TURN){
                    System.out.println("It's player1's turn.");
                }

                else if(gameInfo.message==GameMessage.PLAYER2s_TURN){
                    System.out.println("It's player2's turn.");
                }

                else if(gameInfo.message==GameMessage.PLAYER3s_TURN){
                    System.out.println("It's player3's turn.");
                }

                else if(gameInfo.message==GameMessage.PLAYER4s_TURN){
                    System.out.println("It's player4's turn.");
                }
                else {System.out.println("Error.");};

            }catch (Exception e) {
                System.out.println("Got something wrong QAQ");
                System.out.println(e);
            }
        }

        
        
        
        
    }

    public void in()
    {
        in();
    }
    public void plz()
    {
        plz();
    }


    }

 