package org.example;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardPanel extends JPanel{
    private int padding = 40;
    private int gap = 10;

    public static int pressedKey = -1;
    public static int mouseLocation = -1;
    public static boolean pressed;

    public BoardPanel() {
        setBackground(Color.gray);
        setFocusable(true);

        // player input stuff, should always be running

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {

                int bHeight = getHeight() - padding *2;
                int bWidth = (int)(bHeight*7/6);
                if (bWidth > getWidth() - padding * 2) {
                    bWidth = getWidth() - padding * 2;
                    bHeight = (int)(bWidth * (6.0 / 7.0));

                }

                int pd = (getWidth()-bWidth)/2;

                int posX = e.getX() - pd;
                double val = ((double)posX)/ ((double) bWidth);
                val *= 7;
                val /= 10;
                if(e.getX()>=pd || e.getX()<=pd+bWidth){
                    synchronized (GameWindow.class){
                        mouseLocation = (int)(val*10);
                        GameWindow.class.notifyAll();
                    }
                    repaint();
                }else{
                    mouseLocation = -1;
                }

               // mouseLocation = (int)(val*10);

            }
        });

/*
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
                    synchronized (GameWindow.class){
                        pressedKey = col;
                        GameWindow.class.notifyAll();
                    }
                    repaint();
                }
            }
        });

*/

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
                if(brd[j][i] == 1) cl = Color.yellow;
                if(brd[j][i] == -1) cl = Color.red;

                g.setColor(cl);
                g.fillOval(centX + i*cellScale+(int)(.1*cellScale),centY+j*cellScale+(int)(.1*cellScale), (int)(.8*cellScale),(int)(.8*cellScale));


            }
        }

    }

}
