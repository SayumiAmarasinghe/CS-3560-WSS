package wss.gui;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import wss.gui.util.ImageLoader;

public class GameScreen extends JPanel {
    public GameScreen() {
        // create a JFrame - GUI window to add components to
        JFrame frame = new JFrame("WSS");        
        frame.setSize(500, 500);
        frame.getContentPane().setBackground(new Color(0x123456));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // create JLabel - text/image to display on the screen
        JLabel label = new JLabel("Welcome to WSS!");
        label.setForeground(Color.WHITE);
        label.setIcon(ImageLoader.loadIcon("player.png", 100, 100));
        frame.add(label);
        frame.setVisible(true); // make frame visible

    }

}
