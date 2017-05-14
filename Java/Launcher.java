import java.util.Scanner;

public class Launcher{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("do you want to be Game Server(s) or Player(c)?");
        while(true){
            if(sc.hasNextLine()){
                String inputString = sc.nextLine();
                if(inputString.compareToIgnoreCase("s") == 0){
                    System.out.println("server Start");
                    GameServer gs = new GameServer();
                    gs.Start();
                    break;
                }
                else if(inputString.compareToIgnoreCase("c") == 0 ){
                    System.out.println("client Start");
                    GameClient gc = new GameClient();
                    gc.start();
                    break;
                }
                else {
                    System.out.println("please retry");
                }
            }
             
        }
    }
}