package org.example;
import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {

    public GameWindow() {
        setBackground(Color.gray);
        setTitle("Connect 4");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        BoardPanel board = new BoardPanel();
        board.setSize(400,600);

        JPanel rightSide = new JPanel();
        rightSide.add(new JLabel("Turn: Player"));

        JPanel leftSide = new JPanel();
        leftSide.add(new JLabel("Turn: Player"));


        rightSide.setBackground(Color.gray);
        leftSide.setBackground(Color.gray);

        add(board,BorderLayout.CENTER);
        add(leftSide, BorderLayout.WEST);
        add(rightSide, BorderLayout.EAST);



        setVisible(true);
    }

}
