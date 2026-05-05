package wss.gui;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class GameScreen extends JFrame {
    private JPanel mapPanel;
    private JProgressBar foodBar, waterBar, moveBar;
    private JLabel[][] gridLabels;
    
    // Mock data based on project requirements
    private int playerX = 0, playerY = 0;
    private final int width = 10, height = 10;
    
    public GameScreen() {
        setTitle("WSS Game - Journey East");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(0x123456));
        setLayout(new BorderLayout(10, 10));

        // --- TOP RIGHT: STATS DASHBOARD ---
        JPanel statsPanel = createStatsDashboard();
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(statsPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // --- CENTER: TERRAIN MAP ---
        mapPanel = new JPanel(new GridLayout(height, width, 2, 2));
        mapPanel.setBackground(Color.BLACK);
        gridLabels = new JLabel[height][width];
        initializeMap();
        add(mapPanel, BorderLayout.CENTER);

        // --- CONTROLS ---
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleMovement(e.getKeyCode());
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createStatsDashboard() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE), "PLAYER RESERVES", 
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, 
            new Font("MV Boli", Font.PLAIN, 14), Color.WHITE));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Initialize Progress Bars [cite: 138, 139, 140]
        foodBar = createBar(Color.ORANGE, "Food");
        waterBar = createBar(new Color(0x00BFFF), "Water");
        moveBar = createBar(Color.GREEN, "Move");

        addStatRow(panel, "Food:", foodBar, 0);
        addStatRow(panel, "Water:", waterBar, 1);
        addStatRow(panel, "Move:", moveBar, 2);

        return panel;
    }

    private void initializeMap() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                JLabel tile = new JLabel();
                tile.setOpaque(true);
                // Assign colors based on terrain types [cite: 204, 233, 234, 235]
                tile.setBackground(getTerrainColor(x, y)); 
                tile.setBorder(BorderFactory.createLineBorder(new Color(0,0,0,50)));
                gridLabels[y][x] = tile;
                mapPanel.add(tile);
            }
        }
        updatePlayerIcon();
    }

    private Color getTerrainColor(int x, int y) {
        // Simple logic to demonstrate different terrains [cite: 233-238]
        if (x == 9) return new Color(0xFFD700); // East Edge/Exit [cite: 15, 497]
        int type = (x + y) % 6;
        switch(type) {
            case 0: return new Color(0x34A853); // Plains
            case 1: return new Color(0x707070); // Mountains
            case 2: return new Color(0xEDC9AF); // Desert
            case 3: return new Color(0x0B6623); // Forest
            case 4: return new Color(0x4A5D23); // Swamp
            default: return new Color(0xA5F2F3); // Tundra
        }
    }

    private void handleMovement(int keyCode) {
        // Move logic: Pay costs and update bars [cite: 202, 212]
        gridLabels[playerY][playerX].setText(""); // Clear old position
        
        if (keyCode == KeyEvent.VK_UP && playerY > 0) playerY--;
        if (keyCode == KeyEvent.VK_DOWN && playerY < height - 1) playerY++;
        if (keyCode == KeyEvent.VK_LEFT && playerX > 0) playerX--;
        if (keyCode == KeyEvent.VK_RIGHT && playerX < width - 1) playerX++;

        updatePlayerIcon();
        updateResources();
    }

    private void updatePlayerIcon() {
        gridLabels[playerY][playerX].setText("P");
        gridLabels[playerY][playerX].setForeground(Color.WHITE);
        gridLabels[playerY][playerX].setHorizontalAlignment(SwingConstants.CENTER);
        gridLabels[playerY][playerX].setFont(new Font("Arial", Font.BOLD, 20));
    }

    private void updateResources() {
        // Logic: Costs are deducted when entering a square [cite: 199, 475]
        foodBar.setValue(foodBar.getValue() - 5);
        waterBar.setValue(waterBar.getValue() - 5);
        moveBar.setValue(moveBar.getValue() - 2);
    }

    private JProgressBar createBar(Color color, String label) {
        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(100);
        bar.setForeground(color);
        bar.setStringPainted(true);
        bar.setPreferredSize(new Dimension(150, 20));
        return bar;
    }

    private void addStatRow(JPanel p, String text, JProgressBar bar, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = y;
        gbc.gridx = 0;
        JLabel l = new JLabel(text);
        l.setForeground(Color.WHITE);
        p.add(l, gbc);
        gbc.gridx = 1;
        p.add(bar, gbc);
    }
}
