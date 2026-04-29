package org.example;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class BoardPanel extends JPanel{
    private int padding = 20;
    private int gap = 10;
    public BoardPanel() {
        setBackground(Color.gray);
        setFocusable(true);

        // player input stuff, should always be running
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent k) {
                int col = -1;
                if (k.getKeyCode() == KeyEvent.VK_1) col = 0;
                if (k.getKeyCode() == KeyEvent.VK_2) col = 1;
                if (k.getKeyCode() == KeyEvent.VK_3) col = 2;
                if (k.getKeyCode() == KeyEvent.VK_4) col = 3;
                if (k.getKeyCode() == KeyEvent.VK_5) col = 4;
                if (k.getKeyCode() == KeyEvent.VK_6) col = 5;
                if (k.getKeyCode() == KeyEvent.VK_7) col = 6;
                if (col != -1) {
                    GameHandler.move(col,1);
                    GameHandler.move(Main.getBotInputPos(),2);
                    repaint();
                }
            }
        });


    }

    // use repaint( ) to update board
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int bHeight = getHeight() - padding *2;
        int bWidth = (int)(bHeight*7/6);
        if (bWidth > getWidth() - padding * 2) {
            bWidth = getWidth() - padding * 2;
            bHeight = (int)(bWidth * (6.0 / 7.0));
        }
        int cellScale = bWidth/ 7;

        int centX = (getWidth()-bWidth)/2;
        int centY = (getHeight()-bHeight)/2;

        g.setColor(Color.BLUE);
        g.fillRect(centX,centY ,bWidth,bHeight);
        int[][] brd = GameHandler.getBoard();


        for(int i = 0; i< 7; i++){
            for(int j = 0; j< 6; j++){
                Color cl = Color.white;
                if(brd[j][i] == 1) cl = Color.red;
                if(brd[j][i] == 2) cl = Color.yellow;

                g.setColor(cl);
                g.fillOval(centX + i*cellScale+(int)(.1*cellScale),centY+j*cellScale+(int)(.1*cellScale), (int)(.8*cellScale),(int)(.8*cellScale));


            }
        }

    }

}
