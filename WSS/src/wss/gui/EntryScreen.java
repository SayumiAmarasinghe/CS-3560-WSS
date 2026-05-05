package wss.gui;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.*;
import wss.gui.util.ImageLoader;

public class EntryScreen extends JPanel {
    private JLabel title;
    private JButton instructBtn;
    private JButton startBtn;
    private JFrame frame;

    public EntryScreen() {
        frame = new JFrame("WSS");
        frame.setSize(500, 500);
        frame.setMinimumSize(new Dimension(400, 400)); // Prevents UI collapse
        frame.getContentPane().setBackground(new Color(0x123456));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Using GridBagLayout for perfect centering
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // --- TITLE & ICON ---
        title = new JLabel("Welcome to WSS Game!");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("MV Boli", Font.BOLD, 24));
        title.setIcon(ImageLoader.loadIcon("player.png", 100, 100));
        title.setVerticalTextPosition(JLabel.TOP);
        title.setHorizontalTextPosition(JLabel.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20); // Spacing
        gbc.anchor = GridBagConstraints.CENTER;
        frame.add(title, gbc);

        // --- BUTTONS PANEL ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);

        instructBtn = createStyledButton("How to Play");
        instructBtn.addActionListener(e -> new InstrScreen());

        startBtn = createStyledButton("Start Game");
        
        buttonPanel.add(instructBtn);
        buttonPanel.add(startBtn);

        gbc.gridy = 1; // Position buttons below the title
        frame.add(buttonPanel, gbc);

        // --- ADD RESPONSIVE SCALING ---
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                scaleUI();
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("MV Boli", Font.PLAIN,18));
        btn.setBackground(Color.WHITE);
        return btn;
    }

    /**
     * Dynamically adjusts fonts and icon sizes based on frame dimensions
     */
    private void scaleUI() {
        int width = frame.getWidth();
        int height = frame.getHeight();
        
        // Scale Font Size (Ratio based on original 500px width)
        int newFontSize = Math.max(16, width / 20);
        title.setFont(new Font("MV Boli", Font.BOLD, newFontSize));
        
        // Scale Icon Size
        int iconSize = Math.max(50, width / 5);
        title.setIcon(ImageLoader.loadIcon("player.png", iconSize, iconSize));

        // Scale Buttons
      Dimension d1 = instructBtn.getPreferredSize();
    instructBtn.setPreferredSize(new Dimension(d1.width + 20, d1.height + 10));
    
    Dimension d2 = startBtn.getPreferredSize();
    startBtn.setPreferredSize(new Dimension(d2.width + 20, d2.height + 10));

    frame.revalidate();
    }
}