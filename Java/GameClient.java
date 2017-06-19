
import java.io.*;
import java.net.*;
import java.util.*;

public class GameClient {

    GameInfo gameInfo = null;
    String input = "";
    Scanner scanner = new Scanner(System.in);
    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;
    Socket server = null;

    public void start(String IP) {
        System.out.println("gc running");
        String host = IP;
        int port = 8888;

        try {
            server = new Socket(host, port);
            System.out.println("The connection is successful.");
            System.out.println("Waiting for other players...");

            oos = new ObjectOutputStream(server.getOutputStream());
            ois = new ObjectInputStream(server.getInputStream());

        } catch (IOException e) {
            System.out.println("Client connection error.");
            closeProgram();
            System.exit(1);
        }
        boolean tttttttttttice = false;
        while (true) {
            try {
                readgameinfo();
                int lastplayer = 0;
                boolean hadplayedcard=false;

                if (gameInfo.message == GameMessage.YOUR_TURN) {
                    System.out.println("It's your turn =D"); //your turn
                    //rules
                    System.out.println("Here's the rule for inputting the card (used when moving a card) : ");
                    System.out.println("First,enter the number of the card.");
                    System.out.println("Second,enter the color of the card,split them with blank space.");
                    System.out.println("(Use the first letter(lower case) of colors' names to represent the color of the card.");
                    System.out.println("For example,the r letter represents color red,and the b letter represents color blue)");

                    System.out.println(">>Number of cards in the card pile : " + gameInfo.remainCardPileNumber + "\n"); //cardpile

                    showcardgroups(); //card groups

                    showhands(); //hands

                    gameInfo.isBreakedTheIce = tttttttttttice;

                    if (!gameInfo.isBreakedTheIce) //ice
                    {
                        System.out.println("You have NOT breaked the ice.");
                    }

                    boolean complete = false;
                    boolean rollback = false;
                    PlayerMovement movement = PlayerMovement.Roll_Back;
                    boolean inputck1 = false;
                    boolean inputck2 = false;
                    boolean inputck3 = false;

                    System.out.println("Please enter your movement : "); //first movement choice
                    System.out.println("0=Draw a card,1=Play cards");

                    while (!complete) {

                        in();

                        if (input.compareToIgnoreCase("0") == 0 ) { //draw a card

                            complete = true;
                            gameInfo.playerMovement = PlayerMovement.Pick_One_Card;
                            writegameinfo();

                            if(gameInfo.remainCardPileNumber!=1){
                                readgameinfo();
                                System.out.println("This turn ends.");
                                showhands();
                            }
                            break;
                        }
                        

                        else if (input.compareToIgnoreCase("1") == 0) { //got cards to play
                            int cardplayedtotalnumber = 0;

                            int originalhandsnumber = gameInfo.playersHand.size();
                            int cardplace = -2;
                            int cardgroup = 0;
                            Card cardtoplay = null;
                            boolean playcardcomplete = false;
                            int initialgroupnumber = gameInfo.copy.cardGroups.size() - 1;
                            //boolean wanttoendplaycard = false;
                            while (!playcardcomplete) {
                                System.out.println("Please enter your movement : "); //second movement choice
                                System.out.println("0=Play a card,1=Move a card,2=End your movement,3=Roll back");
                                in();
                                if(hadplayedcard) originalhandsnumber++;
                                if (input.compareToIgnoreCase("0") == 0) //play a card
                                {
                                    boolean rightcardtoplayandplace = false;

                                    while (!rightcardtoplayandplace) //get card and place
                                    {

                                        System.out.println("Which card would you like to play? "); //which card
                                        System.out.println("(The card on the top is card 0,and so on.)");
                                        in();
                                        cardplace = ti();

                                        while (cardplace < 0 || cardplace >= gameInfo.playersHand.size()) //QvQ
                                        {
                                            plz();
                                            in();
                                            cardplace = ti();
                                        }

                                        gameInfo.isBreakedTheIce = tttttttttttice;
                                        if (!gameInfo.isBreakedTheIce
                                                && gameInfo.playersHand.get(cardplace).color == CardColor.SMILE) {
                                            System.out
                                                    .println("Sorry,you cannot play a joker before breaking the ice.");
                                            plz();
                                        }

                                        System.out.println("Which group do you want to place this card?"); //which group
                                        System.out.println("(-1=a new gruop)");
                                        in();
                                        cardgroup = ti();
                                        inputck1=false;
                                        while (!inputck1) {
                                            if (cardgroup < -1 || cardgroup >= gameInfo.copy.cardGroups.size()) {
                                                System.out.println("Illegal group.");
                                                plz();
                                                in();
                                                cardgroup = ti();
                                            }
                                            else inputck1=true;
                                            if (cardgroup != -1 && cardgroup <= initialgroupnumber
                                                    && !gameInfo.isBreakedTheIce) {
                                                System.out.println(
                                                        "Sorry,you cannot lay off cards before breaking the ice.");
                                                plz();
                                                in();
                                                cardgroup = ti();
                                            } else if(inputck1)
                                                break;
                                        }

                                        if (cardgroup != -1) {
                                            System.out.println("put " + gameInfo.playersHand.get(cardplace).toString()
                                                    + " to group" + cardgroup + ",is that right?");
                                            System.out.println("0=No,1=Yes.");
                                        } else {
                                            System.out.println("put " + gameInfo.playersHand.get(cardplace).toString()
                                                    + " to a new group ,is that right?");
                                            System.out.println("0=No,1=Yes.");
                                        }
                                        inputck3=false;
                                        while (!inputck3) {
                                            in();
                                            if (input.compareToIgnoreCase("1") == 0) {
                                                rightcardtoplayandplace = true;
                                                break;
                                            } else if (input.compareToIgnoreCase("0") == 0) {
                                                rightcardtoplayandplace = false;
                                                System.out.println("Please select the card and the group again.");
                                                inputck3=true;
                                                break;
                                            } else
                                                plz();
                                        }

                                    } //right card and place
                                    if (cardgroup == -1) { //create new card group
                                        gameInfo.copy.cardGroups.add(new ArrayList<Card>());
                                        cardgroup = gameInfo.copy.cardGroups.size() - 1;
                                    }
                                    cardtoplay = gameInfo.playersHand.get(cardplace); //get card to play
                                    gameInfo.copy.cardGroups.get(cardgroup).add(cardtoplay); //add card to play
                                    gameInfo.playersHand.remove(cardplace); //delete card to play in hands

                                    if (cardtoplay.color != CardColor.SMILE) { //sort
                                        sort(cardgroup);
                                    } else {
                                        sortjoker(cardgroup); //sort joker
                                    }

                                    gameInfo.playerMovement = PlayerMovement.Out_Of_A_Card; //send gameinfo
                                    writegameinfo();
                                    readgameinfo();
                                    System.out.println("\n" + "Card played." + "\n");
                                    cardplayedtotalnumber += cardtoplay.number;
                                    showcardgroups();
                                    showhands();
                                } 

                                else if (input.compareToIgnoreCase("1") == 0) { //move a card

                                    int fromgroup = 0;
                                    int togroup = 0;
                                    CardColor cardtomovecolor = null;
                                    int cardtomovenumber = 0;
                                    Card cardtomove = null;
                                    int cardtomoveindex = -1;

                                    gameInfo.isBreakedTheIce = tttttttttttice;
                                    if (!gameInfo.isBreakedTheIce) { //check ice
                                        System.out.println(
                                                "Sorry,you can't move cards before you had breaked the ice." + "\n");
                                        plz();
                                        continue;
                                    } //check ice end

                                    boolean rightcardtomove = false;
                                    while (!rightcardtomove) { // get all moving information 
                                        System.out.println("Which card group contains the card you want to move?"); //from which group
                                        in();
                                        fromgroup = ti();
                                        while (fromgroup < 0 || fromgroup >= gameInfo.copy.cardGroups.size()) {
                                            plz();
                                            in();
                                            fromgroup = ti();
                                        }
                                        inputck1=false;
                                        while (!inputck1) { //which card
                                            System.out.println("Which card in this group would you like to move?"); //which card
                                            System.out.println("what number is the card ex: 1 r\nex: 2 b\nex: 3 g\nex: 4 y\nex: 0 s");
                                            in();
                                            
                                            cardtomovenumber = ti();
                                            in();
                                            cardtomovecolor = getcolor(input);
                                            if (cardtomovecolor != CardColor.SMILE) { //QvQ
                                                if (cardtomovenumber <= 0 || cardtomovenumber > 13) {
                                                    System.out.println(
                                                            "Illegal card number or color,please try again." + "\n");
                                                    inputck1 = false;
                                                }
                                            } else if (cardtomovecolor == CardColor.SMILE) {
                                                cardtomovenumber = 0;
                                                inputck1 = true;
                                            }
                                            cardtomove = new Card(cardtomovenumber, cardtomovecolor);
                                            //System.out.println(cardtomove.toString());
                                            int recordnumber = 0;
                                            for (Card card : gameInfo.copy.cardGroups.get(fromgroup)) {
                                                if (card.number == cardtomove.number
                                                        && card.color == cardtomove.color) {
                                                    //System.out.println("Card found.");
                                                    inputck1 = true;
                                                    cardtomoveindex = recordnumber;
                                                    break;
                                                }
                                                recordnumber++;
                                            }
                                            if (!inputck1) {
                                                System.out.println("Sorry,this card is not in this group.");
                                                plz();
                                            }

                                        }
                                        inputck1 = false;

                                        while (!inputck1) {
                                            System.out.println("Which card group would you like to place this card?");
                                            System.out.println("(-1=a new group)");
                                            in();
                                            togroup = ti();
                                            while (togroup < -1 || togroup >= gameInfo.copy.cardGroups.size()) {
                                                plz();
                                                in();
                                                fromgroup = ti();
                                            }
                                            inputck1 = true;
                                        }
                                        System.out.print(
                                                "Move" + cardtomove.toString() + "in the (" + fromgroup + ")group"); //check right card
                                        System.out.println(" to (" + togroup + ")group,is this right?");
                                        System.out.println("0=No,1=Yes.");
                                        while (true) //QvQ
                                        {
                                            in();
                                            if (input.compareToIgnoreCase("1") == 0) {
                                                rightcardtomove = true;
                                                break;
                                            } else if (input.compareToIgnoreCase("0") == 0) {
                                                rightcardtomove = false;
                                                System.out.println("Please select the card again.");
                                                break;
                                            } else
                                                plz();
                                        }
                                    }
                                    gameInfo.copy.cardGroups.get(togroup).add(cardtomove);
                                    gameInfo.copy.cardGroups.get(fromgroup).remove(cardtomoveindex);
                                    if (cardtomove.color != CardColor.SMILE) {
                                        sort(togroup);
                                    } else {
                                        sortjoker(togroup);
                                    }
                                    organizecardpile();
                                    gameInfo.playerMovement = PlayerMovement.Move_One_Card;
                                    writegameinfo();
                                    readgameinfo();
                                    showcardgroups();
                                    showhands();
                                }

                                else if (input.compareToIgnoreCase("2") == 0) { //end move

                                    gameInfo.isBreakedTheIce = tttttttttttice;
                                    if (gameInfo.playersHand.size() == originalhandsnumber) {
                                        System.out.println(
                                                "Sorry,you cannot end your movement without playing any card.");
                                        plz();
                                    }

                                    else if (!gameInfo.isBreakedTheIce) {
                                        if (cardplayedtotalnumber < 30) {
                                            System.out.println(
                                                    "Sorry,the total number of cards you played must be greater or equal to 30 to break the ice.");
                                            plz();

                                        } else {
                                            gameInfo.isBreakedTheIce = true;
                                            tttttttttttice = true;
                                            System.out.println("Are you sure you have complete all movements?");
                                            System.out.println("0=No,1=Yes.");
                                            while (true) //QvQ
                                            {
                                                in();
                                                if (input.compareToIgnoreCase("1") == 0) {
                                                    gameInfo.playerMovement = PlayerMovement.Confirm;
                                                    writegameinfo();
                                                    readgameinfo();
                                                    if (gameInfo.message == GameMessage.CONFIRM_FAIL) {
                                                        System.out.println(
                                                                "Sorry,there are some illegal card groups,please alter them.");
                                                        hadplayedcard=true;
                                                        readgameinfo();
                                                        break;
                                                    } else if (gameInfo.message == GameMessage.CONFIRM_OK) {
                                                        playcardcomplete = true;
                                                        complete = true;
                                                        System.out.println("This turn ends.");

                                                        showhands();
                                                        break;
                                                    } else if (gameInfo.message == GameMessage.YOU_WIN) {
                                                        System.out.println("\n" + "You win.");
                                                        closeProgram();
                                                        System.exit(1);
                                                    }
                                                    break;
                                                } else if (input.compareToIgnoreCase("0") == 0) {
                                                    break;
                                                } else
                                                    plz();
                                            }
                                        }

                                    } else {
                                        System.out.println("Are you sure you have complete all movements?");
                                        System.out.println("0=No,1=Yes.");
                                        while (true) //QvQ
                                        {
                                            in();
                                            if (input.compareToIgnoreCase("1") == 0) {
                                                gameInfo.playerMovement = PlayerMovement.Confirm;
                                                writegameinfo();
                                                readgameinfo();
                                                if (gameInfo.message == GameMessage.CONFIRM_FAIL) {
                                                    System.out.println(
                                                            "Sorry,there are some illegal card groups,please alter them.");
                                                    readgameinfo();
                                                    break;
                                                } else if (gameInfo.message == GameMessage.CONFIRM_OK) {
                                                    playcardcomplete = true;
                                                    complete = true;
                                                    System.out.println("This turn ends.");

                                                    showhands();
                                                    break;
                                                } else if (gameInfo.message == GameMessage.YOU_WIN) {
                                                    System.out.println("\n" + "You win.");
                                                    closeProgram();
                                                    System.exit(1);
                                                }
                                                break;
                                            } else if (input.compareToIgnoreCase("0") == 0) {
                                                break;
                                            } else
                                                plz();
                                        }
                                    }
                                }

                                else if (input.compareToIgnoreCase("3") == 0) {
                                    System.out.println(
                                            "All movement in this turn will reset,are you sure that you want to roll back?");
                                    System.out.println("0=No,1=Yes.");
                                    while (true) //QvQ
                                    {
                                        in();
                                        if (input.compareToIgnoreCase("1") == 0) {
                                            gameInfo.playerMovement = PlayerMovement.Roll_Back;
                                            writegameinfo();
                                            rollback = true;
                                            playcardcomplete = true;
                                            complete = true;
                                            break;
                                        } else if (input.compareToIgnoreCase("0") == 0) {
                                            break;
                                        } else
                                            plz();
                                    }
                                } else
                                    plz();
                            }
                        }

                        else
                            plz();

                    }

                    if (rollback) {
                        System.out.println("\n" + "Restart this turn : " + "\n");
                        rollback = false;
                        complete = false;
                    } //your turn ends

                } else if (gameInfo.message == GameMessage.PLAYER1s_TURN) {
                    System.out.println("It's player1's turn." + "\n");
                }

                else if (gameInfo.message == GameMessage.PLAYER2s_TURN) {
                    System.out.println("It's player2's turn." + "\n");
                }

                else if (gameInfo.message == GameMessage.PLAYER3s_TURN) {
                    System.out.println("It's player3's turn." + "\n");
                }

                else if (gameInfo.message == GameMessage.PLAYER4s_TURN) {
                    System.out.println("It's player4's turn." + "\n");
                } else if (gameInfo.message == GameMessage.YOU_LOSE) {
                    //System.out.println("\n" + "player" + lastplayer + " wins.");
                    System.out.println("You lose.");

                    closeProgram();
                    System.exit(1);
                } else if (gameInfo.message == GameMessage.YOU_WIN){
                    System.out.println("You win.");

                    closeProgram();
                    System.exit(1);
                }
                else {
                    System.out.println("Error.");
                }
                

            } catch (Exception e) {
                System.out.println("Got something wrong QAQ");
                System.out.println(e);
                e.printStackTrace();
                closeProgram();
                System.exit(1);
            }
        }
    }

    public void showcardgroups() {
        System.out.println(">>Card groups : "); //cardgroup
        int cardgroupnumbering = 0;
        int cardsingroupnumbering = 0;
        String s = "";
        for (ArrayList<Card> group : gameInfo.copy.cardGroups) {
            cardsingroupnumbering = 0;
            s += "(" + Integer.toString(cardgroupnumbering) + ") ";
            for (Card card : group) {
                cardsingroupnumbering++;
                s += card.toString();
                if (cardsingroupnumbering != group.size()) {
                    s += ",";
                }
            }
            cardgroupnumbering++;
            s += "\n";
        }
        System.out.println(s + "\n");
    }

    public void showhands() {
        sorthands();
        System.out.println(">>Your hands : "); //playerhands
        String s = "";
        int handsnumbering = 0;
        for (Card card : gameInfo.playersHand) {
            s += "(" + Integer.toString(handsnumbering) + ") " + card.toString() + ",";
            handsnumbering++;
            if (handsnumbering % 5 == 0 || handsnumbering != 0) {
                s += "\n";
            }
        }
        s += "\n";
        System.out.println(s);
    }

    public void sorthands() {
        Card temp = null;
        int handsnumber = gameInfo.playersHand.size();
        for (int i = 0; i < handsnumber; i++) { //sort number
            for (int j = 0; j < handsnumber - i - 1; j++) {
                if (gameInfo.playersHand.get(j).number < gameInfo.playersHand.get(j + 1).number) {
                    Collections.swap(gameInfo.playersHand, j, j + 1);
                }
            }
        }

        // sort color
        for (int i = 0; i < handsnumber; i++) {
            if (gameInfo.playersHand.get(i).color == CardColor.GREEN) {
                temp = gameInfo.playersHand.get(i);
                gameInfo.playersHand.add(0, temp);
                gameInfo.playersHand.remove(i + 1);
            }
        }
        for (int i = 0; i < handsnumber; i++) {
            if (gameInfo.playersHand.get(i).color == CardColor.BLUE) {
                temp = gameInfo.playersHand.get(i);
                gameInfo.playersHand.add(0, temp);
                gameInfo.playersHand.remove(i + 1);
            }
        }
        for (int i = 0; i < handsnumber; i++) {
            if (gameInfo.playersHand.get(i).color == CardColor.YELLOW) {
                temp = gameInfo.playersHand.get(i);
                gameInfo.playersHand.add(0, temp);
                gameInfo.playersHand.remove(i + 1);
            }
        }
        for (int i = 0; i < handsnumber; i++) {
            if (gameInfo.playersHand.get(i).color == CardColor.RED) {
                temp = gameInfo.playersHand.get(i);
                gameInfo.playersHand.add(0, temp);
                gameInfo.playersHand.remove(i + 1);
            }
        }

    }

    public void in() {
        while (true) {
            if (scanner.hasNext()) {
                input = scanner.next();
                break;
            }
        }

    }

    public void plz() {
        System.out.println("Pleae try again." + "\n");
    }

    public int ti() {
        return Integer.parseInt(input);
    }

    public CardColor getcolor(String in) {
        if (in.compareToIgnoreCase("r") == 0) {
            return CardColor.RED;
        } else if (in.compareToIgnoreCase("y") == 0) {
            return CardColor.YELLOW;
        } else if (in.compareToIgnoreCase("b") == 0) {
            return CardColor.BLUE;
        } else if (in.compareToIgnoreCase("g") == 0) {
            return CardColor.GREEN;
        } else if (in.compareToIgnoreCase("j") == 0) {
            return CardColor.SMILE;
        } else {

            return null;
        }
    }

    public void readgameinfo() {
        try {
            gameInfo = (GameInfo) ois.readObject();
        } catch (Exception e) {
            System.out.println("Fail to get game information.");
            e.printStackTrace();
            closeProgram();
            System.exit(1);
        }

    }

    public void writegameinfo() {
        try {
            oos.writeObject(gameInfo);
        } catch (Exception e) {
            System.out.println("Have trouble with sending game information.");
            closeProgram();
        }

    }

    public void sort(int group) {

        if (ispair(group)) {
            //
            return;
        }
        int cardplayedindex = gameInfo.copy.cardGroups.get(group).size() - 1;
        int putindex = cardplayedindex;
        int jokeroneindex = -1;
        int jokertwoindex = -1;
        Card newcard = gameInfo.copy.cardGroups.get(group).get(cardplayedindex);

        if (gameInfo.copy.cardGroups.get(group).size() == 2) { //only 2 cards
            if (gameInfo.copy.cardGroups.get(group).get(0).number > gameInfo.copy.cardGroups.get(group).get(1).number) {
                //System.out.println("got only two cards in this group.");
                putindex = 0;
            }
        }

        for (int i = 0; i < gameInfo.copy.cardGroups.get(group).size() - 2; i++) { //sort XDDDDDDDDDDDDDDDD
            if (gameInfo.copy.cardGroups.get(group).get(i).number == 0
                    && gameInfo.copy.cardGroups.get(group).get(i + 1).number != 0) { //i=0
                if (i == 0) { //i=0 is the first card
                    if (gameInfo.copy.cardGroups.get(group).get(i + 1).number >= gameInfo.copy.cardGroups.get(group)
                            .get(cardplayedindex).number) {
                        jokeroneindex = 0;
                        putindex = 1;
                        break;
                    }
                } else if (gameInfo.copy.cardGroups.get(group).get(i + 1).number >= gameInfo.copy.cardGroups.get(group)
                        .get(cardplayedindex).number
                        && gameInfo.copy.cardGroups.get(group).get(i - 1).number <= gameInfo.copy.cardGroups.get(group)
                                .get(cardplayedindex).number) {
                    if (gameInfo.copy.cardGroups.get(group).get(i + 1).number
                            - 2 == gameInfo.copy.cardGroups.get(group).get(i - 1).number) {
                        jokeroneindex = i + 1; //considerate
                    }
                    putindex = i; //i=0,not the first card
                    break;
                }
            }

            else if (gameInfo.copy.cardGroups.get(group).get(i + 1).number == 0
                    && gameInfo.copy.cardGroups.get(group).get(i).number != 0) { //i+1 is 0
                if (i == 0) {
                    if (gameInfo.copy.cardGroups.get(group).get(i).number >= gameInfo.copy.cardGroups.get(group)
                            .get(cardplayedindex).number) {
                        putindex = 0;
                        break;
                    }
                } else if (i + 2 < gameInfo.copy.cardGroups.get(group).size() - 1) {//i+1=0 is not the last card
                    if (gameInfo.copy.cardGroups.get(group).get(i + 2).number >= gameInfo.copy.cardGroups.get(group)
                            .get(cardplayedindex).number
                            && gameInfo.copy.cardGroups.get(group).get(i).number <= gameInfo.copy.cardGroups.get(group)
                                    .get(cardplayedindex).number) {
                        putindex = i + 1;
                        jokeroneindex = i + 2;
                    }
                }
                //i+1 is last 
                else if (gameInfo.copy.cardGroups.get(group).get(i).number
                        + 1 == gameInfo.copy.cardGroups.get(group).get(cardplayedindex).number) { //card can make group complete
                    putindex = i + 1; //no need to sort joker
                    break;
                }
            } else if (gameInfo.copy.cardGroups.get(group).get(i + 1).number == 0
                    && gameInfo.copy.cardGroups.get(group).get(i).number == 0) { //both 0
                if (i == 0) {
                    if (gameInfo.copy.cardGroups.get(group).get(i + 2).number >= gameInfo.copy.cardGroups.get(group)
                            .get(cardplayedindex).number) {
                        putindex = 2; //2*0,first
                        jokeroneindex = 0;
                        jokertwoindex = 1;
                        break;
                    }
                } else if (i + 2 < gameInfo.copy.cardGroups.get(group).size() - 1) {
                    if (gameInfo.copy.cardGroups.get(group).get(i + 2).number >= gameInfo.copy.cardGroups.get(group)
                            .get(cardplayedindex).number
                            && gameInfo.copy.cardGroups.get(group).get(i - 1).number <= gameInfo.copy.cardGroups
                                    .get(group).get(cardplayedindex).number) {
                        putindex = i; //2*0,no first ,not reach end
                        jokeroneindex = i + 1;
                        jokertwoindex = i + 2;
                        break;
                    }
                }
            } else if (i == 0) { //no 0,i=0
                //System.out.println("no 0,i=0");
                if (gameInfo.copy.cardGroups.get(group).get(i).number >= gameInfo.copy.cardGroups.get(group)
                        .get(cardplayedindex).number) {
                    putindex = 0;
                    break;
                } else if (i + 1 == gameInfo.copy.cardGroups.get(group).size() - 2) {
                    if (gameInfo.copy.cardGroups.get(group).get(i + 1).number >= gameInfo.copy.cardGroups.get(group)
                            .get(cardplayedindex).number
                            && gameInfo.copy.cardGroups.get(group).get(i).number <= gameInfo.copy.cardGroups.get(group)
                                    .get(cardplayedindex).number) {
                        putindex = i + 1;
                        break;
                    }
                }
            } else if (i + 1 < gameInfo.copy.cardGroups.get(group).size() - 1) {//no 0,no first,no end,check
                // System.out.println("no 0,no first,no end,check");
                if (gameInfo.copy.cardGroups.get(group).get(i + 1).number >= gameInfo.copy.cardGroups.get(group)
                        .get(cardplayedindex).number
                        && gameInfo.copy.cardGroups.get(group).get(i).number <= gameInfo.copy.cardGroups.get(group)
                                .get(cardplayedindex).number) {
                    putindex = i + 1;
                    break;
                }
            }
        }
        gameInfo.copy.cardGroups.get(group).add(putindex, newcard);
        gameInfo.copy.cardGroups.get(group).remove(cardplayedindex + 1);
        if (jokeroneindex >= 0) {
            if (jokertwoindex >= 0) {
                gameInfo.copy.cardGroups.get(group).add(new Card(0, CardColor.SMILE));
                gameInfo.copy.cardGroups.get(group).add(new Card(0, CardColor.SMILE));
                gameInfo.copy.cardGroups.get(group).remove(jokertwoindex);
                gameInfo.copy.cardGroups.get(group).remove(jokeroneindex);
                sortjoker(group);
                sortjoker(group);
            } else {
                gameInfo.copy.cardGroups.get(group).add(new Card(0, CardColor.SMILE));
                gameInfo.copy.cardGroups.get(group).remove(jokeroneindex);
                sortjoker(group);
            }
        }

        return;
    }

    public void sortjoker(int group) {
        if (ispair(group))
            return;
        int putindex = 0;
        int cardplayedindex = gameInfo.copy.cardGroups.get(group).size() - 1;
        Card newcard = gameInfo.copy.cardGroups.get(group).get(cardplayedindex);

        if (gameInfo.copy.cardGroups.get(group).size() > 2) { //get outindex
            if (ifstraightlegal(group) == -1 && gameInfo.copy.cardGroups.get(group).get(0).number == 1) {
                putindex = gameInfo.copy.cardGroups.get(group).size() - 1;
            } else
                putindex = ifstraightlegal(group);
        }
        gameInfo.copy.cardGroups.get(group).add(putindex, newcard);
        gameInfo.copy.cardGroups.get(group).remove(cardplayedindex + 1);

        return;
    }

    public void organizecardpile() {
        int groupindex = 0;
        for (ArrayList<Card> group : gameInfo.copy.cardGroups) {
            if (group.size() == 0) {
                gameInfo.copy.cardGroups.remove(groupindex);
            }
            groupindex++;
        }
    }

    public int ifstraightlegal(int group) {
        int cardingroupnumber = gameInfo.copy.cardGroups.get(group).size() - 1;
        int lackindex = -1;
        if (cardingroupnumber < 2)
            return -2;
        for (int i = 0; i < cardingroupnumber; i++) { //check
            if (gameInfo.copy.cardGroups.get(group).get(i).number == 0
                    && gameInfo.copy.cardGroups.get(group).get(i + 1).number != 0) { //i=0
                if (i == 0) { //i=0 is the first card
                } else if (gameInfo.copy.cardGroups.get(group).get(i + 1).number
                        - 2 != gameInfo.copy.cardGroups.get(group).get(i - 1).number) {
                    lackindex = i; //i=0,not the first card
                    break;
                }
            }

            else if (gameInfo.copy.cardGroups.get(group).get(i + 1).number == 0
                    && gameInfo.copy.cardGroups.get(group).get(i).number != 0) { //i+1 is 0
                if (i + 2 <= cardingroupnumber) {//i+1=0 is the not last card
                    if (gameInfo.copy.cardGroups.get(group).get(i + 2).number
                            - 2 != gameInfo.copy.cardGroups.get(group).get(i).number) {
                        lackindex = i + 1;
                        break;
                    }
                }
            }

            else if (gameInfo.copy.cardGroups.get(group).get(i + 1).number == 0
                    && gameInfo.copy.cardGroups.get(group).get(i).number == 0) { //both 0
                if (i == 0) {
                    i++;
                } else if (i + 2 < gameInfo.copy.cardGroups.get(group).size() - 1) { //i+1 is not last 
                    if (gameInfo.copy.cardGroups.get(group).get(i + 2).number
                            - 3 != gameInfo.copy.cardGroups.get(group).get(i - 1).number) {
                        lackindex = i + 1; //2*0,not reach end
                        break;
                    }
                }
            }

            else if (i + 1 <= cardingroupnumber) {//no 0,no end,check
                if (gameInfo.copy.cardGroups.get(group).get(i + 1).number
                        - 1 != gameInfo.copy.cardGroups.get(group).get(i).number) {
                    lackindex = i + 1;
                }
            }
        }
        return lackindex;
    }

    public boolean ispair(int group) {
        boolean ispair = true;
        for (int i = 1; i < gameInfo.copy.cardGroups.get(group).size(); i++) {
            if (gameInfo.copy.cardGroups.get(group).get(i).number != 0
                    && gameInfo.copy.cardGroups.get(group).get(i - 1).number != 0) {
                if (gameInfo.copy.cardGroups.get(group).get(i).number != gameInfo.copy.cardGroups.get(group)
                        .get(i - 1).number) {
                    ispair = false;
                    break;
                }
            }
        }
        return ispair;
    }

    public void closeProgram() {
        try {
            oos.close();
            ois.close();
            server.close();
        } catch (Exception e) {

        } finally {
            System.exit(1);
        }

    }
}
