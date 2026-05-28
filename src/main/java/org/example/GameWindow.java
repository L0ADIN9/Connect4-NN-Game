package org.example;
import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {

    private static CardLayout lyt;
    public static StartPanel startP = new StartPanel();
    public static BoardPanel boardP = new BoardPanel();
    public static EndPanel endP = new EndPanel();
    public static JLayeredPane lyp;

    public GameWindow() {



        setVisible(false);

        setBackground(Color.gray);
        setTitle("Connect 4");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        lyt = new CardLayout();
        setLayout(lyt);


    // Start Panel
        startP.setOpaque(true);

    // Game Panel

        lyp = new JLayeredPane();
        lyp.setLayout(new OverlayLayout(lyp));
        lyp.setBounds(0,0,lyp.getWidth(),lyp.getHeight());

        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBackground(Color.gray);


        JPanel rightSide = new JPanel();
        rightSide.add(new JLabel("                   "));
        rightSide.setBackground(Color.gray);


        JPanel leftSide = new JPanel();
        leftSide.add(new JLabel("                   "));
        leftSide.setBackground(Color.gray);


        //boardP.setSize(400,600);
        boardP.setOpaque(true);

        gamePanel.add(boardP,BorderLayout.CENTER);
        boardP.setVisible(true);
        gamePanel.add(leftSide, BorderLayout.WEST);
        leftSide.setVisible(true);
        gamePanel.add(rightSide, BorderLayout.EAST);
        rightSide.setVisible(true);

        lyp.add(gamePanel, JLayeredPane.DEFAULT_LAYER);

        // End Panel
        lyp.add(endP,JLayeredPane.PALETTE_LAYER);
        endP.setVisible(false);







        // Overall UI Stuff
        add(startP,"START");
        add(lyp,"GAME");

        setVisible(true);



    }

    public static void startUI(){
        lyt.show(Main.gWindow.getContentPane(),"GAME");
        lyp.revalidate();
        lyp.repaint();
        lyp.setVisible(true);
    }
    public static void homeUI(){
        lyt.show(Main.gWindow.getContentPane(),"START");
        lyp.revalidate();
        lyp.repaint();
        lyp.setVisible(false);
        endP.setVisible(false);
    }

    public static void endUI(int w){
        endP.setWin(w);
        endP.setVisible(true);
        endP.repaint();

    }


}
