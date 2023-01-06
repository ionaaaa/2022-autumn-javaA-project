package view;

import controller.GameController;
import model.ChessColor;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import static java.awt.Image.SCALE_DEFAULT;


/**
 * 这个类表示游戏窗体，窗体上包含：
 * 1 Chessboard: 棋盘
 * 2 JLabel:  标签
 * 3 JButton： 按钮
 */
public class ChessGameFrame extends JFrame {
    private int WIDTH = 720;
    private int HEIGHT = 720;
    public int CHESSBOARD_SIZE = HEIGHT * 4 / 5;
    private GameController gameController;

    private static BackgroundPanel bgp;

//    private final JPanel playingPanel = (JPanel)this.getContentPane();
    private final JPanel playingPanel = new JPanel();
//    private final JPanel startingPanel = (JPanel)this.getContentPane();
    private final JPanel startingPanel = new JPanel();

    private JPanel tempPanel = new JPanel();

    private static JLabel statusLabel = new JLabel();
    private static JLabel PVPLabel = new JLabel();
    private static JLabel tenfold_black = new JLabel();
    private static JLabel onefold_black = new JLabel();
    private static JLabel tenfold_red = new JLabel();
    private static JLabel onefold_red = new JLabel();
    private static JLabel win_label = new JLabel();
    private static JLabel scoreLabelBlack;
    private static JLabel scoreLabelRed;
    private Chessboard chessboard = new Chessboard(391, this.CHESSBOARD_SIZE);
    private static boolean onCheatMode;

    private static ArrayList<ImageIcon> numbers = new ArrayList<>();
    private static ImageIcon[][] chessIcon = new ImageIcon[2][7];

    private static JLabel[][] chessEaten = new JLabel[4][7];


    private List<String> chessEatenList = new ArrayList<String>();


    //添加了cheatmode 变量，true表示开启作弊，false表示作弊模式关闭
    public ChessGameFrame(int width, int height) throws IOException {
        setTitle("2022 CS109 Project Demo"); //设置标题
        this.WIDTH = width;
        this.HEIGHT = height;
        this.CHESSBOARD_SIZE = HEIGHT * 4 / 5;
        this.chessEatenList = chessboard.detectChessEaten();

        for (int i = 0; i < 10; i++){
            String path = "src\\pic\\字\\score\\" + i + ".png";
            ImageIcon temp = new ImageIcon(path);
            numbers.add(temp);
        }

        setChessIcon();

        setPlayingUI();
        setStartingUI();

        tempPanel.setSize(this.WIDTH, this.HEIGHT);

        this.add(startingPanel);

        System.out.println(tempPanel.hashCode());
        System.out.println(startingPanel.hashCode());


        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了

        this.setVisible(true);


    }

    public void setChessIcon(){
        String[][] chessName = {{"pao", "shuai", "shi", "xiang", "ju", "ma", "bing"},
                {"pao", "jiang", "shi", "xiang", "ju", "ma", "zu"}};
        for (int i = 0; i < 7 ; i++){
            String f_name = "qizi_" + chessName[0][i] + "_red_1.png";
            ImageIcon temp = new ImageIcon("src\\pic\\字\\red\\" + f_name);
            chessIcon[0][i] = temp;
        }
        for (int i = 0; i < 7 ; i++){
            String f_name = "qizi_" + chessName[1][i] + "_black_1.png";
            ImageIcon temp = new ImageIcon("src\\pic\\字\\black\\" + f_name);
            chessIcon[1][i] = temp;
        }
    }

    /**
     * 在游戏窗体中添加棋盘
     */
    private void addChessboard(BackgroundPanel bgp) {

        gameController = new GameController(chessboard);
        chessboard.setLocation(-16, 12);
        bgp.add(chessboard);
        chessboard.setVisible(true);
    }

    /**
     * 在游戏窗体中添加标签
     */
    private void addLabel(BackgroundPanel bgp) {
        ImageIcon blackLable = new ImageIcon("src\\pic\\black_label.png");
        statusLabel = new JLabel(blackLable);

        statusLabel.setBounds(this.WIDTH * 3 / 5, this.HEIGHT * 15 / 100, 200, 60);
        bgp.add(statusLabel);
    }

    private void addscoreLabel(BackgroundPanel bgp)
    {
        ImageIcon blackLable = new ImageIcon("src\\pic\\black_label.png");
        scoreLabelBlack = new JLabel(blackLable);
        scoreLabelBlack.setLocation(WIDTH / 2, HEIGHT * 5 / 10);
        scoreLabelBlack.setSize(200, 60);
        bgp.add(scoreLabelBlack);

        tenfold_black.setIcon(numbers.get(0));
        tenfold_black.setLocation(WIDTH / 2 + 150, HEIGHT * 5 / 10);
        tenfold_black.setSize(200, 60);

        onefold_black.setIcon(numbers.get(0));
        onefold_black.setLocation(WIDTH / 2 + 180, HEIGHT * 5 / 10);
        onefold_black.setSize(200, 60);

        bgp.add(tenfold_black);
        bgp.add(onefold_black);

        ImageIcon redLable = new ImageIcon("src\\pic\\red_label.png");
        scoreLabelRed = new JLabel(redLable);
        scoreLabelRed.setLocation(WIDTH / 2, HEIGHT * 6 / 10);
        scoreLabelRed.setSize(200, 60);
        bgp.add(scoreLabelRed);

        tenfold_red.setIcon(numbers.get(0));
        tenfold_red.setLocation(WIDTH / 2 + 150, HEIGHT * 6 / 10);
        tenfold_red.setSize(200, 60);

        onefold_red.setIcon(numbers.get(0));
        onefold_red.setLocation(WIDTH / 2 + 180, HEIGHT * 6 / 10);
        onefold_red.setSize(200, 60);

        bgp.add(tenfold_red);
        bgp.add(onefold_red);

    }

    private void addPVPLabel(BackgroundPanel bgp) {
        ImageIcon lable = new ImageIcon("src\\pic\\pvp_label.png");

        PVPLabel.setLayout(null);

        int width_label = lable.getIconWidth();
        int height_label = lable.getIconHeight();

        PVPLabel = new JLabel(lable);
        PVPLabel.setSize(width_label, height_label);
        PVPLabel.setLocation(this.WIDTH * 65 / 100, 30);
        bgp.add(PVPLabel);
    }

    public static JLabel getStatusLabel() {
        return statusLabel;
    }

    public static JLabel getPVPLabel(){
        return PVPLabel;
    }

    public static JLabel getEatenLabel(int row, int col){
        if (row == 0){
            return chessEaten[1][col];
        }else return chessEaten[3][col];
    }

    public static JLabel getFoldOfColor(int num){
        switch (num){
            case 1 -> {
                return tenfold_black;
            }
            case 2 -> {
                return onefold_black;
            }
            case 3 -> {
                return tenfold_red;
            }
            case 4 -> {
                return onefold_red;
            }
            default -> {
                return null;
            }
        }
    }

    public static ArrayList<ImageIcon> getNumbers() {
        return numbers;
    }

    public static boolean getOnCheatMode()
    {
        return onCheatMode;
    }

    public static JLabel getWin_label() {
        return win_label;
    }

    private void addrestartButton(BackgroundPanel bgp) {
        ImageIcon restartLable = new ImageIcon("src\\pic\\restart_button2.png");

        JLabel restartButton = new JLabel(restartLable);
        restartButton.setLayout(null);

        int width_label = restartLable.getIconWidth();
        int height_label = restartLable.getIconHeight();

        restartButton.setSize(width_label, height_label);
        restartButton.setLocation(this.WIDTH / 2, this.HEIGHT / 10 + 200);
        bgp.add(restartButton);

        JButton button = new JButton();
        button.setLocation(WIDTH / 2, HEIGHT / 10 + 200);
        button.setSize(width_label, height_label);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        bgp.add(button);

        button.addActionListener(e -> {
            Music soundEffect = new Music("src\\pic\\sound\\select.wav");
            soundEffect.player();

            restart();
        });
    }

    private void addCheatButton(BackgroundPanel bgp) {
        ImageIcon cheatLable = new ImageIcon("src\\pic\\cheat_button.png");

        int width_label = cheatLable.getIconWidth();
        int height_label = cheatLable.getIconHeight();

        JLabel cheatButton = new JLabel(cheatLable);
        cheatButton.setLayout(null);
        cheatButton.setSize(width_label, height_label);
        cheatButton.setLocation(this.WIDTH * 3 / 4, this.HEIGHT / 10 + 200);
        bgp.add(cheatButton);
        
        
        JButton button = new JButton();
        button.addActionListener(e ->
        {
            Music soundEffect = new Music("src\\pic\\sound\\select.wav");
            soundEffect.player();
            if(!onCheatMode)
            {
                JOptionPane.showMessageDialog(this, "Now we enter Cheat Mode");
                onCheatMode=true;
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Cheat Mode off");
                onCheatMode=false;
            }
        });
        button.setLocation(WIDTH * 3 / 4, HEIGHT / 10 + 200);
        button.setSize(width_label, height_label);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        bgp.add(button);
    }

    private void addLoadButton(BackgroundPanel bgp) {
        ImageIcon loadLable = new ImageIcon("src\\pic\\load_button.png");

        int width_label = loadLable.getIconWidth();
        int height_label = loadLable.getIconHeight();

        JLabel loadButton = new JLabel(loadLable);
        loadButton.setLayout(null);
        loadButton.setSize(width_label, height_label);
        loadButton.setLocation(this.WIDTH * 3 / 4, this.HEIGHT / 10 + 120);
        bgp.add(loadButton);

        JButton button = new JButton();
        button.setLocation(WIDTH * 3 / 4, HEIGHT / 10 + 120);
        button.setSize(width_label, height_label);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        bgp.add(button);

        button.addActionListener(e -> {
            Music soundEffect = new Music("src\\pic\\sound\\select.wav");
            soundEffect.player();

            System.out.println("Click load");
            String path = JOptionPane.showInputDialog(this, "Input Path here");
            gameController.loadGameFromFile(path);
        });

    }

    private void addSaveButton(BackgroundPanel bgp) {
        ImageIcon saveLable = new ImageIcon("src\\pic\\save_button.png");
        int width_label = saveLable.getIconWidth();
        int height_label = saveLable.getIconHeight();

        JLabel loadButton = new JLabel(saveLable);
        loadButton.setLayout(null);
        loadButton.setSize(width_label, height_label);
        loadButton.setLocation(this.WIDTH / 2, this.HEIGHT / 10 + 120);
        bgp.add(loadButton);

        JButton button = new JButton();
        button.setLocation(WIDTH / 2, HEIGHT / 10 + 120);
        button.setSize(width_label, height_label);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        bgp.add(button);

        button.addActionListener(e -> {
            Music soundEffect = new Music("src\\pic\\sound\\select.wav");
            soundEffect.player();

            System.out.println("Click save");
            gameController.saveGameToFile();
            JOptionPane.showMessageDialog(this, "save game successfully");
        });
    }

    public static void addChessEaten(BackgroundPanel bgp) throws IOException {
//        展示出来


        for (int i = 0; i < 7; i++){
            chessEaten[0][i] = new JLabel(chessIcon[0][i]);
            chessEaten[0][i].setLocation(90 * i + 50, 550);
            chessEaten[0][i].setSize(chessIcon[0][i].getIconWidth(), chessIcon[0][i].getIconHeight());
            bgp.add(chessEaten[0][i]);
            chessEaten[1][i] = new JLabel(numbers.get(0));
            chessEaten[1][i].setLocation(90 * i + 105, 550);
            chessEaten[1][i].setSize(numbers.get(0).getIconWidth(), numbers.get(0).getIconHeight());
            bgp.add(chessEaten[1][i]);
            chessEaten[2][i] = new JLabel(chessIcon[1][i]);
            chessEaten[2][i].setLocation(90 * i + 50, 600);
            chessEaten[2][i].setSize(chessIcon[1][i].getIconWidth(), chessIcon[1][i].getIconHeight());
            bgp.add(chessEaten[2][i]);
            chessEaten[3][i] = new JLabel(numbers.get(0));
            chessEaten[3][i].setLocation(90 * i + 105, 600);
            chessEaten[3][i].setSize(numbers.get(0).getIconWidth(), numbers.get(0).getIconHeight());
            bgp.add(chessEaten[3][i]);
        }

        for(int i = 0; i < 2; i++){
            for (int j = 0; j < 7; j++){
                try {
                    Image img = ImageIO.read(new File("src\\pic\\qizi_1.png"));
                    img = img.getScaledInstance(50, 50, SCALE_DEFAULT);
                    ImageIcon temp_icon = new ImageIcon(img);
                    JLabel temp = new JLabel(temp_icon);
                    temp.setLocation(90 * j + 50, 550 + 50 * i);
                    temp.setSize(temp_icon.getIconWidth(), temp_icon.getIconHeight());
                    bgp.add(temp);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }


    }

    public void addWinLable(BackgroundPanel bgp){
        win_label.setSize(720, 720);
        win_label.setLocation(0, 0);

        bgp.add(win_label);
    }

    public void addSettingButton(BackgroundPanel bgp){
        ImageIcon saveLable = new ImageIcon("src\\pic\\setting.png");
        int width_label = saveLable.getIconWidth();
        int height_label = saveLable.getIconHeight();

        JLabel loadButton = new JLabel(saveLable);
        loadButton.setLayout(null);
        loadButton.setSize(width_label, height_label);
        loadButton.setLocation(650, 32);
        bgp.add(loadButton);

        JButton button = new JButton();
        button.setLocation(650, 32);
        button.setSize(width_label, height_label);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        bgp.add(button);

        Object[] selectedValues = new Object[]{"1", "2", "3"};

        button.addActionListener(e -> {
            Music soundEffect = new Music("src\\pic\\sound\\select.wav");
            soundEffect.player();

            Object input = JOptionPane.showInputDialog(this, "Change background picture", "Background Picture Selection",
                    JOptionPane.PLAIN_MESSAGE, null, selectedValues, selectedValues[1]);
            try {
                bgp.setIm("src\\pic\\bg" + input + ".jpg");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            bgp.repaint();

        });
    }

    public void addBackButton(BackgroundPanel bgp){
        ImageIcon icon = new ImageIcon("src\\pic\\back.png");
        int width_label = icon.getIconWidth();
        int height_label = icon.getIconHeight();

        JLabel label = new JLabel(icon);
        label.setLayout(null);
        label.setSize(width_label, height_label);
        label.setLocation(500, 32);
        bgp.add(label);

        JButton button = new JButton();
        button.setLocation(500, 32);
        button.setSize(width_label, height_label);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        bgp.add(button);

        button.addActionListener(e -> {
            Music soundEffect = new Music("src\\pic\\sound\\select.wav");
            soundEffect.player();
            switchPanel(startingPanel, tempPanel);

//            switchPanel(currentPanel, startingPanel);
//            currentPanel = startingPanel;
//            currentPanel.repaint();
        });
    }

    public void addSwitchButton(BackgroundPanel bgp){
        ImageIcon saveLable = new ImageIcon("src\\pic\\switch.png");
        int width_label = saveLable.getIconWidth();
        int height_label = saveLable.getIconHeight();

        JLabel loadButton = new JLabel(saveLable);
        loadButton.setLayout(null);
        loadButton.setSize(width_label, height_label);
        loadButton.setLocation(350, 32);
        bgp.add(loadButton);

        JButton button = new JButton();
        button.setLocation(350, 32);
        button.setSize(width_label, height_label);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        bgp.add(button);

        button.addActionListener(e -> {
            Music soundEffect = new Music("src\\pic\\sound\\select.wav");
            soundEffect.player();

            if (chessboard.getpanel() == 1){
                int result = JOptionPane.showConfirmDialog(this, "switch to pve?");
                if (result == 0){
                    restart();
                    ImageIcon switchIcon = new ImageIcon("src\\pic\\primary.png");
                    PVPLabel.setIcon(switchIcon);
                    PVPLabel.setSize(switchIcon.getIconWidth(), switchIcon.getIconHeight());
                    chessboard.ispanel = 3;
                }
            } else if (chessboard.getpanel() == 2 || chessboard.getpanel() == 3){
                int result = JOptionPane.showConfirmDialog(this, "switch to pvp?");
                if (result == 0){
                    restart();
                    ImageIcon switchIcon = new ImageIcon("src\\pic\\pvp_label.png");
                    PVPLabel.setIcon(switchIcon);
                    PVPLabel.setSize(switchIcon.getIconWidth(), switchIcon.getIconHeight());
                    chessboard.ispanel = 1;
                }
            }
            bgp.repaint();

        });
    }

    public void addUndoButton(BackgroundPanel bgp){
        ImageIcon icon = new ImageIcon("src\\pic\\undo.png");
        int width_label = icon.getIconWidth();
        int height_label = icon.getIconHeight();

        JLabel label = new JLabel(icon);
        label.setLayout(null);
        label.setSize(width_label, height_label);
        label.setLocation(600, 382);
        bgp.add(label);

        JButton button = new JButton();
        button.setLocation(600, 382);
        button.setSize(width_label, height_label);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        bgp.add(button);

        button.addActionListener(e -> {
            Music soundEffect = new Music("src\\pic\\sound\\select.wav");
            soundEffect.player();
//            switchPanel(startingPanel, tempPanel);

            System.out.println("Click undo");
            String path = "src\\save_auto.txt";
//            gameController.loadGameFromFile(path);

            if (chessboard.ispanel == 1){
                gameController.undo(path);
            }else JOptionPane.showMessageDialog(this, "you cannot undo in pve mode!");

//            JOptionPane.showMessageDialog(this, "undo");
        });
    }

    public void setPlayingUI() throws IOException {
        playingPanel.setSize(this.WIDTH, this.HEIGHT);
        playingPanel.setLayout(null);

        ImageIcon bg = new ImageIcon("src\\pic\\bg2.jpg");

        bgp = new BackgroundPanel(bg.getImage());
        bgp.setBounds(0, 0, bg.getIconWidth(), bg.getIconHeight());
        bgp.setLayout(null);

        addWinLable(bgp);
        addChessboard(bgp);
        addLabel(bgp);
        addscoreLabel(bgp);
        addPVPLabel(bgp);

        addrestartButton(bgp);
        addLoadButton(bgp);
        addSaveButton(bgp);
        addCheatButton(bgp);
        addChessEaten(bgp);
        addSettingButton(bgp);
        addUndoButton(bgp);
        addSwitchButton(bgp);

        playingPanel.add(bgp);

        this.setBounds(0, 0, bg.getIconWidth(), bg.getIconHeight());

    }

    public void setStartingUI(){
        startingPanel.setSize(this.WIDTH, this.HEIGHT);
        startingPanel.setLayout(null);
        ImageIcon bg = new ImageIcon("src\\pic\\start\\start.jpg");

        bgp = new BackgroundPanel(bg.getImage());
        bgp.setBounds(0, 0, bg.getIconWidth(), bg.getIconHeight());
        bgp.setLayout(null);

        ImageIcon pvp_icon = new ImageIcon("src\\pic\\start\\pvp.png");
        JLabel pvp_Label = new JLabel(pvp_icon);
        int label_width = pvp_icon.getIconWidth();
        int label_height = pvp_icon.getIconHeight();
        pvp_Label.setSize(label_width, label_height);
        pvp_Label.setLocation(this.WIDTH * 2 / 10, this.HEIGHT * 5 / 10);
        bgp.add(pvp_Label);

        JButton pvpButton = new JButton();
        pvpButton.setLocation(this.WIDTH * 2 / 10, this.HEIGHT * 5 / 10);
        pvpButton.setSize(label_width, label_height);
        pvpButton.setContentAreaFilled(false);
        pvpButton.setBorderPainted(false);
        bgp.add(pvpButton);

        pvpButton.addActionListener(e -> {
            Music soundEffect = new Music("src\\pic\\sound\\select.wav");
            soundEffect.player();
            switchPanel(startingPanel, playingPanel); //temp是存的startingPanel的copy
            chessboard.ispanel=1;
        });

        ImageIcon pve_icon = new ImageIcon("src\\pic\\start\\leave.png");
        JLabel pve_Label = new JLabel(pve_icon);
        pve_Label.setSize(label_width, label_height);
        pve_Label.setLocation(this.WIDTH * 6 / 10, this.HEIGHT * 5 / 10);
        bgp.add(pve_Label);

        JButton pveButton = new JButton();
        pveButton.setLocation(this.WIDTH * 6 / 10, this.HEIGHT * 5 / 10);
        pveButton.setSize(label_width, label_height);
        pveButton.setContentAreaFilled(false);
        pveButton.setBorderPainted(false);
        bgp.add(pveButton);

        pveButton.addActionListener(e -> {
//            Music soundEffect = new Music("src\\pic\\sound\\select.wav");
            this.dispose();
        });


        startingPanel.add(bgp);
//        startingPanel.setVisible(true);
        this.setBounds(0, 0, bg.getIconWidth(), bg.getIconHeight());

    }

    public static void switchPanel(JPanel oldPanel, JPanel newPanel){
        /*
        会返回一份旧的panel的copy
         */
//        JPanel temp = new JPanel();
//        temp.add(oldPanel);
        oldPanel.removeAll();//移除面板中的所有组件
        oldPanel.add(newPanel);//添加要切换的面板
        oldPanel.repaint();//刷新页面，重绘面板
        oldPanel.validate();//使重绘的面板确认生效
//        return temp;
    }

    public void restart(){
        try {
            chessboard.initAllChessOnBoard();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        chessboard.setRedScore(0);
        chessboard.setBlackScore(0);


        tenfold_black.setIcon(numbers.get(0));
        onefold_black.setIcon(numbers.get(0));
        tenfold_red.setIcon(numbers.get(0));
        onefold_red.setIcon(numbers.get(0));

        chessboard.setCurrentColor(ChessColor.BLACK);

        ImageIcon blackLable = new ImageIcon("src\\pic\\black_label.png");
        statusLabel.setIcon(blackLable);

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

