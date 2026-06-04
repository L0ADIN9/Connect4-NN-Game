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

        JLabel titleTXT = new JLabel("Connect 4");
        titleTXT.setFont(new Font("Arial", Font.BOLD, 100));
        titleTXT.setAlignmentX(Component.CENTER_ALIGNMENT);

        sButton = new JButton("Start Game");
        sButton.setFont(new Font("Arial", Font.PLAIN, 50));
        sButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        sButton.addActionListener( k ->{
            try {
                Main.START(selectedDiff,true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        JPanel diffButtonsUI = new JPanel();
        diffButtonsUI.setLayout(new FlowLayout());
        diffButtonsUI.setBackground(Color.gray);

        ButtonGroup diffSelect = new ButtonGroup();

        JRadioButton easy = new JRadioButton("Easy");
        JRadioButton medium = new JRadioButton("Medium");
        JRadioButton hard = new JRadioButton("Hard");
        JRadioButton zack = new JRadioButton("Impossible");

        easy.addActionListener(e -> selectedDiff = "EASY");
        medium.addActionListener(e -> selectedDiff = "MEDIUM");
        hard.addActionListener(e -> selectedDiff = "HARD");
        zack.addActionListener(e -> selectedDiff = "ZACK");
        medium.setSelected(true);

        for (JRadioButton b : new JRadioButton[]{easy, medium, hard, zack}) {
            diffSelect.add(b);
            b.setFont(new Font("Arial", Font.PLAIN, 30));
            b.setBorder(BorderFactory.createEmptyBorder(10,10,20,10));

        }

        diffButtonsUI.add(easy);
        diffButtonsUI.add(Box.createHorizontalGlue());
        diffButtonsUI.add(medium);
        diffButtonsUI.add(Box.createHorizontalGlue());
        diffButtonsUI.add(hard);
        diffButtonsUI.add(Box.createHorizontalGlue());
        diffButtonsUI.add(zack);

        diffButtonsUI.setAlignmentX(.5f);

        add(Box.createVerticalGlue());
        add(titleTXT);
        add(Box.createVerticalStrut(100));
        add(sButton);
        add(Box.createVerticalStrut(70));
        add(diffButtonsUI);
        add(Box.createVerticalStrut(20));

    }
}
