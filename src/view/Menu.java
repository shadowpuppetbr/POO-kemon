package view;

import game.Game;
import game.GameSave;
import java.awt.*;
import javax.swing.*;

public class Menu extends JFrame {

    public Menu() {
        setTitle("POOkemon");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);
        Image icon = new ImageIcon("src/resources/images/pokeball.png").getImage();
        setIconImage(icon.getScaledInstance(30, 30, Image.SCALE_SMOOTH));


        // Menu buttons
        JButton playButton = new JButton("Jogar");
        JButton loadButton = new JButton("Carregar Jogo");
        JButton exitButton = new JButton("Sair");

        playButton.addActionListener(_ -> {
            new Game().startNewGame();
            
            dispose();
        });
        
        loadButton.addActionListener(_ -> {
            Game loadedGame = GameSave.loadGame();
            if (loadedGame != null) {
                loadedGame.resumeGameAfterLoading();
                dispose();
            }
        });

        exitButton.addActionListener(_ -> System.exit(0));

        // Main panel: NORTH -> Image | CENTER -> Buttons
        JPanel mainPanel = new JPanel(new BorderLayout(10, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
 
        Image menuImage = new ImageIcon("src/resources/images/2ihuz31s.png").getImage();
        JLabel menuImageLabel = new JLabel(new ImageIcon(menuImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH)));

        mainPanel.add(menuImageLabel, BorderLayout.NORTH);

        // Panel for the buttons (CENTER)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10)); // 3 botões
        buttonPanel.add(playButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(exitButton);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);
    }
}