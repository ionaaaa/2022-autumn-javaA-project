package controller;

import chessComponent.*;
import model.ChessColor;
import view.*;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class ClickController {
    private final Chessboard chessboard;
    private SquareComponent first;
    private EasyAI easyai;
    private AI ai;
    ChessGameFrame mainFrame;

    public ClickController(Chessboard chessboard) {
        this.chessboard = chessboard;
        easyai=new EasyAI(chessboard);
        ai=new AI(chessboard);
    }

    public void onClick(SquareComponent squareComponent) throws IOException, InterruptedException {
        //判断第一次点击

        if(chessboard.getpanel()==2)
        {
            if(chessboard.getCurrentColor()==ChessColor.RED)
            {
                ai.work();
                chessboard.clickController.swapPlayer();
                return;
            }
        }
        if(chessboard.getpanel()==3)
        {
            if(chessboard.getCurrentColor()==ChessColor.RED)
            {
                easyai.work();
                chessboard.clickController.swapPlayer();
                return;
            }
        }
        if(!ChessGameFrame.getOnCheatMode())
        {
            if (first == null) {
                if (handleFirst(squareComponent)) {
                    squareComponent.setSelected(true);
                    first = squareComponent;
                    first.repaint();
                }
            } else {
                if (first == squareComponent) { // 再次点击取消选取
                    squareComponent.setSelected(false);
                    SquareComponent recordFirst = first;
                    first = null;
                    recordFirst.repaint();
                } else if (handleSecond(squareComponent)) {
                    //repaint in swap chess method.
                    Music soundEffect = new Music("src\\pic\\sound\\go.wav");
                    soundEffect.player();

                    chessboard.swapChessComponents(first, squareComponent);
                    chessboard.clickController.swapPlayer();

                    first.setSelected(false);
                    first = null;

                    //检测吃掉的棋子
                    List<String> chessEaten = chessboard.detectChessEaten();
                    ArrayList<ImageIcon> num_icon = ChessGameFrame.getNumbers();

                    int[][] numOfChessEaten = new int[2][7];

//                  遍历list，得到相应棋子，改数字
                    for (int i = 0; i < chessEaten.size(); i++){
                        char color = chessEaten.get(i).charAt(0);
                        int row = 0;
                        if (color == 'B'){
                            row = 1;
                        }
                        int col = Integer.parseInt(chessEaten.get(i).substring(1, 2));
                        numOfChessEaten[row][col]++;
                    }
                    for (int i = 0; i < 2; i++){
                        for (int j = 0; j < numOfChessEaten[i].length; j++){
                            ChessGameFrame.getEatenLabel(i, j).setIcon(num_icon.get(numOfChessEaten[i][j]));
                        }
                    }

                }
            }
        }
        //如果我们此时作弊了
        else
        {
            if(squareComponent instanceof ChessComponent)
            {
                String t1=squareComponent.getName();
                ChessColor t2=squareComponent.getChessColor();
                if(t2==ChessColor.RED)
                JOptionPane.showMessageDialog(mainFrame,t1+" for red");
                else if(t2==ChessColor.BLACK)
                JOptionPane.showMessageDialog(mainFrame,t1+" for black");
            }
        }
    }


    /**
     * @param squareComponent 目标选取的棋子
     * @return 目标选取的棋子是否与棋盘记录的当前行棋方颜色相同
     */

    private boolean handleFirst(SquareComponent squareComponent) throws IOException, InterruptedException {

        if(squareComponent instanceof EmptySlotComponent) return false;
        
        if (!squareComponent.isReversal()) {
            squareComponent.setReversal(true);
            System.out.printf("onClick to reverse a chess [%d,%d]\n", squareComponent.getChessboardPoint().getX(), squareComponent.getChessboardPoint().getY());
            squareComponent.repaint();
            chessboard.clickController.swapPlayer();
            return false;
        }
        return squareComponent.getChessColor() == chessboard.getCurrentColor();
    }

    /**
     * @param squareComponent first棋子目标移动到的棋子second
     * @return first棋子是否能够移动到second棋子位置
     */

    private boolean handleSecond(SquareComponent squareComponent) {
        if (!first.canMoveTo(chessboard.getChessComponents(), squareComponent.getChessboardPoint(), first)){
            JOptionPane.showMessageDialog(mainFrame, "Invalid move");
        }

        return first.canMoveTo(chessboard.getChessComponents(), squareComponent.getChessboardPoint(), first);
    }

    public void swapPlayer() throws IOException, InterruptedException {
        //棋盘换方
        chessboard.setCurrentColor(chessboard.getCurrentColor() == ChessColor.BLACK ? ChessColor.RED : ChessColor.BLACK);

        //自动存档
        String[][] info = chessboard.generateInfoOfOneStep();
        chessboard.setPreviousSteps(info);
        chessboard.saveGame("src\\save_auto.txt");
        System.out.println("auto save successfully");

        //显示黑红方
        ImageIcon blackLable = new ImageIcon("src\\pic\\black_label.png");
        ImageIcon redLable = new ImageIcon("src\\pic\\red_label.png");


        if(Chessboard.getBlackScore()<60&&Chessboard.getRedScore()<60) {
//            ChessGameFrame.setStatusLabel(chessboard.getCurrentColor());
            switch (chessboard.getCurrentColor()) {
                case RED -> ChessGameFrame.getStatusLabel().setIcon(redLable);
                case BLACK -> ChessGameFrame.getStatusLabel().setIcon(blackLable);
            }
        }
        else
        {
            SquareComponent.setDone(true);
            ImageIcon black_win_icon = new ImageIcon("src\\pic\\win_black.png");
            ImageIcon red_win_icon = new ImageIcon("src\\pic\\win_red.png");

            if(Chessboard.getBlackScore()>=60)
                ChessGameFrame.getWin_label().setIcon(black_win_icon);

            else
                ChessGameFrame.getWin_label().setIcon(red_win_icon);


            Music soundEffect = new Music("src\\pic\\sound\\gamewin.wav");
            soundEffect.player();

            if (chessboard.getpanel() == 1){
                JOptionPane.showMessageDialog(mainFrame, "game over");
                restart();
            }
            else if (chessboard.getpanel() == 3) {
                if (Chessboard.getBlackScore()>=60){
                    JOptionPane.showMessageDialog(mainFrame, "you win, next level!");
                    chessboard.setPanel(2);
                    ImageIcon switchIcon = new ImageIcon("src\\pic\\secondary.png");
                    ChessGameFrame.getPVPLabel().setIcon(switchIcon);
                    restart();
                }
                else {
                    JOptionPane.showMessageDialog(mainFrame, "you lose, game over");
                    restart();
                }

            } else if (chessboard.getpanel() == 2) {
                if (Chessboard.getBlackScore()>=60){
                    JOptionPane.showMessageDialog(mainFrame, "you win!");
                    chessboard.setPanel(3);
                    ImageIcon switchIcon = new ImageIcon("src\\pic\\primary.png");
                    ChessGameFrame.getPVPLabel().setIcon(switchIcon);
                    restart();
                }
                else {
                    JOptionPane.showMessageDialog(mainFrame, "you lose, game over");
                    restart();
                }

            }

        }

        int ten_black = Chessboard.getBlackScore() / 10;
        int one_black = Chessboard.getBlackScore() % 10;
        int ten_red = Chessboard.getRedScore() / 10;
        int one_red = Chessboard.getRedScore() % 10;
        ArrayList<ImageIcon> allNum = ChessGameFrame.getNumbers();
        ChessGameFrame.getFoldOfColor(1).setIcon(allNum.get(ten_black));
        ChessGameFrame.getFoldOfColor(2).setIcon(allNum.get(one_black));
        ChessGameFrame.getFoldOfColor(3).setIcon(allNum.get(ten_red));
        ChessGameFrame.getFoldOfColor(4).setIcon(allNum.get(one_red));
    }

    public void restart(){
        try {
            ImageIcon empty = new ImageIcon("src\\pic\\empty.png");
            ChessGameFrame.getWin_label().setIcon(empty);
            chessboard.initAllChessOnBoard();
            SquareComponent.setDone(false);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        chessboard.setRedScore(0);
        chessboard.setBlackScore(0);


        ChessGameFrame.getFoldOfColor(1).setIcon(ChessGameFrame.getNumbers().get(0));
        ChessGameFrame.getFoldOfColor(2).setIcon(ChessGameFrame.getNumbers().get(0));
        ChessGameFrame.getFoldOfColor(3).setIcon(ChessGameFrame.getNumbers().get(0));
        ChessGameFrame.getFoldOfColor(4).setIcon(ChessGameFrame.getNumbers().get(0));

        chessboard.setCurrentColor(ChessColor.BLACK);

        ImageIcon blackLable = new ImageIcon("src\\pic\\black_label.png");
        ChessGameFrame.getStatusLabel().setIcon(blackLable);

        List<String[][]> previousSteps = new ArrayList<>();
        Chessboard.setPreviousSteps(previousSteps);
        String[][] info = chessboard.generateInfoOfOneStep();
        chessboard.setPreviousSteps(info);

        try {
            chessboard.saveGame("src\\save_auto.txt");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        System.out.println("auto save successfully!");

        List<String> chessEaten = chessboard.detectChessEaten();
        ArrayList<ImageIcon> num_icon = ChessGameFrame.getNumbers();

        int[][] numOfChessEaten = new int[2][7];

//                  遍历list，得到相应棋子，改数字
        for (int i = 0; i < chessEaten.size(); i++){
            char color = chessEaten.get(i).charAt(0);
            int row = 0;
            if (color == 'B'){
                row = 1;
            }
            int col = Integer.parseInt(chessEaten.get(i).substring(1, 2));
            numOfChessEaten[row][col]++;
        }
        for (int i = 0; i < 2; i++){
            for (int j = 0; j < numOfChessEaten[i].length; j++){
                ChessGameFrame.getEatenLabel(i, j).setIcon(num_icon.get(numOfChessEaten[i][j]));
            }
        }
    }

}
