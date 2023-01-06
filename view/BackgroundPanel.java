package view;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class BackgroundPanel extends JPanel {
    Image im;

    public BackgroundPanel(Image im) {
        this.im = im;
        this.setOpaque(true);
    }

    //Draw the back ground.
    public void paintComponent(Graphics g) {
        super.paintComponents(g);
        g.drawImage(im, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    public void setIm(String path) throws IOException {
        try {
            Image im1 = ImageIO.read(new File(path));
            this.im = im1;
        }catch (IOException e){
            e.printStackTrace();
        }


    }
}
