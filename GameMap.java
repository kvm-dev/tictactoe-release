package ru.geekbrains.gui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class GameMap extends JPanel {

    public static final int MODE_HVH = 0;
    public static final int MODE_HVA = 1;

    private static final Random RANDOM = new Random();
    private static final int DOT_EMPTY = 0;
    private static final int DOT_HUMAN = 1;
    private static final int DOT_AI = 2;
    private static final int DOT_PADDING = 5;

    private int stateGameOver;
    private static final int STATE_DRAW=0;
    private static final int STATE_WIN_HUMAN = 1;
    private static final int STATE_WIN_AI = 2;

    private static final String MSG_WIN_HUMAN = "Победил человек";
    private static final String MSG_WIN_AI = "Победила машина";
    private static final String MSG_DRAW = "Ничья";

    private int[][] field;
    private int fieldSizeX;
    private int fieldSizeY;
    private int winLength;
    private int cellWidth;
    private int cellHeight;
    private boolean isGameOver;
    private boolean initialized;


    public GameMap()
    {
        setBackground(new Color(0,50,80));
        addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
              update(e);
            }
        });
        initialized = false;
    }

    private void update(MouseEvent e) {
        if(isGameOver ||!initialized) return;
        int cellX = e.getX() / cellWidth;
        int cellY = e.getY() / cellHeight;
        //проверочка
        // System.out.printf("x: %d, y: %d\n", cellx, celly);
        if(!isValidCell(cellX, cellY) || !isEmptyCell(cellX, cellY))
            return;
        field[cellY][cellX] = DOT_HUMAN;
        if(checkEndGame(DOT_HUMAN, STATE_WIN_HUMAN)) return;
        aiTurn();
        repaint();
        if(checkEndGame(DOT_AI, STATE_WIN_AI)) return;

    }
    private boolean checkEndGame(int dot, int stateGameOver){
        if(checkWin(dot)){
            this.stateGameOver = stateGameOver;
            isGameOver = true;
            repaint();
            return true;
        }
        if(isMapFull()){
            this.stateGameOver = STATE_DRAW;
            isGameOver = true;
            repaint();
            return true;
        }
        return false;
    }

    void StartNewGame(int gameMode, int fieldSizeX, int fieldSizeY, int winLength){
    //System.out.printf("mode: %d, size: %dx%d, win:%d\n", gameMode, fieldSizeX, fieldSizeY, winLength );
        this.fieldSizeY = fieldSizeY;
        this.fieldSizeX = fieldSizeX;
        this.winLength = winLength;
        field = new int[fieldSizeY][fieldSizeX];
        initialized = true;
        isGameOver = false;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);

    }

    private void render(Graphics g) {
        if(!initialized) return;
        int width = getWidth();
        int height = getHeight();
        cellWidth = width / fieldSizeX;
        cellHeight = height / fieldSizeY;
        g.setColor(Color.BLACK);
        for (int i = 0; i<fieldSizeY;i++){
            int y = i * cellHeight;
            g.drawLine(0,y,width,y);
        }
        for (int i = 0; i<fieldSizeX; i++){
            int x = i * cellWidth;
            g.drawLine(x,0, x, height);
        }
        for(int y=0;y<fieldSizeY;y++){
            for(int x=0;x<fieldSizeX;x++){
                if(isEmptyCell(x,y)) continue;
                if(field[y][x]==DOT_HUMAN){
                    g.setColor(new Color(1,1,255));
                    g.fillOval(x * cellWidth + DOT_PADDING,
                                y * cellHeight + DOT_PADDING,
                            cellWidth - DOT_PADDING * 2,
                            cellHeight - DOT_PADDING * 2);
                }
                else if (field[y][x]==DOT_AI){
                    g.setColor(Color.RED);
                    g.fillRect(x * cellWidth + DOT_PADDING,
                                y * cellHeight + DOT_PADDING,
                                cellWidth - DOT_PADDING * 2,
                                cellHeight - DOT_PADDING * 2);
                }
                else {
                    throw new RuntimeException(
                            String.format("Can't recognize cell field [%d][%d]: %d",y, x, field[y][x] ));
                }
            }
        }
        if(isGameOver){
            showMessageGameOver(g);
        }
    }
    //выводим инфу про победу компа
    private void showMessageGameOver(Graphics g){
    g.setColor(Color.DARK_GRAY);
    g.fillRect(0, 200, getWidth(), 70);
    g.setColor(Color.YELLOW);
    g.setFont(new Font("Times New Roman", Font.BOLD, 32));
    switch (stateGameOver){
        case STATE_DRAW:
            g.drawString(MSG_DRAW,188, getHeight() / 2);
            break;
        case STATE_WIN_AI:
            g.drawString(MSG_WIN_AI, 20, getHeight() / 2);
            break;
        case STATE_WIN_HUMAN:
            g.drawString(MSG_WIN_HUMAN, 70, getHeight() / 2);
            break;
        default:
            throw new RuntimeException("Unexpected gameOver state: " + stateGameOver);
    }
    }
    //ход компа
    private void aiTurn(){
        if(turnAIWinCell()) return; // проверка следующего хода игрока на победу
        if(turnHumanWinCell()) return; //проверка следующего хода компа на победу
        int x, y;
        do{
            x = RANDOM.nextInt(fieldSizeX);
            y = RANDOM.nextInt(fieldSizeY);
        }
        while (!isEmptyCell(x, y));
        field[y][x] = DOT_AI;
    }
    //проверка может ли выйграть комп
    private boolean turnAIWinCell(){
        for (int i=0; i<fieldSizeY; i++){
            for(int j=0; j<fieldSizeX; j++){
                if(isEmptyCell(j, i)){ //поставим 0 в каждую клетку поля по очереди для проверки
                    field[i][j] = DOT_AI;
                    if(checkWin(DOT_AI)) return true; // если мы выйграли вернем истину
                    field[i][j] = DOT_EMPTY; // если нет вернем обратно пустоту
                }
            }
        }
        return false;
    }

    //Проверка выйграет ли игрок своим следующим ходом
    private boolean turnHumanWinCell(){
        for (int i=0; i<fieldSizeY;i++){
            for (int j=0;j<fieldSizeX;j++){
                if(isEmptyCell(j,i)){
                    field[i][j] = DOT_HUMAN; //поставим крестик в каждую клетку поля по очереди для проверки
                    if(checkWin(DOT_HUMAN)){ // если игрок победит
                        field[i][j] = DOT_AI; // поставить на то место нолик
                        return true;
                    }
                    field[i][j] = DOT_EMPTY; //в противном случае вернуть туда пустоту
                }
            }
        }
        return false;
    }
    //проверка на победу
    private boolean checkWin(int c){
        for (int i=0; i<fieldSizeX; i++){
            for(int j=0;j<fieldSizeY;j++){
                if(checkLine(i, j, 1, 0, winLength, c)) return true; // проверяем линию по x
                if(checkLine(i, j, 1, 1, winLength, c)) return true; // проверяем линию по диагонали вправо и вниз
                if(checkLine(i, j, 0, 1, winLength, c)) return true; //проверяем линию по y
                if(checkLine(i, j, 1, -1, winLength, c)) return true; // проверяем линию по диагонали вправо и вверх
            }
        }
        return false;
    }
    //проверка линии
    private boolean checkLine(int x, int y, int vx, int vy, int len, int c){
        final int far_x = x +(len -1) * vx; // просчитаем конец проверяемой линии
        final int far_y = y +(len -1) * vy;
        if(!isValidCell(far_x, far_y)) return false; //проверяем не проходит ли проверяемая линия за пределы поля
        for(int i=0; i<len; i++){ //идем по проверяемой линии
            if(field[y+i * vy][x+i * vx] !=c) return false; // проверяем одинаковые ли символы в ячейках линии
        }
        return true;
    }

    //ничья?
    private boolean isMapFull(){
        for (int i=0; i<fieldSizeY;i++){
            for (int j=0;j<fieldSizeX;j++){
                if(field[i][j]==DOT_EMPTY) return false;
            }
        }
        return true;
    }

    //проверяем правильность ячейки
    private boolean isValidCell(int x, int y){
        return x >=0 && x<fieldSizeX && y>=0 && y <fieldSizeY;
    }
// проверка на пустоту в ячейке
    private boolean isEmptyCell(int x, int y){
        return field[y][x] == DOT_EMPTY;
    }
}
//2.45.36