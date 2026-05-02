package wss.gui.util;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import wss.gui.util.ImageLoader;

public class InstrScreen extends JFrame {
    public InstrScreen() {
        // Create the JFrame with the same WSS theme
        JFrame frame = new JFrame("WSS - Instructions");
        frame.setSize(500, 600);
        frame.getContentPane().setBackground(new Color(0x123456));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        // Header matching EntryScreen title style
        JLabel header = new JLabel("How to Play");
        header.setForeground(Color.WHITE);
        header.setFont(new Font("MV Boli", Font.BOLD, 30));
        header.setIcon(ImageLoader.loadIcon("player.png", 50, 50));
        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        frame.add(header, BorderLayout.NORTH);

        // Content Area
        JTextArea rulesArea = new JTextArea();
        rulesArea.setEditable(false);
        rulesArea.setLineWrap(true);
        rulesArea.setWrapStyleWord(true);
        rulesArea.setMargin(new Insets(10, 10, 10, 10));
        
        // Match the background and font style
        rulesArea.setBackground(new Color(0x123456));
        rulesArea.setForeground(Color.WHITE);
        rulesArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        rulesArea.setText(generateRulesText());

        JScrollPane scrollPane = new JScrollPane(rulesArea);
        scrollPane.setBorder(null); // Clean look
        scrollPane.getViewport().setBackground(new Color(0x123456));
        frame.add(scrollPane, BorderLayout.CENTER);

        // Styled Button
        JButton closeButton = new JButton("Ready to Start!");
        closeButton.setFont(new Font("MV Boli", Font.PLAIN, 18));
        closeButton.setBackground(Color.WHITE);
        closeButton.addActionListener(e -> frame.dispose());
        frame.add(closeButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
    private String generateRulesText() {
        return "--- THE MISSION ---\n\n"  +
               "Travel from the West side of the map and exit through the East edge.\n\n" +

               "--- PLAYER STATS ---\n\n" +
               "You start with set amounts of Food, Water, and Movement Points.\n" +
               "• Moving: Entering a square costs resources. If you lack the required amount, you cannot enter. \n" +
               "• Standing Still: You can stay in your current square to gain 2 Movement Points, but you will still consume Food and Water at 1/2 the normal rate. \n\n" +

               "--- TERRAIN & COSTS ---\n" +
               "The map has Plains, Mountains, Deserts, Swamps, Forests, and Tundra as its terrains. \n" +
               "Each has three costs: Movement, Water, and Food.\n\n" +

               "--- ITEMS & TRADERS ---\n\n" +
               "• Items: You may find Food, Water, or Gold bonuses. \n" +
               "• Traders: You can trade Gold, Food, or Water. Traders have 'Patience' levels; if you offer poor trades, they may stop negotiating.\n\n" +

               "--- WINNING & LOSING ---\n\n" +
               "• WIN: Successfully reach any square on the East edge of the map. \n" +
               "• LOSE: If your Food or Water reserves drop to 0 or less, your journey ends in Game Over.\n\n ";
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InstrScreen().setVisible(true);
        });
    }

    }

