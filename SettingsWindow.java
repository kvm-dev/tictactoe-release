package ru.geekbrains.gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// не работает фишка с центрированием, даже без указания позиции для окна, один фиг у меня создавалось оно по центру Game Window, и никак параметры на это не влияют, предполагаю, что дело в моей оболочке xfce. Ос (linux Kali)
//у меня тут в общем-то и радиобаттон работает как чекбокс.. в топ плане, что можно со всех снять состояние checked
public class SettingsWindow extends JFrame {
    private static final int WINDOW_WIDTH = 350;
    private static final int WINDOW_HEIGHT = 270;
    private static final int MIN_WIN_LENGTH = 3;
    private static final int MIN_FIELD_SIZE = 3;
    private static final int MAX_FIELD_SIZE = 10;
    private static final String FIELD_SIZE_PREFIX = "Field Size is: ";
    private static final String WIN_LENGTH_PREFIX = "Win length is: ";
    private GameWindow gameWindow;
    private JRadioButton humVSAI;
    private JRadioButton humVSHum;
    private JSlider slideWinlen;
    private JSlider slideFieldSize;

    SettingsWindow(GameWindow gameWindow){
        this.gameWindow = gameWindow;
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        Rectangle gameWindowBounds = gameWindow.getBounds();
        int posX = (int) gameWindowBounds.getCenterX() - WINDOW_WIDTH / 2;
        int posY = (int) gameWindowBounds.getCenterY() - WINDOW_HEIGHT / 2;
        setLocation(posX, posY);
        setResizable(false);
        setTitle("Creating new game");
        setLayout(new GridLayout(10,1));
        addGameModeControls();
        addFieldControls();
        JButton btnStartGame = new JButton("Start a game");
        btnStartGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                collectDataFromControlsAndStartGame();
            }
        });
        add(btnStartGame);
    }

    private void addGameModeControls(){
        add(new JLabel("Choose game mode"));
        humVSAI = new JRadioButton("Human Versus AI", true);
        humVSHum = new JRadioButton("Human Versus Human");
        ButtonGroup gameMode = new ButtonGroup();
        gameMode.add(humVSAI);
        gameMode.add(humVSHum);
        add(humVSAI);
        add(humVSHum);

    }
    private void addFieldControls(){
        JLabel lbFieldSize = new JLabel((FIELD_SIZE_PREFIX + MIN_FIELD_SIZE));
        JLabel lbWinLegth = new JLabel(WIN_LENGTH_PREFIX + MIN_WIN_LENGTH);
        slideFieldSize = new JSlider(MIN_FIELD_SIZE, MAX_FIELD_SIZE, MIN_FIELD_SIZE);
        slideWinlen = new JSlider(MIN_WIN_LENGTH, MIN_FIELD_SIZE, MIN_FIELD_SIZE);
        slideFieldSize.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                int currentValue = slideFieldSize.getValue();
            lbFieldSize.setText(FIELD_SIZE_PREFIX + currentValue);
            slideWinlen.setMaximum(currentValue);
            }
        });
        slideWinlen.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
            lbWinLegth.setText(WIN_LENGTH_PREFIX + slideWinlen.getValue());
            }
        });

        add(new JLabel("Choose field size"));
        add(lbFieldSize);
        add(slideFieldSize);
        add(new JLabel("Choosw win legth"));
        add(lbWinLegth);
        add(slideWinlen);

    }
    private void collectDataFromControlsAndStartGame(){
        int gameMode;
        if(humVSAI.isSelected()){
            gameMode = GameMap.MODE_HVA;
        }
        else if(humVSHum.isSelected()){
            gameMode = GameMap.MODE_HVH;
        }
        else{
            throw new RuntimeException("Unexpected game mode");
        }
        int fieldSize = slideFieldSize.getValue();
        int winLen = slideWinlen.getValue();

        gameWindow.acceptSettings(gameMode, fieldSize, fieldSize, winLen);

        setVisible(false);
    }


}
