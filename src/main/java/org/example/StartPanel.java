package org.example;

import javax.swing.*;
import java.awt.*;


public class StartPanel extends JPanel {

    public static JButton sButton;

    public static String selectedDiff = "MEDIUM";

    public StartPanel(){
        setBackground(Color.gray);
        setFocusable(true);
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));

    //Game title
        JLabel titleTXT = new JLabel("Connect 4");
        titleTXT.setFont(new Font("Arial", Font.BOLD, 100));
        titleTXT.setAlignmentX(Component.CENTER_ALIGNMENT);

    //Start Button
        sButton = new JButton("Start Game");
        sButton.setFont(new Font("Arial", Font.PLAIN, 50));
        sButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        sButton.addActionListener( k ->{
            try {
                Main.START(selectedDiff,false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    //Difficulty Selection
        JPanel diffButtonsUI = new JPanel();
        diffButtonsUI.setLayout(new FlowLayout());
        diffButtonsUI.setBackground(Color.gray);

        ButtonGroup diffSelect = new ButtonGroup();

        JRadioButton easy = new JRadioButton("Hard");
        JRadioButton medium = new JRadioButton("Harder");
        JRadioButton hard = new JRadioButton("Hardest");

        easy.addActionListener(e -> selectedDiff = "EASY");
        medium.addActionListener(e -> selectedDiff = "MEDIUM");
        hard.addActionListener(e -> selectedDiff = "HARD");
        medium.setSelected(true);

        for (JRadioButton b : new JRadioButton[]{easy, medium, hard}) {
            diffSelect.add(b);
            b.setFont(new Font("Arial", Font.PLAIN, 30));
            b.setBorder(BorderFactory.createEmptyBorder(10,10,20,10));

        }

        diffButtonsUI.add(easy);
        diffButtonsUI.add(Box.createHorizontalGlue());
        diffButtonsUI.add(medium);
        diffButtonsUI.add(Box.createHorizontalGlue());
        diffButtonsUI.add(hard);

        diffButtonsUI.setAlignmentX(.5f);

    //Panel Setup

        add(Box.createVerticalGlue());
        add(titleTXT);
        add(Box.createVerticalStrut(100));
        add(sButton);
        add(Box.createVerticalStrut(70));
        add(diffButtonsUI);
        add(Box.createVerticalStrut(20));
        //add(Box.createVerticalGlue());

    }
}
