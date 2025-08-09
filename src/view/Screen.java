package view;

import game.Board;
import java.awt.*;
import javax.swing.*;

public class Screen extends JFrame {
    private Board board;


    public void initializeScreen() {
        setTitle("POOkemon");

        Image icon = new ImageIcon("src/resources/images/pokeball.png").getImage();
        setIconImage(icon.getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout(FlowLayout.LEFT));

        
        // LEFT board
        board = new Board();
        board.setPreferredSize(new Dimension(640, 640));
        JPanel boardWrapper = new JPanel();
        boardWrapper.setLayout(new FlowLayout());
        boardWrapper.add(board);
        add(boardWrapper);

        // RIGHT game info

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(3, 1, 10, 10));
        add(rightPanel);


        JPanel infoPanel = new JPanel(new FlowLayout());
        JLabel scoreLabel = new JLabel("Placar: 0");
        JLabel roundLabel = new JLabel("Rodada: 1");
        infoPanel.add(scoreLabel);
        infoPanel.add(roundLabel);
        infoPanel.setBackground(new Color(Color.GREEN.getRGB()));

        rightPanel.add(infoPanel);

        // EAST botões de controle
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        JButton attackButton = new JButton("Atacar");
        JButton defendButton = new JButton("Defender");
        JButton endTurnButton = new JButton("Finalizar Turno");
        controlPanel.add(attackButton);
        controlPanel.add(defendButton);
        controlPanel.add(endTurnButton);
        controlPanel.setBackground(new Color(Color.YELLOW.getRGB()));
        
        rightPanel.add(controlPanel);

        // Set the window size
        setSize(960,720);
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    public Board getBoard() {
        return board;
    }
}
