package org.example;
import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {

    public static BoardPanel boardP = new BoardPanel();

    public GameWindow() {
        setBackground(Color.gray);
        setTitle("Connect 4");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        boardP.setSize(400,600);

        JPanel rightSide = new JPanel();
        rightSide.add(new JLabel("Turn: Player"));

        JPanel leftSide = new JPanel();
        leftSide.add(new JLabel("Turn: Player"));


        rightSide.setBackground(Color.gray);
        leftSide.setBackground(Color.gray);



        add(boardP,BorderLayout.CENTER);
        add(leftSide, BorderLayout.WEST);
        add(rightSide, BorderLayout.EAST);





        setVisible(true);
    }
    public void winUI(int w){
        System.out.println(w);
        JPanel winner = new JPanel();
        JLabel txt = new JLabel(w + " won!");
        txt.setFont(new Font("Arial", Font.PLAIN, 48));
        remove(boardP);
        winner.add(txt,BorderLayout.CENTER);
        add(winner,BorderLayout.CENTER);
        setVisible(true);
        repaint();
    }

}
