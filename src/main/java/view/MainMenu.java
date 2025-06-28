package view;

import controller.HighScoresController;
import controller.StartGameController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;

public class MainMenu extends JPanel {

    private Image backgroundImage;
    private Image startImage;
    private Image exitImage;
    private Image highScoreImage;

    private JRadioButton easy, normal, hard; // Declare radio buttons as fields
    private JTextField name;
    private JPanel parentPanel;
    private CardLayout cardLayout;

    Color mycolor = new Color(175, 25, 25);

    public MainMenu(CardLayout cardLayout, JPanel parentPanel) {
        this.cardLayout = cardLayout;
        this.parentPanel = parentPanel;

        // Load the images once
        loadBackgroundImage();
        loadStartImage();
        loadExitImage();
        loadHighScoreImage();

        this.setLayout(new BorderLayout(10, 10));

        // Title Label
        JLabel titleLabel = new JLabel("Select Difficulty:");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(mycolor);

        // User Panel
        JPanel userPanel = new JPanel(new BorderLayout());
        JLabel nameLabel = new JLabel("PLEASE WRITE YOUR NAME ");
        nameLabel.setOpaque(false);
        nameLabel.setForeground(Color.RED);

        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        namePanel.setOpaque(false);

        name = new JTextField(20); // Set width
        name.setFont(new Font("Arial", Font.PLAIN, 16));
        name.setForeground(Color.RED);
        name.setOpaque(false);

        namePanel.add(nameLabel);
        namePanel.add(name);

        // Create Radio Panel
        JPanel radioPanel = createRadioPanel();
        userPanel.add(namePanel, BorderLayout.NORTH);
        userPanel.add(radioPanel, BorderLayout.CENTER);
        userPanel.setOpaque(false);

        // Button Panel
        JPanel buttonPanel = createButtonPanel();

        // Add Components
        this.add(titleLabel, BorderLayout.NORTH);
        this.add(userPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createRadioPanel() {
        easy = new JRadioButton("Easy");
        normal = new JRadioButton("Normal");
        hard = new JRadioButton("Hard");

        easy.setFont(new Font("Arial", Font.PLAIN, 20));
        normal.setFont(new Font("Arial", Font.PLAIN, 20));
        hard.setFont(new Font("Arial", Font.PLAIN, 20));

        easy.setOpaque(false);
        normal.setOpaque(false);
        hard.setOpaque(false);

        easy.setForeground(mycolor);
        normal.setForeground(mycolor);
        hard.setForeground(mycolor);

        ButtonGroup radioGroup = new ButtonGroup();
        radioGroup.add(easy);
        radioGroup.add(normal);
        radioGroup.add(hard);

        JPanel radioPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        radioPanel.add(easy, gbc);
        gbc.gridy++;
        radioPanel.add(normal, gbc);
        gbc.gridy++;
        radioPanel.add(hard, gbc);
        radioPanel.setOpaque(false);

        return radioPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        buttonPanel.setPreferredSize(new Dimension(0, 100));

        // Create buttons
        JButton startButton = createButton("START", startImage);
        JButton highScoreButton = createButton("HIGH SCORES", highScoreImage);
        JButton exitButton = createButton("EXIT", exitImage);

        // Add action listeners
        startButton.addActionListener(e -> {
            new StartGameController(cardLayout, parentPanel, this, null).actionPerformed(e);
        });

        highScoreButton.addActionListener(e -> {
            new HighScoresController(cardLayout, parentPanel).actionPerformed(e);
        });

        exitButton.addActionListener(e -> {
            System.exit(0); //
        });

        // Add buttons to the panel
        buttonPanel.add(startButton);
        buttonPanel.add(highScoreButton);
        buttonPanel.add(exitButton);

        buttonPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int buttonWidth = buttonPanel.getWidth() / 3; // Divide width by 3 for each button
                int buttonHeight = buttonPanel.getHeight();

                if (buttonWidth > 0 && buttonHeight > 0) {
                    startButton.setIcon(
                            new ImageIcon(startImage.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH)));
                    highScoreButton.setIcon(new ImageIcon(
                            highScoreImage.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH)));
                    exitButton.setIcon(
                            new ImageIcon(exitImage.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH)));
                }
            }
        });

        return buttonPanel;
    }

    private JButton createButton(String text, Image image) {
        JButton button = new JButton(text);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(true);

        // Set an initial scaled icon
        button.setIcon(new ImageIcon(image.getScaledInstance(450, 100, Image.SCALE_SMOOTH)));

        return button;
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        if (backgroundImage != null) {
            grphcs.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void loadBackgroundImage() {
        backgroundImage = loadImage("images/mainMenu.png");
    }

    private void loadStartImage() {
        startImage = loadImage("images/start.png");
    }

    private void loadExitImage() {
        exitImage = loadImage("images/exit.png");
    }

    private void loadHighScoreImage() {
        highScoreImage = loadImage("images/highScores.png");
    }

    private Image loadImage(String path) {
        try {
            var file = new File(path);
            if (file.exists()) {
                return ImageIO.read(file);
            } else {
                System.out.println("Image not found: " + path);
                return null;
            }
        } catch (IOException e) {
            System.out.println("Error loading image: " + e.getMessage());
            return null;
        }
    }

    public String getPlayerName() {
        return name.getText().trim().isEmpty() ? "default" : name.getText().trim();
    }

    public String getSelectedDifficulty() {
        if (easy.isSelected())
            return "Easy";
        if (normal.isSelected())
            return "Normal";
        if (hard.isSelected())
            return "Hard";
        return null;
    }
}
