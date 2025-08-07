package view;

import game.Board;

import javax.swing.*;
import java.awt.*;

public class Screen extends JFrame {
    private Board board;

    public Screen() {
        initializeScreen();
    }

    private void initializeScreen() {
        setTitle("POOkemon");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // NORTH informações do jogo
        JPanel infoPanel = new JPanel(new FlowLayout());
        JLabel scoreLabel = new JLabel("Placar: 0");
        JLabel roundLabel = new JLabel("Rodada: 1");
        infoPanel.add(scoreLabel);
        infoPanel.add(roundLabel);
        add(infoPanel, BorderLayout.NORTH);

        // CENTER tabuleiro
        board = new Board();
        board.setPreferredSize(new Dimension(600, 600));
        JPanel boardWrapper = new JPanel();
        boardWrapper.setLayout(new FlowLayout());
        boardWrapper.add(board);
        add(boardWrapper, BorderLayout.CENTER);

        // EAST botões de controle
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        JButton attackButton = new JButton("Atacar");
        JButton defendButton = new JButton("Defender");
        JButton endTurnButton = new JButton("Finalizar Turno");
        controlPanel.add(attackButton);
        controlPanel.add(defendButton);
        controlPanel.add(endTurnButton);
        add(controlPanel, BorderLayout.EAST);

        // Set the window size
        setSize(1260, 720);
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    public Board getBoard() {
        return board;
    }
}
