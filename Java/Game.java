import java.io.*;
import java.util.*;



public class Game {
    //this class designed for implementing rummikub game logic;

    ArrayList<Card> cardPile = new ArrayList<>();
    ArrayList<ArrayList<Card>> cardGroups = new ArrayList<>();
    ArrayList<GameSnapshot> gameHistory = new ArrayList<>();
    ArrayList<GamePlayer> players = new ArrayList<>();
    int playerNum = 0;

    public Game(ArrayList<GamePlayer> players) {
        this.players = players;
        playerNum = players.size();
    }

    private void init() {
        generateCards();
        disturbCards();
    }

    private void generateCards() {
        for (int j = 0; j < 2; j++) {
            for (int i = 1; i <= 13; i++) {
                cardPile.add(new Card(i, CardColor.RED));
            }
            for (int i = 1; i <= 13; i++) {
                cardPile.add(new Card(i, CardColor.YELLOW));
            }
            for (int i = 1; i <= 13; i++) {
                cardPile.add(new Card(i, CardColor.BLUE));
            }
            for (int i = 1; i <= 13; i++) {
                cardPile.add(new Card(i, CardColor.GREEN));
            }
        }
        cardPile.add(new Card(0, CardColor.SMILE));
        cardPile.add(new Card(0, CardColor.SMILE));
    }

    private void disturbCards() {
        Random random = new Random();
        for (int i = 0; i < 106; i++) {
            Card card1 = cardPile.get(random.nextInt(106));
            cardPile.remove(card1);
            cardPile.add(card1);
        }
    }

    private void deal() {
        //deal

        for (int i = 0; i < 14; i++) {
            for (GamePlayer player : players) {
                Card card = cardPile.get(0);
                cardPile.remove(card);
                player.hand.add(card);
            }
        }

    }

    private GameInfo generateServerToClientGameInfo(GameMessage message, GamePlayer player) {
        GameInfo gameInfo = new GameInfo();
        gameInfo.sender = "server";
        gameInfo.message = message;
        gameInfo.copy = new GameSnapshot(cardPile, cardGroups, players);
        gameInfo.copy.cardPile = new ArrayList<Card>();
        gameInfo.copy.hands = new ArrayList<ArrayList<Card>>();
        gameInfo.remainCardPileNumber = cardPile.size();
        gameInfo.isBreakedTheIce = player.isBreakedTheIce;
        gameInfo.playersHand = (ArrayList<Card>) player.hand.clone();
        return gameInfo;

    }

    private boolean isTheMovementLegally(GamePlayer player, GameSnapshot nowGameSnapshot) {
        //todo
        // return true;
        System.out.println("check the movement legally");
        int indexOfThisPlayer = players.indexOf(player);

        boolean playersHandHasChange = (player.hand.size() != nowGameSnapshot.hands.get(indexOfThisPlayer).size());
        //if hand never change then this step is illegal
        if (!playersHandHasChange) {
            return false;
        } else {
            //if the player hasn't broke the ice , total point of cards which sends out from hand need more or equal 30
            if (!player.isBreakedTheIce) {
                ArrayList<Card> lastHand = (ArrayList<Card>) nowGameSnapshot.hands.get(indexOfThisPlayer).clone();
                ArrayList<Card> newHand = (ArrayList<Card>) player.hand.clone();
                for (int i = 0; i < lastHand.size(); i++) {
                    for (Card newCard : newHand) {
                        if (lastHand.get(i).equals(newCard)) {
                            lastHand.remove(i);
                            i--;
                        }
                    }
                }
                int totalPoint = 0;
                for (Card c : lastHand) {
                    totalPoint += c.number;
                }
                if (totalPoint < 30) {
                    System.out.println("not break the ice");
                    return false;
                }

            }
            if (!isAllCardGroupsLegally()) {
                System.out.println("the movement is not legal");
                return false;
            }
        }
        System.out.println("the movement is legal");
        return true;
    }

    private boolean isAllCardGroupsLegally() {
        for (ArrayList<Card> cardGroup : cardGroups) {
            ArrayList<Card> cardGroupCopy = (ArrayList<Card>) cardGroup.clone();
            System.out.println("check the cardGroup");
            for (Card c : cardGroup) {
                System.out.print(c + ",");
            }
            System.out.println();

            if (cardGroup.size() < 3) {
                System.out.println("cardGroup size lower than 3");
                return false;
            }
            int numberOfSmileCards = 0;
            //get number of smile cards
            for (Card card : cardGroup) {
                if (card.color == CardColor.SMILE) {
                    numberOfSmileCards++;
                }

            }
            //same color and number is continuous
            if (isCardsSameColor(cardGroup)) {
                //it's impossible the qty of cards which has same color and continuous number in a group more than 13 
                if (cardGroup.size() > 13) {
                    System.out.println("cardGroup size higher than 13");
                    return false;
                }
                
                // the algorithm is explain below
                // if we can promise that the number will not duplicate
                // we can get min and max number of cards in the card group
                // then we can get difference,which is value x, of max and min and size,which is value y, of card group
                // value x is how many cards should be in the card group,so we need add 1 to correct 
                // then we can get difference,which is value z, of y and x
                // if z does not smaller than the number of smile card mean that we don't have enough smile card to fill the vacancy of the number series
                HashSet<Integer> set = new HashSet<>();
                for(Card c:cardGroupCopy){
                    if(!set.add(c.number)){
                        System.out.println("cardGroup should be series has duplicate numbers");
                        return false;
                    }
                }
                
                int max = 0; //the lowest number of card is 1
                int min = 14; // the highest number of card is 13
                for (Card c : cardGroupCopy) {
                    //the number of smile card do not care
                    if (c.color == CardColor.SMILE) {
                        continue;
                    }
                    if (c.number > max) {
                        max = c.number;
                    }
                    if (c.number < min) {
                        min = c.number;
                    }
                }
                int x = max - min + 1;
                //int y = numberOfSmileCards;
                //int z = x - y;
                if (x - cardGroupCopy.size() > 0) {
                    System.out.println("cardGroup do not have enough smile card");
                    return false;
                }

                //abandon
                // //smile card do not need to join sort
                // for(int i = 0 ; i< cardGroupCopy.size() ; i++){
                //     if(cardGroupCopy.get(i).color == CardColor.SMILE){
                //         cardGroupCopy.remove(i);
                //         i--;
                //     }
                // }
                // for(int i = 0 ; i<cardGroupCopy.size();i++){
                //     for(int j = 0 ; j<cardGroupCopy.size()-1;j++){
                //         if(cardGroupCopy.get(j).number > cardGroupCopy.get(j+1).number){
                //             Card card = new Card(cardGroupCopy.get(j).number, cardGroupCopy.get(j).color);

                //         }

                //     }
                // }
            }
            //same number but all card different color
            else {
                //it's impossible the qty of cards which has same number and different color in a group more than 4 
                if (cardGroupCopy.size() > 4) {
                    System.out.println("cardGroup size higher than 4");
                    return false;
                }
                // check all cards has same number
                int targetNumber = cardGroupCopy.get(0).number;
                for (Card card : cardGroupCopy) {
                    if (card.color == CardColor.SMILE) {
                        //the smile does not join this judgement
                        continue;
                    }
                    if (card.number != targetNumber) {
                        //the numbers are differerent
                        System.out.println("cardGroup should be different number has duplicate number");
                        return false;
                    }
                }
                //this data structure can't add the same thing which has been added 
                HashSet<CardColor> set = new HashSet<CardColor>();
                for (Card card : cardGroupCopy) {
                    if(card.color == CardColor.SMILE){
                        continue;
                    }
                    if(!set.add(card.color)){
                        System.out.println("cardGroup should be different color has same number");
                        return false;
                    }
                }

                // if (numberOfSmileCards == 0 || numberOfSmileCards == 1) {
                //     if (set.size() != cardGroupCopy.size()) {
                //         //if this two number not the same means there are duplicate color in card group
                //         return false;
                //     }
                // } else if (numberOfSmileCards == 2) {
                //     //add one meas two smile be two different color
                //     if (set.size() + 1 != cardGroupCopy.size()) {
                //         return false;
                //     }
                // } else {
                //     //never be excuted segment
                //     return false;
                // }

            }

        }
        return true;
    }

    private boolean isCardsSameColor(ArrayList<Card> cards) {
        CardColor targetColor = cards.get(0).color;
        for (Card card : cards) {
            //we can ignore all smile card because we can see it as any color
            if (card.color == CardColor.SMILE) {
                continue;
            }
            if (targetColor != card.color) {
                return false;
            }
        }
        return true;
    }

    private boolean hasTheWinner() {
        // //todo
        // return true;

        // if cardpile have no card
        if (cardPile.size() == 0) {
            return true;
        }
        //if somebody has use all cards
        for (GamePlayer player : players) {
            if (player.hand.size() == 0) {
                return true;
            }
        }
        return false;
    }

    private void broadcastTheMyTurnMessase(GamePlayer player) throws IOException {
        int numberOfThisPlayer = players.indexOf(player);

        //send breadcast message to other client
        for (int i = 0; i < players.size(); i++) {
            if (i == numberOfThisPlayer) {
                continue;
            } else {
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
    }

    public void start() {
        init();

        deal();

        //todo ---------------this segment is for test-------------------------
        // i want to manually asign cards
        // HashSet<Integer> set = new HashSet<>();
        // System.out.println(set.add(1));
        // System.out.println(set.add(1));
        // for (int j = 0; j < 2; j++) {
        //     for (int i = 1; i <= 13; i++) {
        //         cardPile.add(new Card(i, CardColor.RED));
        //     }
        //     for (int i = 1; i <= 13; i++) {
        //         cardPile.add(new Card(i, CardColor.YELLOW));
        //     }
        //     for (int i = 1; i <= 13; i++) {
        //         cardPile.add(new Card(i, CardColor.BLUE));
        //     }
        //     for (int i = 1; i <= 13; i++) {
        //         cardPile.add(new Card(i, CardColor.GREEN));
        //     }
        // }
        // cardPile.add(new Card(0, CardColor.SMILE));
        // cardPile.add(new Card(0, CardColor.SMILE));
        players.get(0).hand = new ArrayList<Card>();
        players.get(1).hand = new ArrayList<Card>();
        // players.get(0).hand.add(new Card(0,CardColor.SMILE));
        // players.get(0).hand.add(new Card(0,CardColor.SMILE));
        players.get(0).hand.add(new Card(13, CardColor.RED));
        players.get(0).hand.add(new Card(13, CardColor.GREEN));
        players.get(0).hand.add(new Card(13, CardColor.BLUE));
        players.get(0).hand.add(new Card(13, CardColor.YELLOW));
        players.get(1).hand.add(new Card(10, CardColor.YELLOW));
        players.get(1).hand.add(new Card(11, CardColor.YELLOW));
        players.get(1).hand.add(new Card(12, CardColor.YELLOW));
        players.get(1).hand.add(new Card(0, CardColor.SMILE));
        //---------------this segment is for test-------------------------

        GameSnapshot snapshot = new GameSnapshot(cardPile, cardGroups, players);
        gameHistory.add(snapshot);
        System.out.println("the game is start with the condition below \n");
        System.out.println(snapshot);

        //this loop represent the whole game proccess will go through
        outer: while (true) {
            try {
                //this loop represent the game will switch term in game players
                for (GamePlayer player : players) {
                    GameSnapshot nowGameSnapshot = (GameSnapshot) (gameHistory.get(gameHistory.size() - 1).clone());
                    int numberOfThisPlayer = players.indexOf(player);

                    //send breadcast message to other client
                    broadcastTheMyTurnMessase(player);

                    // this loop represent every player has unlimited chance to move
                    myTerm: while (true) {

                        //generate the data pack that will send to client
                        player.oos.writeObject(generateServerToClientGameInfo(GameMessage.YOUR_TURN, player));
                        //Thread.sleep(5000);

                        GameInfo replygameInfo = (GameInfo) player.ois.readObject();
                        if (replygameInfo.playerMovement == PlayerMovement.Pick_One_Card) {
                            Card c = cardPile.get(0);
                            cardPile.remove(c);
                            player.hand.add(c);

                            player.oos.writeObject(generateServerToClientGameInfo(GameMessage.YOUR_TURN, player));

                            break myTerm;
                        } else if (replygameInfo.playerMovement == PlayerMovement.Move_One_Card) {
                            cardGroups = (ArrayList<ArrayList<Card>>) replygameInfo.copy.cardGroups.clone();
                            player.isBreakedTheIce = replygameInfo.isBreakedTheIce;
                        } else if (replygameInfo.playerMovement == PlayerMovement.Out_Of_A_Card) {
                            cardGroups = (ArrayList<ArrayList<Card>>) replygameInfo.copy.cardGroups.clone();
                            player.hand = (ArrayList<Card>) replygameInfo.playersHand.clone();
                            player.isBreakedTheIce = replygameInfo.isBreakedTheIce;
                        } else if (replygameInfo.playerMovement == PlayerMovement.Confirm) {
                            //todo candy ask me to add
                            player.isBreakedTheIce = replygameInfo.isBreakedTheIce;

                            if (isTheMovementLegally(player, nowGameSnapshot)) {
                                if (hasTheWinner()) {
                                    //it seens do not nesseccery
                                    //numberOfThisPlayer = players.indexOf(player);

                                    //send breadcast message to other client
                                    for (int i = 0; i < players.size(); i++) {
                                        if (i == numberOfThisPlayer) {
                                            player.oos.writeObject(
                                                    generateServerToClientGameInfo(GameMessage.YOU_WIN, player));
                                            System.out.println("player" + numberOfThisPlayer + "  win the game");
                                            continue;
                                        }
                                        player.oos.writeObject(
                                                generateServerToClientGameInfo(GameMessage.YOU_LOSE, player));
                                        System.out.println("player" + i + "  lose the game");
                                    }
                                    Thread.sleep(1000); // wait for client receive
                                    break outer;
                                }
                                player.oos.writeObject(generateServerToClientGameInfo(GameMessage.CONFIRM_OK, player));
                                break myTerm;
                            } else {
                                player.oos
                                        .writeObject(generateServerToClientGameInfo(GameMessage.CONFIRM_FAIL, player));

                            }
                        } else if (replygameInfo.playerMovement == PlayerMovement.Roll_Back) {
                            cardPile = (ArrayList<Card>) nowGameSnapshot.cardPile.clone();
                            cardGroups = (ArrayList<ArrayList<Card>>) nowGameSnapshot.cardGroups.clone();
                            player.hand = (ArrayList<Card>) nowGameSnapshot.hands.get(numberOfThisPlayer).clone();
                            player.isBreakedTheIce = nowGameSnapshot.isBreakedTheIces.get(numberOfThisPlayer);
                        } else {
                            System.out.println("get a gameinfo which player movement is null");

                        }

                    }

                    gameHistory.add(new GameSnapshot(cardPile, cardGroups, players));
                    System.out.println();
                    System.out.println();
                    System.out.println(gameHistory.get(gameHistory.size() - 1));

                }

            }

            catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
                this.shutdown();
                break outer;
            } catch (ClassNotFoundException e) {
                System.out.println(e);
                e.printStackTrace();
                this.shutdown();
                break outer;
            } catch (Exception e) {
                System.out.println(e);
                e.printStackTrace();
                this.shutdown();
                break outer;
            }

        }
        this.shutdown();
    }

    public void shutdown() {
        for (GamePlayer player : players) {
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