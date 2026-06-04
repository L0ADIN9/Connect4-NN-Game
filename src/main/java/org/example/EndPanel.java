package org.example;

import javax.swing.*;
import java.awt.*;

public class EndPanel extends JPanel {

    private static JLabel endTXT;
    private static JButton rstBut;

    public EndPanel(){
        setLayout(new BorderLayout());

        endTXT = new JLabel("win_test_popup!",SwingConstants.CENTER);
        endTXT.setForeground(Color.black);
        endTXT.setFont(new Font("Arial", Font.BOLD, 100));
        endTXT.setOpaque(false);

        JPanel bck = new JPanel();
        bck.setBackground(new Color(3, 3, 3, 80));
        bck.setOpaque(true);

        rstBut = new JButton("To Menu");
        rstBut.setFont(new Font("Arial", Font.PLAIN, 65));
        rstBut.setBackground(Color.gray);
        rstBut.addActionListener( k -> {
            Main.RESET();
        });


        rstBut.setAlignmentX(Component.CENTER_ALIGNMENT);
        endTXT.setAlignmentX(Component.CENTER_ALIGNMENT);
        bck.setLayout(new BoxLayout(bck,BoxLayout.Y_AXIS));


        bck.add(Box.createVerticalGlue());
        bck.add(endTXT);
        bck.add(Box.createVerticalGlue());
        bck.add(rstBut);
        bck.add(Box.createVerticalGlue());
        add(bck,BorderLayout.CENTER);
        setOpaque(false);

    }


    public static void setWin(int w){
        if(w == -1){
            endTXT.setText(Main.isZackMode() ? "You Lose!" : "AI Wins!");
        } else if (w == 1) {
            endTXT.setText("Human Wins!");
        }else{
            endTXT.setText("Draw!");
        }
    }
}
