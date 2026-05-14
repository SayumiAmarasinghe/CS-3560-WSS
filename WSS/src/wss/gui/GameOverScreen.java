package wss.gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

        // --- TITLE ---
        JLabel titleLabel = new JLabel("Game Over!");
        titleLabel.setFont(new Font("MV Boli", Font.BOLD, 70)); // Made slightly larger
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, gbc);

        // --- WIN/LOSS MESSAGE ---
        gbc.gridy = 1;
        String message = isWin ? "You Won!" : "You Lost!";
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("MV Boli", Font.PLAIN, 45));

        //gold for win, red for loss 
        Color resultColor = isWin? new Color(0xFFD700) : new Color(0xFF6347);
        messageLabel.setForeground(resultColor);
        add(messageLabel, gbc);

        //buttons 
        gbc.gridy = 2;
        gbc.gridwidth = 1; 
        gbc.insets = new Insets(60, 20, 20, 20);

        //play again button 
        JButton playAgainBtn = createStyledButton("Play Again", resultColor);
        playAgainBtn.addActionListener(e -> {
            dispose(); // Close the game over screen
            new EntryScreen(); // Return to entry screen
        });
        add(playAgainBtn, gbc);

        //exit button
        gbc.gridx = 1;
        JButton exitBtn = createStyledButton("Exit", resultColor);
        exitBtn.addActionListener(e -> System.exit(0));
        add(exitBtn, gbc);

        setLocationRelativeTo(null); 
        setVisible(true);

    }

    private JButton createStyledButton(String text, Color hoverColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("MV Boli", Font.BOLD, 24));
    
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(0x123456));

        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        //custom border w/padding 
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2),
            BorderFactory.createEmptyBorder(10, 30, 10, 30)
        ));

        //add hover affect 
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(hoverColor);
                btn.setForeground(new Color (0x123456));
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(hoverColor, 2),
                    BorderFactory.createEmptyBorder(10, 30, 10, 30)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(0x123456));
                btn.setForeground(Color.WHITE);
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.WHITE, 2),
                    BorderFactory.createEmptyBorder(10, 30, 10, 30)
                ));
            }
        });
        return btn;
    }
}