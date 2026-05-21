package org.example;
import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {

    public static BoardPanel boardP = new BoardPanel();
    private static JLabel txt;

    public GameWindow() {



        setVisible(false);

        setBackground(Color.gray);
        setTitle("Connect 4");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel rightSide = new JPanel();
        rightSide.add(new JLabel("                   "));


        JPanel leftSide = new JPanel();
        leftSide.add(new JLabel("                   "));


        rightSide.setBackground(Color.gray);
        leftSide.setBackground(Color.gray);


        boardP.setSize(400,600);
        boardP.setOpaque(true);





        add(boardP,BorderLayout.CENTER);
        add(leftSide, BorderLayout.WEST);
        add(rightSide, BorderLayout.EAST);
        setVisible(true);

        txt = new JLabel("",SwingConstants.CENTER);
        txt.setForeground(Color.black);
        txt.setFont(new Font("Arial", Font.BOLD, 100));
        txt.setOpaque(true);
        this.add(boardP);
        this.setGlassPane(txt);





    }
    public void winUI(int w){
        if(w == -1){
            txt.setText("AI Wins!");
        } else if (w == 1) {
            txt.setText("Human Wins!");
        }else{
            txt.setText("Draw!");

        }
        boardP.setOpaque(true);

        txt.setBackground(new Color(3, 3, 3, 20));
        txt.setOpaque(false);
        txt.setVisible(true);



    }

}
