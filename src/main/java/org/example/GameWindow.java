package org.example;
import javax.swing.*;

public class GameWindow extends JFrame {

    public GameWindow() {
        setTitle("Connect 4");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new BoardPanel()); // UI lives here

        setVisible(true);
    }

}
