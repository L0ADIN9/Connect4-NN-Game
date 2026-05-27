package org.example;

import javax.swing.*;
import java.awt.*;

public class EndPanel extends JPanel {

    private static JLabel endTXT;

    public EndPanel(){
        setBackground(new Color(0, 0, 0, 180));
        endTXT = new JLabel("win_test_popup!",SwingConstants.CENTER);
        endTXT.setForeground(Color.black);

        endTXT.setFont(new Font("Arial", Font.BOLD, 100));
        endTXT.setVisible(true);
        add(endTXT);
    }


    public static void setWin(int w){
        if(w == -1){
            endTXT.setText("AI Wins!");
        } else if (w == 1) {
            endTXT.setText("Human Wins!");
        }else{
            endTXT.setText("Draw!");
        }
    }
}
