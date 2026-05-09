package wss.gui;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import wss.map.Map;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import wss.ai.brain.Brain;
import wss.entities.Player;
import wss.map.Difficulty;
import wss.terrain.TerrainSquare;
import wss.terrain.TerrainType;
import wss.vision.VisionFactory;

public class GameScreen extends JFrame {
    private JPanel mapPanel;
    private JProgressBar foodBar, waterBar, moveBar;
    private JLabel[][] gridLabels;
    
    //use map class 
    private Map gameMap;
    private Player player;

    private int playerX = 0;
    private int playerY = 0;
    private int width;
    private int height;

    /**
     * @param Map width - selected by user
     * @param Map height -selected byuser
     * @param difficulty - selected byuser
     * @param vision Name - randomly assigned
     */
    
    public GameScreen(int w, int h, String difficulty, String vision) {
        this.width = w;
        this.height = h;
        Difficulty difficultyLevel = Difficulty.valueOf(difficulty);
        //initialize map with user-selected dimensions and difficulty
        this.gameMap = new Map(height, width, difficultyLevel);

        //initialize player with random vision and starting resources
        this.player = new Player(100, 100, 100, 100, 
                    VisionFactory.createVision(vision, difficultyLevel), gameMap);
        setTitle("WSS Game - Journey East");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(0x123456));
        setLayout(new BorderLayout(10, 10));

        // --- TOP RIGHT: STATS DASHBOARD + LEGEND ---
        JPanel statsPanel = createStatsDashboard();
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(statsPanel, BorderLayout.EAST);
        topPanel.add(createLegendPanel(), BorderLayout.WEST);
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
        updateUI();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateUI() {
        foodBar.setValue(player.getCurrentFood());
        waterBar.setValue(player.getCurrentWater());
        moveBar.setValue(player.getMovementPoints());

        //display current food / max food on the bar
        foodBar.setString(player.getCurrentFood() + "/" + player.getMaxFood());
        waterBar.setString(player.getCurrentWater() + "/" + player.getMaxWater());
        moveBar.setString(String.valueOf(player.getMovementPoints()));

        //show message if we're out of resources 
        if (player.getCurrentFood() <= 0 || player.getCurrentWater() <= 0) {
            JOptionPane.showMessageDialog(this, "Game Over: You ran out of resources.");
        }

    }

    //create a legend panel to explain terrain types and their colors
    private Component createLegendPanel() {
        JPanel legendPanel = new JPanel(new GridBagLayout());
        legendPanel.setOpaque(false);
        legendPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE), "TERRAIN KEY", 
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
            new Font("MV Boli", Font.PLAIN, 14), Color.WHITE));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 10, 2, 10);
        gbc.anchor = GridBagConstraints.WEST;

        //define legend items matching terrain colors
    addLegendItem(legendPanel, new Color(0x34A853), "Plains", 0, gbc);
    addLegendItem(legendPanel, new Color(0x707070), "Mountains", 1, gbc);
    addLegendItem(legendPanel, new Color(0xEDC9AF), "Desert", 2, gbc);
    addLegendItem(legendPanel, new Color(0x0B6623), "Forest", 3, gbc);
    addLegendItem(legendPanel, new Color(0x4A5D23), "Swamp", 4, gbc);
    addLegendItem(legendPanel, new Color(0xA5F2F3), "Tundra", 5, gbc);
    addLegendItem(legendPanel, new Color(0xFFD700), "Exit", 6, gbc);
    return legendPanel;
    }
    

    //add a single legend item with color and label
    private void addLegendItem(JPanel panel, Color color, String name, int row, GridBagConstraints gbc) {
        gbc.gridy = row;
        //color selection
        gbc.gridx = 0;
        JLabel colorLabel = new JLabel();
        colorLabel.setOpaque(true);
        colorLabel.setBackground(color);
        colorLabel.setPreferredSize(new Dimension(15, 15));
        colorLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        panel.add(colorLabel, gbc);

        //label 
        gbc.gridx = 1;
        JLabel nameLabel = new JLabel(name);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("MV Boli", Font.PLAIN, 12));
        panel.add(nameLabel, gbc);
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
                //fetch square by Map.java
                TerrainSquare square = gameMap.getTerrainAt(y, x);
                TerrainType type = square.getTerrainType();
                JLabel tile = new JLabel();
                tile.setOpaque(true);
                // Assign colors based on terrain types [cite: 204, 233, 234, 235]
                tile.setBackground(getTerrainColor(type, y)); 
                tile.setBorder(BorderFactory.createLineBorder(new Color(0,0,0,50)));
                gridLabels[y][x] = tile;
                mapPanel.add(tile);
            }
        }
        updatePlayerIcon();
    }

    private Color getTerrainColor(TerrainType type, int x) {
        // Simple logic to demonstrate different terrains [cite: 233-238]
        if (x == width - 1) return new Color(0xFFD700);
        switch(type) {
            case PLAINS: return new Color(0x34A853); // Plains
            case MOUNTAIN: return new Color(0x707070); // Mountains
            case DESERT: return new Color(0xEDC9AF); // Desert
            case FOREST: return new Color(0x0B6623); // Forest
            case SWAMP: return new Color(0x4A5D23); // Swamp
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