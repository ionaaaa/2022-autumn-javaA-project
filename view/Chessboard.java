package view;


import chessComponent.*;
import model.*;
import controller.ClickController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * 这个类表示棋盘组建，其包含：
 * SquareComponent[][]: 4*8个方块格子组件
 */
public class Chessboard extends JComponent {


    private static final int ROW_SIZE = 8;
    private static final int COL_SIZE = 4;

    private static SquareComponent[][] squareComponents = new SquareComponent[ROW_SIZE][COL_SIZE];
    //todo: you can change the initial player
    private ChessColor currentColor = ChessColor.BLACK;

    //all chessComponents in this chessboard are shared only one model controller
    public final ClickController clickController = new ClickController(this);
    private final int CHESS_SIZE;

    private static int BlackScore;
    private static int RedScore;
    //新增变量，红色，黑色的分数

    protected int ispanel;



    public int getpanel()
    {
        return this.ispanel;
    }

    public void setPanel(int panel){
        this.ispanel = panel;
    }

    private static List<String[][]> previousSteps = new ArrayList<>();

    public Chessboard(int width, int height) throws IOException {
        setLayout(null); // Use absolute layout.
        setSize(width + 2, height);
        CHESS_SIZE = 50;
        SquareComponent.setSpacingLength(CHESS_SIZE);
        System.out.printf("chessboard [%d * %d], chess size = %d\n", width, height, CHESS_SIZE);

        initAllChessOnBoard();
    }



    public static SquareComponent[][] getChessComponents() {
        return squareComponents;
    }

    public ChessColor getCurrentColor() {
        return currentColor;
    }

    public void setPreviousSteps(String[][] infoOfStep){
        previousSteps.add(infoOfStep);
    }

    public static void setPreviousSteps(List<String[][]> previousSteps) {
        Chessboard.previousSteps = previousSteps;
    }

    public List<String[][]> getPreviousSteps() {
        return previousSteps;
    }

    public void setCurrentColor(ChessColor currentColor) {
        this.currentColor = currentColor;
    }

    public static int getRedScore()
    {
        return RedScore;
    }

    public static int getBlackScore()
    {
        return BlackScore;
    }

    public void addRedScore(SquareComponent A)
    {
        int t1=A.getRank();
        if(t1==0) RedScore+=5;
        if(t1==1) RedScore+=30;
        if(t1==2) RedScore+=10;
        if(t1==3) RedScore+=5;
        if(t1==4) RedScore+=5;
        if(t1==5) RedScore+=5;
        if(t1==6) RedScore+=1;
    }

    public void addBlackScore(SquareComponent A)
    {
        int t1=A.getRank();
        if(t1==0) BlackScore+=5;
        if(t1==1) BlackScore+=30;
        if(t1==2) BlackScore+=10;
        if(t1==3) BlackScore+=5;
        if(t1==4) BlackScore+=5;
        if(t1==5) BlackScore+=5;
        if(t1==6) BlackScore+=1;
    }

    public static void setRedScore(int score)
    {
        Chessboard.RedScore=score;
    }

    public static void setBlackScore(int score)
    {
        Chessboard.BlackScore=score;
    }



    /**
     * 将SquareComponent 放置在 ChessBoard上。里面包含移除原有的component及放置新的component
     * @param squareComponent
     */
    public void putChessOnBoard(SquareComponent squareComponent) {
        int row = squareComponent.getChessboardPoint().getX(), col = squareComponent.getChessboardPoint().getY();
        if (squareComponents[row][col] != null) {
            remove(squareComponents[row][col]);
        }
        add(squareComponents[row][col] = squareComponent);
    }

    /**
     * 交换chess1 chess2的位置
     * @param chess1
     * @param chess2
     */
    public void
    swapChessComponents(SquareComponent chess1, SquareComponent chess2) {
        // Note that chess1 has higher priority, 'destroys' chess2 if exists.
        // 再度重写，对于onclick函数，已经判断过是否可以更换，所以在此只需要：chess1吃chess2，或者chess1和空格子互换
        if (!(chess2 instanceof EmptySlotComponent))
        {
            if(chess1.Chessfight(chess2)==1)
            {
                if(chess2.getChessColor()==ChessColor.RED)
                {
                    addBlackScore(chess2);
                }
                else if(chess2.getChessColor()==ChessColor.BLACK)
                {
                    addRedScore(chess2);
                }
                remove(chess2);
                add(chess2 = new EmptySlotComponent(chess2.getChessboardPoint(), chess2.getLocation(), clickController, CHESS_SIZE,-1));
                chess1.swapLocation(chess2);
            }
        }
        // 如果走到一个空格子，也需要swaplocation
        else
        {
            chess1.swapLocation(chess2);
        }
        int row1 = chess1.getChessboardPoint().getX(), col1 = chess1.getChessboardPoint().getY();
        squareComponents[row1][col1] = chess1;
        int row2 = chess2.getChessboardPoint().getX(), col2 = chess2.getChessboardPoint().getY();
        squareComponents[row2][col2] = chess2;

        //只重新绘制chess1 chess2，其他不变
        chess1.repaint();
        chess2.repaint();
    }

    public void initAllChessOnBoard() throws IOException {
        //初始化棋盘
        List<Integer> colorNum = new ArrayList<>();
        int counter = 0;
        while (counter < 16){
            colorNum.add(1);
            colorNum.add(2);
            counter++;
        }
        Collections.shuffle(colorNum);

        List<Integer> chessRed = new ArrayList<>();
        Collections.addAll(chessRed, 0, 0, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 6, 6, 6);
        Collections.shuffle(chessRed);
        List<Integer> chessBlack = new ArrayList<>();
        Collections.addAll(chessBlack, 0, 0, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 6, 6, 6);
        Collections.shuffle(chessBlack);


        for (int i = 0; i < squareComponents.length; i++) {
            for (int j = 0; j < squareComponents[i].length; j++) {
                ChessColor color = colorNum.get(0) == 1 ? ChessColor.RED : ChessColor.BLACK;
                colorNum.remove(0);

                SquareComponent squareComponent = null;

                if (color == ChessColor.RED){
                    int chessCategory = chessRed.get(0);
                    switch (chessCategory) {
                        case 0 ->
                                squareComponent = new CannonChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE, 0);
                        case 1 ->
                                squareComponent = new GeneralChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE, 1);
                        case 2 ->
                                squareComponent = new AdvisorChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE, 2);
                        case 3 ->
                                squareComponent = new MinisterChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE, 3);
                        case 4 ->
                                squareComponent = new ChariotChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE, 4);
                        case 5 ->
                                squareComponent = new HorseChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE, 5);
                        case 6 ->
                                squareComponent = new SoldierChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE, 6);
                    }
                    chessRed.remove(0);
                }

                if (color == ChessColor.BLACK){
                    int chessCategory = chessBlack.get(0);
                    switch (chessCategory) {
                        case 0 ->
                                squareComponent = new CannonChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE, 0);
                        case 1 ->
                                squareComponent = new GeneralChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE, 1);
                        case 2 ->
                                squareComponent = new AdvisorChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE, 2);
                        case 3 ->
                                squareComponent = new MinisterChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE, 3);
                        case 4 ->
                                squareComponent = new ChariotChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE, 4);
                        case 5 ->
                                squareComponent = new HorseChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE, 5);
                        case 6 ->
                                squareComponent = new SoldierChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE, 6);
                    }
                    chessBlack.remove(0);
                }

                assert squareComponent != null;
                putChessOnBoard(squareComponent);
                squareComponent.setVisible(true);
                squareComponent.repaint();
            }
        }
        String[][] info = generateInfoOfOneStep();
        setPreviousSteps(info);
        saveGame("src\\save_auto.txt");
        System.out.println("auto save successfully");
    }

    /**
     * 绘制棋盘格子
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Image board_pic = null;
        try {
            board_pic = ImageIO.read(new File("src\\pic\\M_ChessBoard_Board_FFQ.png"));
        }catch(Exception e) {
            e.printStackTrace();
        }
        assert board_pic != null;

        g.drawImage(board_pic,0, 0, this.getWidth(), this.getHeight(), this);

//        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    /**
     * 将棋盘上行列坐标映射成Swing组件的Point
     * @param row 棋盘上的行
     * @param col 棋盘上的列
     * @return
     */
    private Point calculatePoint(int row, int col) {
        int bias_x = col * 4;
        int bias_y = row * 4;

        return new Point(col * CHESS_SIZE + 88 + bias_x, row * CHESS_SIZE + 60 + bias_y);
    }

    /**
     * 通过GameController调用该方法

     */
    public void loadGame(List<String> chessData) {
//        List<String> chessData = this.loadDataStorage;
        //chessData是整个文档！！
        chessData.forEach(System.out::println);
        //文本每一行怎么对应
        //存棋盘，分数，过去的move

        List<String[][]> dataBatch = new ArrayList<>();

        int arrayNum = chessData.size() / 11;
        for (int a = 0; a < arrayNum; a++){
            String[][] temp_0 = new String[11][];

            for (int i = 0; i < 11; i++){
                temp_0[i] = chessData.get(a * 11 + i).split(" ");
                if ((i + 1) % 11 == 0 && (i+1) / 11 > 0){
                    try{
                        int counter = 0;
                        for (int row = 0; row < 8; row++){
                            for (int col = 0; col < temp_0[row].length; col++){
                                if (temp_0[row][col] != null){
                                    counter++;
                                }
                            }
                        }
                        if (counter == 32){
                            dataBatch.add(temp_0);
                        }else {
                            JOptionPane.showMessageDialog(this, "invalid board size\nerror:102");
                            throw new RuntimeException();
                        }

                        if (!Objects.equals(temp_0[10][0], "R") && !Objects.equals(temp_0[10][0], "B")){
                            JOptionPane.showMessageDialog(this, "no current player\nerror:104");
                            throw new RuntimeException();
                        }
                    }catch (IndexOutOfBoundsException e){
                        JOptionPane.showMessageDialog(this, "invalid board size\nerror:102");
                        throw new RuntimeException();
                    }
                }
            }

        }


        //按按钮，读list里最后一个string数组里的内容；如果undo的话往前读元素

//        前8行是棋盘，对应list的前8个元素
        if (dataBatch.size() > 1){
            for (int i = 0; i < dataBatch.size() - 1; i++){
                if (Objects.equals(dataBatch.get(i)[10][0], dataBatch.get(i + 1)[10][0])){
                    JOptionPane.showMessageDialog(this, "step lost\nerror:105");
                    throw new RuntimeException();
                }
            }
        }

        String[][] chessboardInfo = dataBatch.get(dataBatch.size()-1);

        List<String[]> chessboard_txt = new ArrayList<>();
        for (int i = 0; i < 8; i++){
            String[] temp = chessboardInfo[i];
            chessboard_txt.add(temp);
        }

        //遍历，根据文本找到对应的棋子
        SquareComponent[][] chessboard = new SquareComponent[8][4];
        for (int i = 0; i < chessboard.length; i++){
            for (int j = 0; j < chessboard[i].length; j++){
                char color = chessboard_txt.get(i)[j].charAt(0);
                char rank = chessboard_txt.get(i)[j].charAt(1);
                char reverse = chessboard_txt.get(i)[j].charAt(2);

                ChessColor colorTemp = ChessColor.NONE;
                switch (color){
                    case 'R' -> colorTemp = ChessColor.RED;
                    case 'B' -> colorTemp = ChessColor.BLACK;
                    case 'E' -> colorTemp = ChessColor.NONE;
                    default -> {
                        JOptionPane.showMessageDialog(this, "invalid chess\nerror:103");
                        throw new RuntimeException();
                    }
                }

                switch (rank) {
                    case '0' ->
                            chessboard[i][j] = new CannonChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), colorTemp, clickController, CHESS_SIZE, 0);
                    case '1' ->
                            chessboard[i][j] = new GeneralChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), colorTemp, clickController, CHESS_SIZE, 1);
                    case '2' ->
                            chessboard[i][j] = new AdvisorChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), colorTemp, clickController, CHESS_SIZE, 2);
                    case '3' ->
                            chessboard[i][j] = new MinisterChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), colorTemp, clickController, CHESS_SIZE, 3);
                    case '4' ->
                            chessboard[i][j] = new ChariotChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), colorTemp, clickController, CHESS_SIZE, 4);
                    case '5' ->
                            chessboard[i][j] = new HorseChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), colorTemp, clickController, CHESS_SIZE, 5);
                    case '6' ->
                            chessboard[i][j] = new SoldierChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), colorTemp, clickController, CHESS_SIZE, 6);
                    //注意rank
                    case '_' ->
                            chessboard[i][j] = new EmptySlotComponent(new ChessboardPoint(i, j), calculatePoint(i, j), clickController, CHESS_SIZE, -1);
                    default -> {
                        JOptionPane.showMessageDialog(this, "invalid chess\nerror:103");
                        throw new RuntimeException();
                    }

                }

                if (reverse == 't'){
                    chessboard[i][j].setReversal(true);
                }else if (reverse == 'f'){
                    chessboard[i][j].setReversal(false);
                }
            }
        }

        for (int i = 0; i < squareComponents.length; i++){
            for (int j = 0; j < squareComponents[i].length; j++){
                SquareComponent squareComponent = chessboard[i][j];
                squareComponent.setVisible(true);
                putChessOnBoard(squareComponent);
                squareComponent.repaint();
            }
        }

        //分数
        String red_score_str = chessboardInfo[8][0];
        int red_score_load = Integer.parseInt(red_score_str);
        setRedScore(red_score_load);

        String black_score_str = chessboardInfo[9][0];
        int black_score_load = Integer.parseInt(black_score_str);
        setBlackScore(black_score_load);

        int ten_black = Chessboard.getBlackScore() / 10;
        int one_black = Chessboard.getBlackScore() % 10;
        int ten_red = Chessboard.getRedScore() / 10;
        int one_red = Chessboard.getRedScore() % 10;
        ArrayList<ImageIcon> allNum = ChessGameFrame.getNumbers();
        ChessGameFrame.getFoldOfColor(1).setIcon(allNum.get(ten_black));
        ChessGameFrame.getFoldOfColor(2).setIcon(allNum.get(one_black));
        ChessGameFrame.getFoldOfColor(3).setIcon(allNum.get(ten_red));
        ChessGameFrame.getFoldOfColor(4).setIcon(allNum.get(one_red));

//        ChessGameFrame.getScoreLabelBlack().setText(String.format("BLACK SCORE:%d", Chessboard.getBlackScore()));
//        ChessGameFrame.getScoreLabelRed().setText(String.format("RED SCORE:%d", Chessboard.getRedScore()));

        //设置行棋方
        String currentColor_str = chessboardInfo[10][0];
        switch (currentColor_str){
            case "R" -> setCurrentColor(ChessColor.RED);
            case "B" -> setCurrentColor(ChessColor.BLACK);
        }
        ImageIcon blackLable = new ImageIcon("src\\pic\\black_label.png");
        ImageIcon redLable = new ImageIcon("src\\pic\\red_label.png");
        switch (getCurrentColor()) {
            case RED -> ChessGameFrame.getStatusLabel().setIcon(redLable);
            case BLACK -> ChessGameFrame.getStatusLabel().setIcon(blackLable);
        }

        List<String[][]> previousSteps = new ArrayList<>();
        Chessboard.setPreviousSteps(previousSteps);
        String[][] info = generateInfoOfOneStep();
        setPreviousSteps(info);

        try {
            saveGame("src\\save_auto.txt");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        System.out.println("auto save successfully!");

        //设置被吃的子
        List<String> chessEaten = detectChessEaten();
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

        JOptionPane.showMessageDialog(this, "load game successfully");

    }

//    public void load(List<String> chessData){
//        chessData.forEach(System.out::println);
//
//        List<String[]> dataBatch = new ArrayList<>();
//
//        for (int i = 0; i < chessData.size(); i++){
//            String[] temp = chessData.get(i).split(" ");
//            if (i % 11 <= 7){
//                if (temp.length < 4){
//                    JOptionPane.showMessageDialog(this, "invalid board size\nerror:102");
//                    throw new RuntimeException();
//                }
//            }else if (i % 11 == 10){
//                if (!Objects.equals(temp[0], "R")
//                        && !Objects.equals(temp[0], "B")){
//                    JOptionPane.showMessageDialog(this, "no current player\nerror:104");
//                    throw new RuntimeException();
//                }
//                if (i + 11 < chessData.size()){
//                    if (chessData.get(i).split(" ")[0] == chessData.get(i+11).split(" ")[0]){
//                        JOptionPane.showMessageDialog(this, "step lost\nerror:105");
//                        throw new RuntimeException();
//                    }
//                }
//            }else dataBatch.add(chessData.get(i).split(" "));
//        }
//
//        String[][] chessboardInfo = new String[11][];
//        for (int i = dataBatch.size() - 11; i < dataBatch.size(); i++){
//            chessboardInfo[i] = dataBatch.get(i);
//        }
//
//
//    }

    public void undo(List<String> chessData) throws IOException {
//        List<String> chessData = this.loadDataStorage;
        //chessData是整个文档！！
        chessData.forEach(System.out::println);
        //文本每一行怎么对应
        //存棋盘，分数，过去的move

        List<String[][]> dataBatch = new ArrayList<>();

        int arrayNum = chessData.size() / 11;
        for (int a = 0; a < arrayNum; a++){
            String[][] temp_0 = new String[11][];

            for (int i = 0; i < 11; i++){
                temp_0[i] = chessData.get(a * 11 + i).split(" ");
                if ((i + 1) % 11 == 0 && (i+1) / 11 > 0){
                    try{
                        int counter = 0;
                        for (int row = 0; row < 8; row++){
                            for (int col = 0; col < temp_0[row].length; col++){
                                if (temp_0[row][col] != null){
                                    counter++;
                                }
                            }
                        }
                        if (counter == 32){
                            dataBatch.add(temp_0);
                        }else {
                            JOptionPane.showMessageDialog(this, "invalid board size\nerror:102");
                            throw new RuntimeException();
                        }

                        if (!Objects.equals(temp_0[10][0], "R") && !Objects.equals(temp_0[10][0], "B")){
                            JOptionPane.showMessageDialog(this, "no current player\nerror:104");
                            throw new RuntimeException();
                        }
                    }catch (IndexOutOfBoundsException e){
                        JOptionPane.showMessageDialog(this, "invalid board size\nerror:102");
                        throw new RuntimeException();
                    }
                }
            }
        }

        //按按钮，读list里最后一个string数组里的内容；如果undo的话往前读元素

//        前8行是棋盘，对应list的前8个元素
        if (dataBatch.size() > 1){
            for (int i = 0; i < dataBatch.size() - 1; i++){
                if (Objects.equals(dataBatch.get(i)[10][0], dataBatch.get(i + 1)[10][0])){
                    JOptionPane.showMessageDialog(this, "step lost\nerror:105");
                    throw new RuntimeException();
                }
            }
        }

        //移除各种记录
        if (dataBatch.size() == 1){
            JOptionPane.showMessageDialog(this, "you cannot undo anymore!");
            throw new RuntimeException();
        }

        dataBatch.remove(dataBatch.size()-1);
        
        int counter = 0;
        while (counter < 11){
            chessData.remove(chessData.size()-1);
            counter++;
        }
        previousSteps.remove(previousSteps.size()-1);

        saveGame("src\\save_auto.txt");

        String[][] chessboardInfo = dataBatch.get(dataBatch.size()-1);

        List<String[]> chessboard_txt = new ArrayList<>();
        for (int i = 0; i < 8; i++){
            String[] temp = chessboardInfo[i];
            chessboard_txt.add(temp);
        }

        //遍历，根据文本找到对应的棋子
        SquareComponent[][] chessboard = new SquareComponent[8][4];
        for (int i = 0; i < chessboard.length; i++){
            for (int j = 0; j < chessboard[i].length; j++){
                char color = chessboard_txt.get(i)[j].charAt(0);
                char rank = chessboard_txt.get(i)[j].charAt(1);
                char reverse = chessboard_txt.get(i)[j].charAt(2);

                ChessColor colorTemp = ChessColor.NONE;
                switch (color){
                    case 'R' -> colorTemp = ChessColor.RED;
                    case 'B' -> colorTemp = ChessColor.BLACK;
                    case 'E' -> colorTemp = ChessColor.NONE;
                    default -> {
                        JOptionPane.showMessageDialog(this, "invalid chess\nerror:103");
                        throw new RuntimeException();
                    }
                }

                switch (rank) {
                    case '0' ->
                            chessboard[i][j] = new CannonChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), colorTemp, clickController, CHESS_SIZE, 0);
                    case '1' ->
                            chessboard[i][j] = new GeneralChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), colorTemp, clickController, CHESS_SIZE, 1);
                    case '2' ->
                            chessboard[i][j] = new AdvisorChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), colorTemp, clickController, CHESS_SIZE, 2);
                    case '3' ->
                            chessboard[i][j] = new MinisterChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), colorTemp, clickController, CHESS_SIZE, 3);
                    case '4' ->
                            chessboard[i][j] = new ChariotChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), colorTemp, clickController, CHESS_SIZE, 4);
                    case '5' ->
                            chessboard[i][j] = new HorseChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), colorTemp, clickController, CHESS_SIZE, 5);
                    case '6' ->
                            chessboard[i][j] = new SoldierChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), colorTemp, clickController, CHESS_SIZE, 6);
                    //注意rank
                    case '_' ->
                            chessboard[i][j] = new EmptySlotComponent(new ChessboardPoint(i, j), calculatePoint(i, j), clickController, CHESS_SIZE, -1);
                    default -> {
                        JOptionPane.showMessageDialog(this, "invalid chess\nerror:103");
                        throw new RuntimeException();
                    }

                }

                if (reverse == 't'){
                    chessboard[i][j].setReversal(true);
                }else if (reverse == 'f'){
                    chessboard[i][j].setReversal(false);
                }
            }
        }

        for (int i = 0; i < squareComponents.length; i++){
            for (int j = 0; j < squareComponents[i].length; j++){
                SquareComponent squareComponent = chessboard[i][j];
                squareComponent.setVisible(true);
                putChessOnBoard(squareComponent);
                squareComponent.repaint();
            }
        }

        //分数
        String red_score_str = chessboardInfo[8][0];
        int red_score_load = Integer.parseInt(red_score_str);
        setRedScore(red_score_load);

        String black_score_str = chessboardInfo[9][0];
        int black_score_load = Integer.parseInt(black_score_str);
        setBlackScore(black_score_load);

        int ten_black = Chessboard.getBlackScore() / 10;
        int one_black = Chessboard.getBlackScore() % 10;
        int ten_red = Chessboard.getRedScore() / 10;
        int one_red = Chessboard.getRedScore() % 10;
        ArrayList<ImageIcon> allNum = ChessGameFrame.getNumbers();
        ChessGameFrame.getFoldOfColor(1).setIcon(allNum.get(ten_black));
        ChessGameFrame.getFoldOfColor(2).setIcon(allNum.get(one_black));
        ChessGameFrame.getFoldOfColor(3).setIcon(allNum.get(ten_red));
        ChessGameFrame.getFoldOfColor(4).setIcon(allNum.get(one_red));

//        ChessGameFrame.getScoreLabelBlack().setText(String.format("BLACK SCORE:%d", Chessboard.getBlackScore()));
//        ChessGameFrame.getScoreLabelRed().setText(String.format("RED SCORE:%d", Chessboard.getRedScore()));

        //设置行棋方
        String currentColor_str = chessboardInfo[10][0];
        switch (currentColor_str){
            case "R" -> setCurrentColor(ChessColor.RED);
            case "B" -> setCurrentColor(ChessColor.BLACK);
        }
        ImageIcon blackLable = new ImageIcon("src\\pic\\black_label.png");
        ImageIcon redLable = new ImageIcon("src\\pic\\red_label.png");
        switch (getCurrentColor()) {
            case RED -> ChessGameFrame.getStatusLabel().setIcon(redLable);
            case BLACK -> ChessGameFrame.getStatusLabel().setIcon(blackLable);
        }

        //设置被吃的子
        List<String> chessEaten = detectChessEaten();
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

//        JOptionPane.showMessageDialog(this, "undo successfully");

    }

    public void saveGame(String path) throws IOException {
        File file = new File(path);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);


//        previousSteps.add(info);

        for (int i = 0; i < previousSteps.size(); i++){
            for (int j = 0; j < previousSteps.get(i).length; j++){
                for (int k = 0; k < previousSteps.get(i)[j].length; k++){
                    outputStreamWriter.write(previousSteps.get(i)[j][k] + " ");
                }
                outputStreamWriter.write("\n");
            }
        }

        outputStreamWriter.close();
    }

    //得到所有棋子的list
    public List<String> generateAllChessList(){
        List<String> allChessList = new ArrayList<>();

        for (int i = 0; i < 16; i++){
            String squareComponent_str;
            if (i == 0){
                squareComponent_str = "R1";
            } else if (i <= 2) {
                squareComponent_str = "R2";
            } else if (i <= 4) {
                squareComponent_str = "R3";
            } else if (i <= 6) {
                squareComponent_str = "R4";
            } else if (i <= 8) {
                squareComponent_str = "R5";
            } else if (i <= 13) {
                squareComponent_str = "R6";
            } else{
                squareComponent_str = "R0";
            }
            allChessList.add(squareComponent_str);
        }

        for (int i = 0; i < 16; i++){
            String squareComponent_str;
            if (i == 0){
                squareComponent_str = "B1";
            } else if (i <= 2) {
                squareComponent_str = "B2";
            } else if (i <= 4) {
                squareComponent_str = "B3";
            } else if (i <= 6) {
                squareComponent_str = "B4";
            } else if (i <= 8) {
                squareComponent_str = "B5";
            } else if (i <= 13) {
                squareComponent_str = "B6";
            } else{
                squareComponent_str = "B0";
            }
            allChessList.add(squareComponent_str);
        }
        return allChessList;
    }

    //    被吃掉的棋子：
    public List<String> detectChessEaten(){
        SquareComponent[][] squareComponentsCurrent = getChessComponents();

//    每点一次检查棋盘：遍历
        List<String> chessboardText = new ArrayList<>();
        for (int i = 0; i < squareComponentsCurrent.length; i++){
            for (int j = 0; j < squareComponentsCurrent[0].length; j++){
                if (squareComponents[i][j].getChessColor() == ChessColor.RED){
                    String rank_str = Integer.toString(squareComponents[i][j].getRank());
                    chessboardText.add("R" + rank_str);

                } else if (squareComponents[i][j].getChessColor() == ChessColor.BLACK) {
                    String rank_str = Integer.toString(squareComponents[i][j].getRank());
                    chessboardText.add("B" + rank_str);

                }
            }
        }

//        看少了哪些棋子：弄一个存有所有棋子的list，遍历棋盘的时候要是棋子在棋盘上就在列表里删除，最后出来的就是被吃掉的
        List<String> allChessList = generateAllChessList();
        for (int i = 0; i < chessboardText.size(); i++){
            allChessList.remove(chessboardText.get(i));
        }

        return allChessList;
    }

    //save：每一步都生成一个str数组，
    public String[][] generateInfoOfOneStep(){
        String[][] chessboardText = new String[11][4];
        squareComponents = getChessComponents();
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < chessboardText[0].length; j++){
                if (squareComponents[i][j].getChessColor() == ChessColor.RED){
                    String rank_str = Integer.toString(squareComponents[i][j].getRank());
                    if (squareComponents[i][j].isReversal()){
                        chessboardText[i][j] = "R" + rank_str + "t";
                    } else chessboardText[i][j] = "R" + rank_str + "f";
                } else if (squareComponents[i][j].getChessColor() == ChessColor.BLACK) {
                    String rank_str = Integer.toString(squareComponents[i][j].getRank());
                    if (squareComponents[i][j].isReversal()){
                        chessboardText[i][j] = "B" + rank_str + "t";
                    } else chessboardText[i][j] = "B" + rank_str + "f";
                } else chessboardText[i][j] = "E__";
            }
        }

        //分数
        int redScore = getRedScore();
        chessboardText[8][0] = Integer.toString(redScore);
        int blackScore = getBlackScore();
        chessboardText[9][0] = Integer.toString(blackScore);

        //当前行棋方
        ChessColor color = getCurrentColor();
        if(color == ChessColor.RED){
            chessboardText[10][0] = "R";
        }else if(color == ChessColor.BLACK){
            chessboardText[10][0] = "B";
        }

        return chessboardText;
    }



}
