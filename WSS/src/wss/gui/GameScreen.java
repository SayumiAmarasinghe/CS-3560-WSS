package wss.gui;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import wss.map.Map;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import wss.ai.brain.Brain;
import wss.entities.Player;
import wss.map.Difficulty;
import wss.terrain.TerrainSquare;
import wss.terrain.TerrainType;
import wss.trading.TradeOffer;
import wss.trading.Trader;
import wss.vision.VisionFactory;
import wss.gui.util.ImageLoader;
import wss.items.*;

public class GameScreen extends JFrame {
    private JPanel mapPanel;
    private JProgressBar foodBar, waterBar, moveBar;
    private JLabel[][] gridLabels;
    
    //use map class 
    private Map gameMap;
    private Player player;
    private int width;
    private int height;

    /**
     * @param w Map width - selected by user
     * @param h Map height -selected byuser
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
        setSize(900, 1000);
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
        setFocusable(true);
        requestFocusInWindow();
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
            dispose();
        }

    }

    private void refreshMapVisionTiles() {
        int tileSize = 60;
        int playerX = player.getXMapPos();
        int playerY = player.getYMapPos();

        // First fog every tile
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                JLabel tile = gridLabels[y][x];
    
                tile.setText("");
    
                if (x == width - 1 && y == height - 1) {
                    tile.setIcon(ImageLoader.loadIcon("exit.png", tileSize, tileSize));
                } else {
                    tile.setIcon(ImageLoader.loadIcon("fog.png", tileSize, tileSize));
                }
            }
        }
    
        // Reveal visible tiles
        TerrainSquare playerSquare = gameMap.getTerrainAt(playerY, playerX);
        TerrainType playerSquareType = playerSquare.getTerrainType();
        JLabel playerTile = gridLabels[playerY][playerX];
        playerTile.setIcon(buildTileIcon(playerSquareType, playerSquare, tileSize));

        int[][] visibleOffsets = player.getVision().getVisibleOffsets();
    
        for (int[] offset : visibleOffsets) {
            int x = playerX + offset[0];
            int y = playerY + offset[1];
    
            if (x < 0 || y < 0 || x >= width || y >= height) {
                continue;
            }
    
            TerrainSquare square = gameMap.getTerrainAt(y, x);
            TerrainType type = square.getTerrainType();
            JLabel tile = gridLabels[y][x];
    
            if (x == width - 1 && y == height - 1) {
                tile.setIcon(ImageLoader.loadIcon("exit.png", tileSize, tileSize));
            } else {
                tile.setIcon(buildTileIcon(type, square, tileSize));
            }
        }
    
        updatePlayerIcon();
        mapPanel.revalidate();
        mapPanel.repaint();
    }

    private ImageIcon buildTileIcon(TerrainType type, TerrainSquare square, int size) {
        BufferedImage base = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = base.createGraphics();
        // 1. TERRAIN (centered base)
        ImageIcon terrain = getTerrainIcon(type, size, size);
    
    
        g.drawImage(terrain.getImage(), 0, 0, size, size, null);
    
        // 2. OFF-CENTER OVERLAYS
        int overlaySize = (int)(size * 0.5);
    
        int offsetStep = size / 6; // controls spread
        int index = 0;
    
        if (square.hasTrader()) {
            ImageIcon trader = ImageLoader.loadIcon("trader.png", overlaySize, overlaySize);
    
            g.drawImage(trader.getImage(), 0, 0, overlaySize*2, overlaySize*2, null);
            index++;
        }
    
        if (square.hasItems()) {
            for (Item item : square.getItems()) {
    
                ImageIcon icon = null;
    
                if (item instanceof GoldBonus) {
                    icon = ImageLoader.loadIcon("gold.png", overlaySize, overlaySize);
                } else if (item instanceof WaterBonus) {
                    icon = ImageLoader.loadIcon("water.png", overlaySize, overlaySize);
                } else if (item instanceof FoodBonus) {
                    icon = ImageLoader.loadIcon("food.png", overlaySize, overlaySize);
                }
    
                if (icon != null) {
                    int x = size - overlaySize - offsetStep;
                    int y = offsetStep + (index * (overlaySize / 2));
    
                    g.drawImage(icon.getImage(), x, y, overlaySize, overlaySize, null);
                    index++;
                }
            }
        }
    
        g.dispose();
        return new ImageIcon(base);
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

        //smaller size for legend 
        int iconSize = 20;

        // Define legend items using your getTerrainIcon method
        addLegendItem(legendPanel, getTerrainIcon(TerrainType.PLAINS, iconSize, iconSize), "Plains", 0, gbc, false);
        addLegendItem(legendPanel, getTerrainIcon(TerrainType.MOUNTAIN, iconSize, iconSize), "Mountains", 1, gbc, false);
        addLegendItem(legendPanel, getTerrainIcon(TerrainType.DESERT, iconSize, iconSize), "Desert", 2, gbc, false);
        addLegendItem(legendPanel, getTerrainIcon(TerrainType.FOREST, iconSize, iconSize), "Forest", 3, gbc, false);
        addLegendItem(legendPanel, getTerrainIcon(TerrainType.SWAMP, iconSize, iconSize), "Swamp", 4, gbc, false);
        addLegendItem(legendPanel, getTerrainIcon(TerrainType.TUNDRA, iconSize, iconSize), "Tundra", 5, gbc, false);
        
        // For the exit, we show a sample icon (Plains) but flag it as the exit to get the gold border
        addLegendItem(legendPanel, ImageLoader.loadIcon("exit.png", iconSize, iconSize), "Exit (Bottom-Right)", 6, gbc, true);
        
        return legendPanel;
    }
    

    //add a single legend item with color and label
    private void addLegendItem(JPanel panel, ImageIcon icon, String name, int row, GridBagConstraints gbc, boolean isExit) {
        gbc.gridy = row;
        //color selection
        gbc.gridx = 0;
        JLabel iconLabel = new JLabel();
        iconLabel.setIcon(icon);
        iconLabel.setPreferredSize(new Dimension(20, 20));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setVerticalAlignment(SwingConstants.CENTER);
        //label 
        // if exit, ad gold border 
        if (name.contains("Exit")) {
            iconLabel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
        } else {
            iconLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        }
        panel.add(iconLabel, gbc);
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

        // Initialize Progress Bars
        foodBar = createBar(Color.ORANGE, "Food");
        waterBar = createBar(new Color(0x00BFFF), "Water");
        moveBar = createBar(Color.GREEN, "Move");

        addStatRow(panel, "Food:", foodBar, 0);
        addStatRow(panel, "Water:", waterBar, 1);
        addStatRow(panel, "Move:", moveBar, 2);

        return panel;
    }

    private void initializeMap() {
        int tileSize = 60;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                //fetch square by Map.java
                TerrainSquare square = gameMap.getTerrainAt(y, x);
                //System.out.println(square.hasItems());
                TerrainType type = square.getTerrainType();
                JLabel tile = new JLabel();
                tile.setOpaque(true);

                // Check for Exit tile specifically
                if (x == width - 1 && y == height - 1) {
                    tile.setIcon(ImageLoader.loadIcon("exit.png", tileSize, tileSize));
                } else {
                    tile.setIcon(buildTileIcon(type, square, tileSize));
                }
                
                tile.setBorder(BorderFactory.createLineBorder(new Color(0,0,0,50)));
                
                // Keep the text centered on top of the image icon
                tile.setHorizontalAlignment(SwingConstants.CENTER);
                tile.setVerticalAlignment(SwingConstants.CENTER);
                tile.setHorizontalTextPosition(JLabel.CENTER);
                tile.setVerticalTextPosition(JLabel.CENTER);
                
                gridLabels[y][x] = tile;
                mapPanel.add(tile);
            }
        }
        refreshMapVisionTiles();
    }

    // FIX: Takes iconWidth and iconHeight to properly size the images instead of using map width/height
    private ImageIcon getTerrainIcon(TerrainType type, int iconWidth, int iconHeight) {
        String fileName = "";
        switch(type) {
            case PLAINS: fileName = "plains_tile.png"; break;
            case MOUNTAIN: fileName = "mountain_tile.png"; break;
            case DESERT: fileName = "desert_tile.png"; break;
            case FOREST: fileName = "forest_tile.png"; break;
            case SWAMP: fileName = "swamp_tile.png"; break;
            default: fileName = "tundra_tile.png"; break; 
        }
        // Load the image for the terrain type at the requested pixel size
        return ImageLoader.loadIcon(fileName, iconWidth, iconHeight);
    }

    private void handleMovement(int keyCode) {
        //get current position of player 
        int currentX = player.getXMapPos();
        int currentY = player.getYMapPos();
        int nextX = currentX;
        int nextY = currentY;
        boolean moveOcurred = false;
        // 1. Determine Intent
        if (keyCode == KeyEvent.VK_UP && currentY > 0) {
            nextY--;
            moveOcurred = true;
        }
        else if (keyCode == KeyEvent.VK_DOWN && currentY < height - 1) {
            nextY++;
            moveOcurred = true;
        }
        else if (keyCode == KeyEvent.VK_LEFT) {
            if (currentX > 0) {
                nextX--; // Normal left move
            } else if (currentY > 0) {
                // Wrap to the right side of the row above
                nextX = width - 1; 
                nextY--;
            }
            moveOcurred = true;
        }
       else if (keyCode == KeyEvent.VK_RIGHT) {
            if (currentX < width - 1) {
                nextX++; // Normal right move
            } else if (currentY < height - 1) {
                // Wrap to the left side of the row below
                nextX = 0; 
                nextY++;
            }
            moveOcurred = true;
        }
        else if (keyCode == KeyEvent.VK_S) { 
            // Stay action
            gameMap.getTerrainAt(currentY, currentX).stayInSquare(player);
            updateUI();
            return;
        } else {
            return; // Invalid key or out of bounds
        }


        // 2. Fetch the target square from the backend map
        TerrainSquare target = gameMap.getTerrainAt(nextY, nextX);
        
        // 3. Check and Apply Logic
        if (target.canEnter(player)) {          
            target.enterSquare(player);         
            
            // 4. Update UI Grid (Clear the "P" from old square)
            gridLabels[currentY][currentX].setText(""); 
            
            // Optional: Re-draw background if switching to player.png image later
            // TerrainType oldType = gameMap.getTerrainAt(currentY, currentX).getTerrainType();
            // gridLabels[currentY][currentX].setIcon(getTerrainIcon(oldType, 60, 60));
            
            // 5. Update Player State
            player.setMapPosition(nextX, nextY); 
            if (moveOcurred) {
                refreshMapVisionTiles();
            } else {
                updatePlayerIcon();
            }

            if (target.hasTrader()) {
                handleTraderEncounter(target.getTrader());
            }
            
            // 6. Win Condition Check
            if (nextX == width - 1 && nextY == height - 1) {
                JOptionPane.showMessageDialog(this, "Success! You reached the East Edge.");
                dispose();
            }
        } else {
            // Provide feedback when move is denied due to lack of resources
            JOptionPane.showMessageDialog(this, "Cannot enter: insufficient reserves for this terrain.", "Move Denied", JOptionPane.WARNING_MESSAGE);
            TerrainSquare currentSquare = gameMap.getTerrainAt(currentY, currentX);
            currentSquare.stayInSquare(player);
        }
        
        // 7. Sync Progress Bars
        updateUI();
    }

    private void updatePlayerIcon() {
        int realX = player.getXMapPos();
        int realY = player.getYMapPos();
        gridLabels[realY][realX].setText("P");
        gridLabels[realY][realX].setForeground(Color.WHITE);
        gridLabels[realY][realX].setHorizontalAlignment(SwingConstants.CENTER);
        gridLabels[realY][realX].setFont(new Font("Arial", Font.BOLD, 20));
    }

    private void handleTraderEncounter(Trader trader) {
        TradeOffer offer = promptForTradeOffer(trader, null);

        if (offer == null) {
            return;
        }

        processTradeOffer(trader, offer);
    }

    private void processTradeOffer(Trader trader, TradeOffer offer) {
        if (!trader.canCompleteTrade(player, offer)) {
            JOptionPane.showMessageDialog(this,
                    "That trade cannot be completed because someone does not have enough resources.\n\n"
                            + getTradeResourceStatus(trader),
                    "Trade Failed",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String decision = trader.proposeTrade(offer);
        if (decision.equals(Trader.ACCEPT)) {
            trader.completeTrade(player, offer);
            JOptionPane.showMessageDialog(this,
                    trader.acceptTrade(offer),
                    "Trade Accepted",
                    JOptionPane.INFORMATION_MESSAGE);
            updateUI();
            return;
        }

        if (decision.equals(Trader.COUNTER)) {
            handleCounterOffer(trader, trader.counterOffer(offer, player));
            return;
        }

        JOptionPane.showMessageDialog(this,
                trader.rejectTrade(offer),
                "Trade Rejected",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private TradeOffer promptForTradeOffer(Trader trader, TradeOffer startingOffer) {
        JSpinner offeredGold = createTradeSpinner(getStartingOfferedGold(startingOffer));
        JSpinner offeredFood = createTradeSpinner(getStartingOfferedFood(startingOffer));
        JSpinner offeredWater = createTradeSpinner(getStartingOfferedWater(startingOffer));
        JSpinner requestedGold = createTradeSpinner(getStartingRequestedGold(startingOffer));
        JSpinner requestedFood = createTradeSpinner(getStartingRequestedFood(startingOffer));
        JSpinner requestedWater = createTradeSpinner(getStartingRequestedWater(startingOffer));

        JPanel resourcePanel = new JPanel(new GridLayout(0, 1));
        resourcePanel.add(new JLabel("Player: " + getPlayerResourceSummary()));
        resourcePanel.add(new JLabel("Trader: " + getTraderResourceSummary(trader)));

        JPanel offerPanel = new JPanel(new GridLayout(0, 4, 6, 6));
        offerPanel.add(new JLabel(""));
        offerPanel.add(new JLabel("Gold"));
        offerPanel.add(new JLabel("Food"));
        offerPanel.add(new JLabel("Water"));
        offerPanel.add(new JLabel("Offer"));
        offerPanel.add(offeredGold);
        offerPanel.add(offeredFood);
        offerPanel.add(offeredWater);
        offerPanel.add(new JLabel("Request"));
        offerPanel.add(requestedGold);
        offerPanel.add(requestedFood);
        offerPanel.add(requestedWater);

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.add(resourcePanel, BorderLayout.NORTH);
        panel.add(offerPanel, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(this,
                panel,
                trader.toString() + " - Make a Trade Offer",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        try {
            return createTradeOffer(
                    (Integer) offeredGold.getValue(),
                    (Integer) offeredFood.getValue(),
                    (Integer) offeredWater.getValue(),
                    (Integer) requestedGold.getValue(),
                    (Integer) requestedFood.getValue(),
                    (Integer) requestedWater.getValue());
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Invalid Trade Offer",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }

    private JSpinner createTradeSpinner(int startingValue) {
        return new JSpinner(new SpinnerNumberModel(startingValue, 0, 100, 1));
    }

    private int getStartingOfferedGold(TradeOffer offer) {
        return offer == null ? 0 : offer.getOfferedGold();
    }

    private int getStartingOfferedFood(TradeOffer offer) {
        return offer == null ? 0 : offer.getOfferedFood();
    }

    private int getStartingOfferedWater(TradeOffer offer) {
        return offer == null ? 0 : offer.getOfferedWater();
    }

    private int getStartingRequestedGold(TradeOffer offer) {
        return offer == null ? 0 : offer.getRequestedGold();
    }

    private int getStartingRequestedFood(TradeOffer offer) {
        return offer == null ? 0 : offer.getRequestedFood();
    }

    private int getStartingRequestedWater(TradeOffer offer) {
        return offer == null ? 0 : offer.getRequestedWater();
    }

    private TradeOffer createTradeOffer(int offeredGold, int offeredFood, int offeredWater,
                                        int requestedGold, int requestedFood, int requestedWater) {
        int totalOffered = offeredGold + offeredFood + offeredWater;
        int totalRequested = requestedGold + requestedFood + requestedWater;

        if (totalOffered == 0 && totalRequested == 0) {
            throw new IllegalArgumentException("Trade offer cannot be empty.");
        }

        return new TradeOffer(offeredGold, offeredFood, offeredWater,
                requestedGold, requestedFood, requestedWater);
    }

    private String getTradeResourceStatus(Trader trader) {
        return "Player: " + getPlayerResourceSummary() + "\n"
                + "Trader: " + getTraderResourceSummary(trader);
    }

    private String getPlayerResourceSummary() {
        return player.getGold() + " gold, "
                + player.getCurrentFood() + " food, "
                + player.getCurrentWater() + " water";
    }

    private String getTraderResourceSummary(Trader trader) {
        return trader.getGold() + " gold, "
                + trader.getFood() + " food, "
                + trader.getWater() + " water";
    }

    private void handleCounterOffer(Trader trader, TradeOffer counterOffer) {
        String[] options = {"Accept Counteroffer", "Make New Offer", "End Trade"};
        int answer = JOptionPane.showOptionDialog(this,
                trader.getName() + " makes a counteroffer:\n" + counterOffer,
                "Counteroffer",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (answer == 0 && trader.canCompleteTrade(player, counterOffer)) {
            trader.completeTrade(player, counterOffer);
            JOptionPane.showMessageDialog(this,
                    trader.acceptTrade(counterOffer),
                    "Trade Accepted",
                    JOptionPane.INFORMATION_MESSAGE);
            updateUI();
            return;
        }

        if (answer == 0) {
            JOptionPane.showMessageDialog(this,
                    "That counteroffer cannot be completed because someone does not have enough resources.\n\n"
                            + getTradeResourceStatus(trader),
                    "Trade Failed",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (answer == 1) {
            TradeOffer newOffer = promptForTradeOffer(trader, counterOffer);
            if (newOffer != null) {
                processTradeOffer(trader, newOffer);
            }
            return;
        }

        JOptionPane.showMessageDialog(this,
                trader.rejectTrade(counterOffer),
                "Trade Ended",
                JOptionPane.INFORMATION_MESSAGE);
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
