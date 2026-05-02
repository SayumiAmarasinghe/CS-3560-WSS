package wss.gui;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import wss.gui.util.ImageLoader;

public class EntryScreen extends JPanel {
    public EntryScreen() {
        // create a JFrame - GUI window to add components to
        JFrame frame = new JFrame("WSS");        
        frame.setSize(500, 500);
        frame.getContentPane().setBackground(new Color(0x123456));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // create JLabel - text/image to display on the screen
        // title
        JLabel title = new JLabel("Welcome to WSS Game!");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("MV Boli", Font.BOLD, 24));
        title.setIconTextGap(20);
        title.setIcon(ImageLoader.loadIcon("player.png", 100, 100));
        title.setVerticalTextPosition(JLabel.TOP);
        title.setHorizontalTextPosition(JLabel.CENTER);

        // add components to the frame
        frame.add(title);
        frame.setVisible(true); // make frame visible

    }

}
