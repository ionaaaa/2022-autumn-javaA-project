package chessComponent;

import controller.ClickController;
import model.ChessColor;
import model.ChessboardPoint;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * 表示棋盘上非空棋子的格子，是所有非空棋子的父类
 */
public class ChessComponent extends SquareComponent{
    protected String name;// 棋子名字：例如 兵，卒，士等

    protected ChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor chessColor, ClickController clickController, int size,int rank) {
        super(chessboardPoint, location, chessColor, clickController, size,rank);
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Image chess_pic = null;
        try {
            chess_pic = ImageIO.read(new File("src\\pic\\qizi_1.png"));
        }catch(Exception e) {
            e.printStackTrace();
        }
        assert chess_pic != null;

//        int bias_x = chessboardPoint.getX() * 5;
//        int bias_y = chessboardPoint.getY() * 5;

        g.drawImage(chess_pic, spacingLength, spacingLength, this.getWidth() - 2 * spacingLength, this.getHeight() - 2 * spacingLength, this);

        if (isReversal) {
            //绘制棋子文字
//            g.setColor(this.getChessColor().getColor());
//            g.setFont(CHESS_FONT);
//            g.drawString(this.name, this.getWidth() / 4, this.getHeight() * 2 / 3);
            int rank = this.getRank();
            ChessColor color = this.getChessColor();
            if (color == ChessColor.BLACK){
                Image word_pic = null;
                switch (rank){
                    case 0 -> {
                        try {
                            word_pic = ImageIO.read(new File("src\\pic\\字\\black\\qizi_pao_black_3.png"));
                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                    case 1 -> {
                        try {
                            word_pic = ImageIO.read(new File("src\\pic\\字\\black\\qizi_jiang_black_3.png"));
                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                    case 2 -> {
                        try {
                            word_pic = ImageIO.read(new File("src\\pic\\字\\black\\qizi_shi_black_3.png"));
                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                    case 3 -> {
                        try {
                            word_pic = ImageIO.read(new File("src\\pic\\字\\black\\qizi_xiang_black_3.png"));
                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                    case 4 -> {
                        try {
                            word_pic = ImageIO.read(new File("src\\pic\\字\\black\\qizi_ju_black_3.png"));
                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                    case 5 -> {
                        try {
                            word_pic = ImageIO.read(new File("src\\pic\\字\\black\\qizi_ma_black_3.png"));
                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                    case 6 -> {
                        try {
                            word_pic = ImageIO.read(new File("src\\pic\\字\\black\\qizi_zu_black_3.png"));
                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                g.drawImage(word_pic, spacingLength, spacingLength, this.getWidth() - 2 * spacingLength, this.getHeight() - 2 * spacingLength, this);

            }
            if (color == ChessColor.RED){
                Image word_pic = null;
                switch (rank){
                    case 0 -> {
                        try {
                            word_pic = ImageIO.read(new File("src\\pic\\字\\red\\qizi_pao_red_3.png"));
                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                    case 1 -> {
                        try {
                            word_pic = ImageIO.read(new File("src\\pic\\字\\red\\qizi_shuai_red_3.png"));
                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                    case 2 -> {
                        try {
                            word_pic = ImageIO.read(new File("src\\pic\\字\\red\\qizi_shi_red_3.png"));
                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                    case 3 -> {
                        try {
                            word_pic = ImageIO.read(new File("src\\pic\\字\\red\\qizi_xiang_red_3.png"));
                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                    case 4 -> {
                        try {
                            word_pic = ImageIO.read(new File("src\\pic\\字\\red\\qizi_ju_red_3.png"));
                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                    case 5 -> {
                        try {
                            word_pic = ImageIO.read(new File("src\\pic\\字\\red\\qizi_ma_red_3.png"));
                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                    case 6 -> {
                        try {
                            word_pic = ImageIO.read(new File("src\\pic\\字\\red\\qizi_bing_red_3.png"));
                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                g.drawImage(word_pic, spacingLength, spacingLength, this.getWidth() - 2 * spacingLength, this.getHeight() - 2 * spacingLength, this);

            }

            //绘制棋子被选中时状态
            if (isSelected()) {
                Image select_pic = null;
                try {
                    select_pic = ImageIO.read(new File("src\\pic\\select.png"));
                }catch(Exception e) {
                    e.printStackTrace();
                }

                Graphics2D g2 = (Graphics2D) g;
//                g2.setStroke(new BasicStroke(4f));
                g2.drawImage(select_pic, spacingLength, spacingLength, this.getWidth() - 2 * spacingLength, this.getHeight() - 2 * spacingLength, this);
            }
        }
    }

    public String getName()
    {
        return this.name;
    }

    public ChessColor getChessColor()
    {
        return this.chessColor;
    }
    //这个方法用来比较两个棋子的大小
    //1 表示第一个棋子大，-1 表示第二个棋子大, 0表示一样大
}
