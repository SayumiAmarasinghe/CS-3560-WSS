package wss.gui;

import java.awt.*;
import javax.swing.*;

public class GameOverScreen extends JFrame {
    public GameOverScreen(boolean isWin) {
        setTitle("WSS Game - Game Over");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(0x123456));
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 20, 10, 20);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2; // Span across two columns for centering
            gbc.anchor = GridBagConstraints.CENTER;

            //title 
            JLabel titleLabel = new JLabel("Game Over!");
            titleLabel.setFont(new Font("MV Boli", Font.BOLD, 60));
            titleLabel.setForeground(Color.WHITE);
            add(titleLabel, gbc);

            //win/loss msg 
            gbc.gridy = 1;
            String message = isWin ? "You Won!": "You Lost!";
            JLabel messageLabel = new JLabel(message);
            messageLabel.setFont(new Font("MV Boli", Font.PLAIN, 40));

            //gold = win, red = loss 
            messageLabel.setForeground(Color.WHITE);
            add(messageLabel, gbc);

            //buttons 
            gbc.gridy = 2;
            gbc.gridwidth = 1; // Reset to single column for buttons
            //add top padding to push buttons down 
            gbc.insets = new Insets(50, 20, 20, 20);

            //play again button 
            JButton playAgainButton = createStyledButton("Play Again");
            playAgainButton.addActionListener(e -> {
                dispose();
                new EntryScreen();
            });
            add(playAgainButton, gbc);

            gbc.gridx = 1;
            JButton exitBtn = createStyledButton("Exit");
            exitBtn.addActionListener(e -> System.exit(0));
            add(exitBtn, gbc);
        setLocationRelativeTo(null);
        setVisible(true);
        }
        private JButton createStyledButton(String text) {
            JButton btn = new JButton(text);
            btn.setFont(new Font("MV Boli", Font.BOLD, 24));
            btn.setBackground(Color.WHITE);
            return btn;
    }
    
}
