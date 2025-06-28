package view;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class GameRunner extends JFrame {
    private Image logoImage;
    private MyMenu myMenu;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    public MyMenu getMyMenu() {
        return myMenu;
    }

    public GameRunner() {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Labyrinth Game");

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        // this.setSize(800, 600); // Default size when not maximized

        setMinimumSize(new Dimension(800, 600));

        this.setLocationRelativeTo(null); // Center the frame in normal mode

        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);

        MainMenu mainMenu = new MainMenu(cardLayout, cardPanel);

        cardPanel.add(mainMenu, "MainMenu");

        this.add(cardPanel);
        this.setLocationRelativeTo(null);

        myMenu = new MyMenu(cardLayout, cardPanel, mainMenu);
        this.setJMenuBar(myMenu);
        myMenu.getMenu(0).setEnabled(false);

        setLogoImage();

        this.pack();
    }

    private void setLogoImage() {
        try {
            // Check if the resource exists
            var file = new File("images/logo.png");
            if (file.exists()) {
                logoImage = ImageIO.read(file);
                this.setIconImage(logoImage);
            } else {
                // Resource not found, setting backgroundImage to null and continuing
                System.out.println("logo image not found: images/logo.png");
                logoImage = null;
            }
        } catch (IOException e) {
            // Catch any IOException that may occur during reading
            System.out.println("Error loading logo image: " + e.getMessage());
            logoImage = null;
        }
    }

}
