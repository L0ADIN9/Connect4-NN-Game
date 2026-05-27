package org.example;
import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {

    private static CardLayout lyt;
    public static StartPanel startP = new StartPanel();
    public static BoardPanel boardP = new BoardPanel();
    public static EndPanel endP = new EndPanel();

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

        JLayeredPane lyp = new JLayeredPane();
        lyp.setLayout(null);
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

        //lyp.add(gamePanel, 2);
        gamePanel.setVisible(true);

        // End Panel
       // lyp.add(endP,JLayeredPane.POPUP_LAYER);
        //endP.setVisible(false);






        // Overall UI Stuff
        add(startP,"START");
        add(gamePanel,"GAME");

        setVisible(true);



    }

    public static void startUI(){
        lyt.show(Main.gWindow.getContentPane(),"GAME");
    }

    public static void endUI(int w){
        endP.setWin(w);
        endP.setVisible(true);
        endP.repaint();

    }


}
