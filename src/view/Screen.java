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

        // Create and add the board
        board = new Board();
        add(board, BorderLayout.CENTER);

        // Set the window size
        setSize(500, 500);
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    public Board getBoard() {
        return board;
    }
}
