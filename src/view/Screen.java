package view;

import game.Board;
import java.awt.*;
import javax.swing.*;

public class Screen extends JFrame {
    private final Board board;

    private JButton debugButton;
    private JButton hintButton;
    private JButton changePokemonButton;
    private JButton endTurnButton;
    private JButton saveButton; // Botão adicionado
    private JButton exitButton;

    public Screen(Board board) {
        super();
        this.board = board;
    }

    public void initializeScreen() {
        setTitle("POOkemon");

        Image icon = new ImageIcon("src/resources/images/pokeball.png").getImage();
        setIconImage(icon.getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setResizable(false);
        
        
        // LEFT board
        this.board.setPreferredSize(new Dimension(640, 640));
        JPanel boardWrapper = new JPanel();
        boardWrapper.setLayout(new FlowLayout());
        boardWrapper.add(this.board);
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
        debugButton = new JButton("Debug");
        hintButton = new JButton("Dica");
        changePokemonButton = new JButton("Trocar Pokémon principal");
        endTurnButton = new JButton("Finalizar Turno");
        saveButton = new JButton("Salvar Jogo"); // Botão criado
        exitButton = new JButton("Sair do Jogo");
        controlPanel.add(debugButton);
        controlPanel.add(hintButton);
        controlPanel.add(changePokemonButton);
        controlPanel.add(endTurnButton);
        controlPanel.add(saveButton); // Botão adicionado ao painel
        controlPanel.add(exitButton);
        controlPanel.setBackground(new Color(Color.YELLOW.getRGB()));
        
        rightPanel.add(controlPanel);

        // Set the window size
        setSize(960,720);
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    public Board getBoard() {
        return this.board;
    }

    public JButton getDebugButton() {
        return debugButton;
    }

    public JButton getHintButton(){
        return hintButton;
    }

    public JButton getChangePokemonButton() {
        return changePokemonButton;
    }

    public JButton getExitButton() {
        return exitButton;
    }

    public JButton getEndTurnButton() {
        return endTurnButton;
    }

    public JButton getSaveButton() { // Getter para o novo botão
        return saveButton;
    }
}