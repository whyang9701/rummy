import java.util.Scanner;
//啟動器類別
//由此類別決定軟體為server side or client side
public class Launcher{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("do you want to be Game Server(s) or Player(c)?");
        while(true){
            if(sc.hasNextLine()){
                String inputString = sc.nextLine();
                if(inputString.compareToIgnoreCase("s") == 0){
                    System.out.println("how many player will join the game?");
                    int playerNum = 0;
                    while(true){
                        if(sc.hasNextInt()){
                            playerNum = sc.nextInt();
                            if(playerNum>1 && playerNum<5){
                                break;
                            }else{
                                System.out.println("please retry");
                            }
                        }
                        
                    }
                    System.out.println("server Start");
                    GameServer gs = new GameServer();
                    gs.Start(playerNum);
                    break;
                }
                else if(inputString.compareToIgnoreCase("c") == 0 ){
                    System.out.println("client Start");
                    GameClient gc = new GameClient();
                    gc.start("127.0.0.1");
                    break;
                }
                else {
                    System.out.println("please retry");
                }
            }
             
        }
    }
}