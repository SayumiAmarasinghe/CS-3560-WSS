package wss.gui;

import java.awt.*;
import java.util.Random;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import wss.vision.VisionFactory;
//import wss.Difficulty;
import wss.map.Difficulty;

public class GameSetupDialog extends JDialog {
    private JTextField widthField;
    private JTextField heightField;
    private JComboBox<String> difficultyBox;
    private JLabel visionDisplayLabel;
    private boolean confirmed = false;
    private String randVision;

    public GameSetupDialog(JFrame parent) {
        super(parent, "New Game Settings", true);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(0x123456));

        // --- INPUT PANEL ---
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false);
        inputPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Styled Labels
        Font labelFont = new Font("MV Boli", Font.PLAIN, 16);
        
        // Map Width
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(createLabel("Map Width:", labelFont), gbc);
        gbc.gridx = 1;
        widthField = new JTextField("10", 5);
        inputPanel.add(widthField, gbc);

        // Map Height
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(createLabel("Map Height:", labelFont), gbc);
        gbc.gridx = 1;
        heightField = new JTextField("10", 5);
        inputPanel.add(heightField, gbc);

        // Difficulty Selection [cite: 136, 273]
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(createLabel("Difficulty:", labelFont), gbc);
        gbc.gridx = 1;
        String[] levels = {"EASY", "MEDIUM", "HARD"};
        difficultyBox = new JComboBox<>(levels);
        inputPanel.add(difficultyBox, gbc);

        //vision display 
         gbc.gridx = 0; gbc.gridy = 3;
         inputPanel.add(createLabel("Vision:", labelFont), gbc);
         visionDisplayLabel = new JLabel();
         visionDisplayLabel.setForeground(Color.WHITE);
         visionDisplayLabel.setFont(new Font("MV Boli", Font.BOLD, 18));
        gbc.gridx = 1;
        inputPanel.add(visionDisplayLabel, gbc);

        // Update vision display when difficulty changes
        difficultyBox.addActionListener(e -> generateRandomVision());

        //initialize random vision
        generateRandomVision(); // Initial display

         



        add(inputPanel, BorderLayout.CENTER);

        // --- BUTTON PANEL ---
        JButton startBtn = new JButton("Begin Journey");
        startBtn.setFont(new Font("MV Boli", Font.BOLD, 18));
        startBtn.addActionListener(e -> {
            confirmed = true;
            dispose();
        });
        
        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.add(startBtn);
        add(btnPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
    }

    private void generateRandomVision() {
        //select a difficulty 
        String selectedStr = (String) difficultyBox.getSelectedItem();
        Difficulty selectedDiff = Difficulty.valueOf(selectedStr);
        //get allowed visions for that difficulty
        List<String> allowed = VisionFactory.getAllowedVisionNames(selectedDiff);

        //pick one randomly
        Random rand = new Random();
        randVision = allowed.get(rand.nextInt(allowed.size()));

        //update UI 
        visionDisplayLabel.setText(randVision);
    }

    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(font);
        return label;
    }

    // Getters for Map Class [cite: 262]
    public int getMapWidth() { 
        return Integer.parseInt(widthField.getText()); 
    }
    public int getMapHeight() {
         return Integer.parseInt(heightField.getText()); 
        }
    public String getDifficulty() {
         return (String) difficultyBox.getSelectedItem();
         }
    public String getVisionName() {
        return randVision;
    }
    public boolean isConfirmed() { 
        return confirmed; }
}
