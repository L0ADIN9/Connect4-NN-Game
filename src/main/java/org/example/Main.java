package org.example;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
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