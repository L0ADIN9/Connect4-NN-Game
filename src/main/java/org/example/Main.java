package org.example;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        for(int i = 0; i< 14; i++){
            if(i%2 ==0){
                GameHandler.move((int)(Math.random()*7),1);
            }else{
                GameHandler.move((int)(Math.random()*7),2);
            }
        }
        SwingUtilities.invokeLater(() -> {
            new GameWindow();
        });

    }
    public static int getPlayerInputPos(){
        return 1;
    }
    public static int getBotInputPos(){
        return 2;
    }
}