package wss.gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import wss.gui.util.ImageLoader;

public class InstrScreen extends JFrame {
    
    public InstrScreen() {
        // Create the JFrame 
        setTitle("WSS - Instructions");
        setSize(650, 750); 
        getContentPane().setBackground(new Color(0x123456));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // header
        JLabel header = new JLabel("How to Play");
        header.setForeground(Color.WHITE);
        header.setFont(new Font("MV Boli", Font.BOLD, 40));
        header.setIcon(ImageLoader.loadIcon("player_tile.png", 60, 60)); // Swapped to base player.png
        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(header, BorderLayout.NORTH);

        //content
        JEditorPane rulesArea = new JEditorPane();
        rulesArea.setEditable(false);
        rulesArea.setContentType("text/html"); 
        rulesArea.setBackground(new Color(0x123456));
        rulesArea.setText(generateRulesText());

        JScrollPane scrollPane = new JScrollPane(rulesArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Clean padding
        scrollPane.getViewport().setBackground(new Color(0x123456));

        //custom scrollbar 
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        
        JPanel scrollContainer = new JPanel(new BorderLayout());
        scrollContainer.setBackground(new Color(0x123456));
        scrollContainer.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Clean padding
        scrollContainer.add(scrollPane, BorderLayout.CENTER);

        add(scrollContainer, BorderLayout.CENTER);

        //button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0)); // Bottom padding

        JButton closeButton = createStyledButton("Ready to Start!", new Color(0x34A853)); // Green hover
        closeButton.addActionListener(e -> {
            GameSetupDialog dialog = new GameSetupDialog(this);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                int w = dialog.getMapWidth();
                int h = dialog.getMapHeight();
                String diff = dialog.getDifficulty();
                String vision = dialog.getVisionName();
        
                System.out.println("Initializing " + diff + " map: " + w + "x" + h);
        
                dispose(); // Close instruction screen
                new GameScreen(w, h, diff, vision); // Launch the game
            }
        });
        
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * instructions text
     */
    private String generateRulesText() {
        return "<html><body style='font-family: Arial, sans-serif; color: white; margin: 10px; font-size: 16px;'>" +
                
               "<h2 style='color: #FFD700; font-family: \"MV Boli\"; font-size: 24px; border-bottom: 1px solid #FFD700; padding-bottom: 5px;'>&#9876; THE MISSION</h2>" +
               "<p>Travel from the West side of the map and safely reach the <b>East Edge Exit</b>.</p><br>" +

               "<h2 style='color: #FFD700; font-family: \"MV Boli\"; font-size: 24px; border-bottom: 1px solid #FFD700; padding-bottom: 5px;'>&#128100; PLAYER STATS</h2>" +
               "<p>You start with set amounts of <span style='color: #FFA500;'><b>Food</b></span>, <span style='color: #00BFFF;'><b>Water</b></span>, and <span style='color: #32CD32;'><b>Movement Points</b></span>.</p>" +
               "<ul style='margin-top: 5px;'>" +
               "<li><b>Moving:</b> Entering a square costs resources. If you lack the required amount, you cannot enter.</li>" +
               "<li><b>Standing Still:</b> You can stay in your current square to gain <b>+2 Movement</b>, but you will still consume Food and Water at 1/2 the normal rate.</li>" +
               "</ul><br>" +

               "<h2 style='color: #FFD700; font-family: \"MV Boli\"; font-size: 24px; border-bottom: 1px solid #FFD700; padding-bottom: 5px;'>&#127757; TERRAIN & COSTS</h2>" +
               "<p>The map consists of Plains, Mountains, Deserts, Swamps, Forests, and Tundra. Each terrain has different costs for entry. Plan your route carefully!</p><br>" +

               "<h2 style='color: #FFD700; font-family: \"MV Boli\"; font-size: 24px; border-bottom: 1px solid #FFD700; padding-bottom: 5px;'>&#128176; ITEMS & TRADERS</h2>" +
               "<ul style='margin-top: 5px;'>" +
               "<li><b>Items:</b> You may find stat bonuses scattered across the map.</li>" +
               "<li><b>Traders:</b> You can barter resources. Traders have <i>Patience</i> levels; if you offer poor trades, they will stop negotiating with you.</li>" +
               "</ul><br>" +

               "<h2 style='color: #FFD700; font-family: \"MV Boli\"; font-size: 24px; border-bottom: 1px solid #FFD700; padding-bottom: 5px;'>&#127942; WINNING & LOSING</h2>" +
               "<ul style='margin-top: 5px;'>" +
               "<li><span style='color: #FFD700;'><b>WIN:</b></span> Successfully reach the Exit square on the Bottom-Right edge of the map.</li>" +
               "<li><span style='color: #FF6347;'><b>LOSE:</b></span> If your Food or Water reserves drop to 0, your journey ends.</li>" +
               "</ul>" +

               "</body></html>";
    }

    /**
     * creates custom ui button
     */
    private JButton createStyledButton(String text, Color hoverColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("MV Boli", Font.BOLD, 22));
        
        btn.setBackground(new Color(0x123456));
        btn.setForeground(Color.WHITE);
        
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2),
            BorderFactory.createEmptyBorder(10, 30, 10, 30)
        ));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(hoverColor); 
                btn.setForeground(new Color(0x123456)); 
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InstrScreen().setVisible(true);
        });
    }
}