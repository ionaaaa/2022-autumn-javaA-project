package controller;

import view.Chessboard;
import view.ChessGameFrame;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * 这个类主要完成由窗体上组件触发的动作。
 * 例如点击button等
 * ChessGameFrame中组件调用本类的对象，在本类中的方法里完成逻辑运算，将运算的结果传递至chessboard中绘制
 */
public class GameController {
    private Chessboard chessboard;
    ChessGameFrame mainFrame;

    public GameController(Chessboard chessboard) {
        this.chessboard = chessboard;
    }

    public List<String> loadGameFromFile(String path) {
        if (!path.endsWith(".txt")){
            JOptionPane.showMessageDialog(mainFrame, "invalid file format\nerror:101");
            throw new RuntimeException();
        }
        try {
            List<String> chessData = Files.readAllLines(Path.of(path)); //读取文件
            chessboard.loadGame(chessData); //使用loadGame方法将文本映射到棋盘
            return chessData;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(mainFrame, "file not found");
            e.printStackTrace();
        }
        return null;
    }

    public void saveGameToFile() {
        try {
            this.chessboard.saveGame("src\\save_manual.txt"); //使用loadGame方法将文本映射到棋盘
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void undo(String path) {
//        if (!path.endsWith(".txt")){
//            JOptionPane.showMessageDialog(mainFrame, "invalid file format\nerror:101");
//            throw new RuntimeException();
//        }
        try {
            List<String> chessData = Files.readAllLines(Path.of(path)); //读取文件
            chessboard.undo(chessData); //使用loadGame方法将文本映射到棋盘
        } catch (IOException e) {
            JOptionPane.showMessageDialog(mainFrame, "file not found");
            e.printStackTrace();
        }
    }

}
