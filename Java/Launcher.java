import java.util.Scanner;

import java.util.regex.*;

//launcher
//this clss is design for choosing the rule(client or server)
//and it is also the enter point 
public class Launcher {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("do you want to be Game Server(s) or Player(c)?");

        while (true) {

            if (sc.hasNextLine()) {
                String inputString = sc.nextLine();

                //start a server
                if (inputString.compareToIgnoreCase("s") == 0) {
                    System.out.println("how many(2~4) player will join the game?");
                    int playerNum = 0;
                    while (true) {
                        if (sc.hasNextLine()) {
                            if (sc.hasNextInt()) {
                                playerNum = sc.nextInt();
                                if (playerNum > 1 && playerNum < 5) {
                                    break;
                                } else {
                                    System.out.println("please retry");
                                }
                            }
                            else{
                                sc.nextLine();
                                System.out.println("please retry");
                            }
                        } 
                        else {

                        }

                    }
                    System.out.println("server Start");
                    GameServer gs = new GameServer();
                    gs.Start(playerNum);
                    break;
                }

                //start a client(player)
                else if (inputString.compareToIgnoreCase("c") == 0) {
                    System.out.println("please enter the server address");
                    String IP_FiltRegrex = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
                    String serverIP = "127.0.0.1";
                    while (true) {
                        serverIP = sc.nextLine();
                        if (serverIP.matches(IP_FiltRegrex)) {
                            break;
                        } else {
                            System.out.println("please retry");
                        }
                    }

                    System.out.println("client Start");
                    GameClient gc = new GameClient();
                    gc.start(serverIP);
                    break;
                }

                //input error
                else {
                    System.out.println("please retry");
                }
            }

        }
    }
}