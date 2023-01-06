//package view;
//
//import javax.swing.*;
//
//public class StartingUI {
//    Chessboard
//
//    StartingUI(int width, int height){
//        JPanel startingPanel = new JPanel();
//        startingPanel.setSize(width, height);
//        startingPanel.setLayout(null);
//        ImageIcon bg = new ImageIcon("src\\pic\\start\\start.jpg");
//
//        BackgroundPanel bgp = new BackgroundPanel(bg.getImage());
//        bgp.setBounds(0, 0, bg.getIconWidth(), bg.getIconHeight());
//        bgp.setLayout(null);
//
//        ImageIcon pvp_icon = new ImageIcon("src\\pic\\start\\pvp.png");
//        JLabel pvp_Label = new JLabel(pvp_icon);
//        int label_width = pvp_icon.getIconWidth();
//        int label_height = pvp_icon.getIconHeight();
//        pvp_Label.setSize(label_width, label_height);
//        pvp_Label.setLocation(width * 4 / 10, height * 5 / 10);
//        bgp.add(pvp_Label);
//
//        JButton pvpButton = new JButton();
//        pvpButton.setLocation(width * 4 / 10, height * 5 / 10);
//        pvpButton.setSize(label_width, label_height);
//        pvpButton.setContentAreaFilled(false);
//        pvpButton.setBorderPainted(false);
//        bgp.add(pvpButton);
//
//        pvpButton.addActionListener(e -> {
//            Music soundEffect = new Music("src\\pic\\sound\\select.wav");
//            soundEffect.player();
//            switchPanel(startingPanel, playingPanel); //temp是存的startingPanel的copy
//            chessboard.ispanel=1;
//        });
//
//        ImageIcon pve_icon = new ImageIcon("src\\pic\\start\\pve.png");
//        JLabel pve_Label = new JLabel(pve_icon);
//        pve_Label.setSize(label_width, label_height);
//        pve_Label.setLocation(width * 6 / 10, height * 5 / 10);
//        bgp.add(pve_Label);
//
//        JButton pveButton = new JButton();
//        pveButton.setLocation(width * 6 / 10, height * 5 / 10);
//        pveButton.setSize(label_width, label_height);
//        pveButton.setContentAreaFilled(false);
//        pveButton.setBorderPainted(false);
//        bgp.add(pveButton);
//
//        pveButton.addActionListener(e -> {
//            Music soundEffect = new Music("src\\pic\\sound\\select.wav");
//            soundEffect.player();
//            switchPanel(startingPanel, playingPanel);
//            chessboard.ispanel=3;
//        });
//
//
//        startingPanel.add(bgp);
////        startingPanel.setVisible(true);
////        this.setBounds(0, 0, bg.getIconWidth(), bg.getIconHeight());
//    }
//
//}
