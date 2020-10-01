package ru.geekbrains.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindow extends JFrame {
    private static final int W_WIDTH = 507;
    private static final int W_HEIGHT = 555;
  //  private static final int W_POS_X = 400;
  //  private static final int W_POS_Y = 150;
    private GameMap map;
    GameWindow(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(W_WIDTH, W_HEIGHT);
        //setLocation(W_POS_X,W_POS_Y);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("TicTacToe");
        JButton btnStart = new JButton("Start game");
       // btnStart.setPreferredSize(new Dimension(0,100));
        JButton btnStop = new JButton("<html><b>Exit</b></html>");
        JPanel panelBottom = new JPanel();
        panelBottom.setLayout(new GridLayout(1,2));
        panelBottom.add(btnStart);
        panelBottom.add(btnStop);
        map = new GameMap();
        SettingsWindow settings = new SettingsWindow(this);
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
            settings.setVisible(true);

            }
        });
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                 System.exit(0);
            }
        });
        add(map,BorderLayout.CENTER);
        add(panelBottom,BorderLayout.SOUTH);
        setVisible(true);

    }
    public void acceptSettings(int gameMode, int fieldSizeX, int fieldSizeY, int winLength){
        map.StartNewGame(gameMode, fieldSizeX, fieldSizeY,winLength);
    }
}
