import view.ChessGameFrame;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                ChessGameFrame mainFrame = new ChessGameFrame(720, 720);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
//            mainFrame.setVisible(true);
        });
    }
}
